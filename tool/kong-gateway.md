# Kong 2.x.x upgrade

## New features

### Hybrid Mode

Traditionally, Kong has always required a database to store configured entities such as routes, services, and plugins. Hybrid mode - control plane / data plane separation (CP/DP), removes the need for a database on every node.

Currently, the projectg uses Kong with `Traditional` (database) mode, not `Hybrid` mode.

### Go language support for plugins

Write plugins in Go

Currently, the porjectg's plugins are developed in Lua.

### Buffered proxying

Plugins can now request buffered reading of ther service response (as opposed to the streaming default), allowing them to modify headers based onthe contens of the body.

## Breaking changes

https://github.com/Kong/kong/blob/master/UPGRADE.md#breaking-changes-2.0

- Kong 2.0.0 does include a few breaking changes over Kong 1.x, all of them related to the removal of service mesh. (Not affect)
- The **Nginx configuration file has changed**, which means that you need to update it if you are using a custom template. Changes were made to improve stream mode support and to make the Nginx injections system more powerful so that custom templates are less of a necessity.
  - `kong_cache` shm (share memory cache) was split into two shms: `kong_core_cache` and `kong_cache`. If you are using a custom Nginx template, make sure core cache shared dictionaries are defined, including db-less mode shadow definitions. Both cache values rely on the already existent `mem_cache_size` configuration option to set their size, so when upgrading from a previous Kong version, the cache memory consumption might double if this value is not adjusted. (Don't user => Not affect)

Notes:
- The projectg use a custom nginx templates (tweak global settings): https://docs.konghq.com/gateway/latest/reference/nginx-directives/#custom-nginx-templates

 ## Suggested Upgrade Path

Upgrade from 1.0.0 - 1.5.0 to 2.0.0

Kong 2.0.0 supports a no-downtime migration model. This means that while the migration is ongoing, you will have two Kong clusters running, sharing the same database.

# Kong 3.0.x upgrade

https://github.com/Kong/kong/blob/master/UPGRADE.md#upgrade-to-30x

## Breaking changes

https://github.com/Kong/kong/blob/release/3.0.x/CHANGELOG.md#breaking-changes

Affected changes:

- We have changed the normalization rules for route.path: Kong stores the unnormalized path, but regex path always pattern matches with the normalized URI. We used to replace percent-encoding in regex path pattern to ensure different forms of URI matches. That is no longer supported. Except for reserved characters defined in [rfc3986](https://datatracker.ietf.org/doc/html/rfc3986#section-2.2), **we should write all other characters without percent-encoding**.[#9024](https://github.com/Kong/kong/pull/9024)
- Kong will no longer use an heuristic to guess whether a route.path is a regex pattern. From now 3.0 onwards, all regex paths must start with the "~" prefix, and all paths that don't start with "~" will be considered plain text. The migration process should automatically convert the regex paths when upgrading from 2.x to 3.0 [#9027](https://github.com/Kong/kong/pull/9027).

For example, in 2.x, a route path that was expressed like this:

`/status/\d+`

Would need to be rewritten in the new 3.0 format:

`~/status/\d+`

If you migrated your Kong Gateway database from 2.8.x to 3.0, the ~ prefix is automatically added to the route paths in the database.

- Plugins MUST now have a valid PRIORITY (integer) and VERSION ("x.y.z" format) field in their handler.lua file, otherwise the plugin will fail to load. [#8836](https://github.com/Kong/kong/pull/8836)


# Upgrade to the new version

Then, run migration to upgrade your database schema:

`$ kong migrations up [-c configuration_file]`

If the command is successful, and no migration ran (no output), then you only have to reload Kong:

`$ kong reload [-c configuration_file]`

