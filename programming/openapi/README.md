## Open API docs

API docs format is [OpenAPI](https://swagger.io/specification/)

Tool to gather, generate beautiful docs: https://github.com/Redocly/redocly-cli

Tool to lint your YAML/JSON files, Ready-to-use Rulesets include OpenAPI: https://github.com/stoplightio/spectral

## Manual

Multi-file structure for Open API Specification:
- src
    - openapi.yaml
    - parameters
        - path
        - query
    - resources
    - responses
    - schemas

Install VSCode extension: Redocly to support writing Open API.

### Build

The command bundles the spec as one `.yaml` file.

```
npm run build
```

The minified document is stored in `_build/openapi.yaml`.

### Test

The command checks if the document follows the OpenAPI 3.0 Specification.

```
npm run test
```

### Preview

The command builds a docs site so that you can view the rendering on your local browser.

```
npm run preview
```

The server starts on http://127.0.0.1:8080.

The site is generated with [ReDoc](https://github.com/Redocly/redoc).

References:
- https://github.com/dgarcia360/openapi-boilerplate
