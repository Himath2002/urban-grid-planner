# Architecture notes

UrbanGrid Planner uses explicit boundaries so the planning model can be tested and extended independently of its console interface.

## Dependency direction

```text
application ─┬─> io ───────────> domain <────────── rule
             ├─> presentation ─> domain
             ├─> service ──────> domain
             └─> strategy ─────> domain

service ─────> strategy
io ──────────> rule
```

The domain never imports the application, file loader, renderer, or service packages.

## Domain invariants

- `BuildingPlan` always has at least one floor and non-null foundation/material values.
- `CityGrid` always has positive dimensions and exactly `rows × columns` non-null cells.
- `Cell` owns a non-null terrain and zoning policy.
- `BuildEstimate` and `CityBuildResult` cannot contain negative costs.
- coordinates in `BuildPlacement` are zero-based internally and non-negative.
- collections crossing result boundaries are copied before storage.

These invariants prevent partially valid objects from leaking into planning algorithms.

## Strategy boundary

`BuildStrategy` proposes a structure for a coordinate. It does not decide whether construction is legal and does not calculate cost. `CityBuilder` owns that orchestration:

1. request a plan from the strategy;
2. evaluate it against the target cell;
3. calculate an estimate only when accepted;
4. record an immutable placement;
5. aggregate the city result.

This separation means a new strategy cannot accidentally bypass zoning.

## Decorator boundary

Every zoning rule implements one policy contract:

- permission: `allows`
- economic adjustment: `costMultiplier`
- rejection explanation
- compact renderer code
- human-readable description and warning
- contamination marker

`RuleDecorator` delegates by default. Concrete rules override only the behavior they add, allowing independent policies to compose without a combinatorial inheritance tree.

## I/O boundary

`GridFileLoader` is the only component that knows the text grammar. It:

- reads UTF-8;
- validates dimensions before allocation;
- limits input to 100,000 cells;
- requires exactly one record per cell;
- rejects unknown or malformed zoning tokens;
- rejects trailing non-blank data;
- translates raw strings into type-safe domain values.

The parser returns a fully valid `CityGrid` or fails with a contextual exception. There is no partially loaded state.

## Presentation boundary

`GridRenderer` produces strings; it does not print. The console decides where those strings go. Stable rendering makes selection and city-build views testable without capturing global output streams.

## Extension checklist

When adding behavior:

1. preserve the domain invariants;
2. keep input translation in `io`;
3. keep policy composition in `rule`;
4. keep coordinate-generation algorithms in `strategy`;
5. keep cross-cell orchestration in `service`;
6. add focused tests at the changed boundary;
7. document grammar or cost changes in both README and changelog.
