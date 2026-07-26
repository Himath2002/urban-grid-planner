# Contributing

UrbanGrid Planner welcomes focused improvements that preserve its model clarity.

## Before opening a change

- Use a short-lived branch from `main`.
- Keep behavior changes separate from documentation-only changes.
- Discuss changes to file grammar or cost semantics in an issue first.
- Do not commit IDE settings, generated grids, build output, or credentials.

## Local verification

```bash
./gradlew clean build
```

For behavior changes, also run the sample:

```bash
./gradlew run --args="examples/sample-grid.txt"
```

## Commit style

Use an imperative Conventional Commit subject:

```text
feat: add industrial zoning decorator
fix: reject non-finite flood risk
docs: explain central build density
test: cover stacked zoning multipliers
```

## Pull requests

A strong pull request:

- explains the problem and chosen boundary;
- includes tests that fail without the change;
- updates input-format and cost documentation when needed;
- states compatibility and parsing risks;
- avoids unrelated formatting or generated files.
