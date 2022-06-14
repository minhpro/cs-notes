# Document Structure

An OpenAPI document MAY be made up of a single document or be divided into multiple, connected parts.

It is RECOMMENDED that the root OpenAPI document be named: openapi.json or openapi.yaml

An OpenAPI document will have these fields (objects)

* openapi (string, required): OpenAPI version, a sematic version number, e.g. 3.0.1
* info (Info Object, required): metadata about tha API
* servers ([Server Object]): an array of Server Objects
* paths (Paths Object, required): available paths and operations for the API
* components (Components Object): an element to hold various schemas for the specification
* security ([Security Requirement Object]): list of alternative security requirement objects that can be used. Only one of the security requirement objects need to be satisfied to authorize a request. Individual operations can override this definition. To make security optional, an empty security requirement ({}) can be included in the array
* tags ([Tag Object]): list of tags
* externalDocs (External Documentation Object): Additional external documentation

## Info Object

* title (string, required)
* description (string)
* termsOfService (string): must be in the format of a URL
* contact (Contact Object)
  * name
  * url
  * email
* license
  * name
  * url
* version (string): version of this API, a sematic version, e.g. 1.0.1

Example

```
title: Sample Pet Store App
description: This is a sample server for a pet store.
termsOfService: http://example.com/terms/
contact:
    name: API Support
    url: http://www.example.com/support
    email: support@example.com
license:
    name: Apache 2.0
    url: https://www.apache.org/licenses/LICENSE-2.0.html
version: 1.0.1
```

## Server Object

servers field is an array of Server Object

* url
* description

```
servers:
- url: https://development.gigantic-server.com/v1
  description: Development server
- url: https://staging.gigantic-server.com/v1
  description: Staging server
- url: https://api.gigantic-server.com/v1
  description: Production server
```

More variable

```
servers:
-   url: https://{username}.gigantic-server.com:{port}/{basePath}
    description: The production API server
    variables:
        username:
            # note! no enum here means it is an open value
            default: demo
            description: this value is assigned by the service provider, in this example `gigantic-server.com`
        port:
            enum:
                - '8443'
                - '443'
            default: '8443'
        basePath:
            # open meaning there is the opportunity to use special base paths as assigned by the provider, default is `v2`
            default: v2
```

## Components Object

