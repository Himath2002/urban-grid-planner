# Security policy

## Supported version

Security fixes are applied to the latest published release and the `main` branch.

## Report privately

Please do not open a public issue for a suspected vulnerability.

Use GitHub’s **Security → Report a vulnerability** flow:

<https://github.com/Himath2002/urban-grid-planner/security/advisories/new>

Include:

- the affected version or commit;
- a minimal grid file or reproduction;
- expected security impact;
- operating system and JDK version;
- any suggested mitigation.

Reports will be acknowledged through the private advisory. Details should remain private until a fix and coordinated disclosure are ready.

## Security posture

The application runs locally, performs no network requests, stores no credentials, and writes no application data. Its primary untrusted boundary is the explicitly supplied grid file. The loader therefore validates format, numeric ranges, exact record count, and maximum grid size before returning a domain model.
