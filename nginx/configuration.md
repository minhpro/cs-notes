## Configuration File's Structure

Nginx consists of modules which are controlled by directives specified in the configuration file. Directives are didived into simple directives and block directives.

Simple directive:
   `<name> <parameters>;`

For example: 
    `proxypass https://upstreamserver.com;`

Block directive:

```
<name> {
    directive1
    directive2
    ...
}
```