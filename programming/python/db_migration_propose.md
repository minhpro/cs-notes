# Database versionsing and migration with Dbmate

Why we should apply a versioning and migration process tool?

- Keep the database schema in synchronous across multiple developers and production servers.
- Avoid some accidential errors from the manual migration processes.

[Dbmate](https://github.com/amacneil/dbmate) is a database migration tool:

- Keep the schema in sync across multiple developers and production servers.
- It is a standalone command-line application that can be with any programming languages and frameworks.

**Features**

- Support Postgresql, MySQL, SQLite, and ClickHouse.
- Uses plain SQL for writing schema migrations.
- Migrations are timestamp-versioned, to avoid version number conflicts with multiple developers.
- Migrations are run atomically inside a transaction.
- Supports creating and dropping databases (handy in development/test).
- Supports saving a schema.sql file to easily diff schema changes in git.
- Database connection URL is defined using an environment variable (DATABASE_URL by default), or specified on the command line.
- Built-in support for reading environment variables from your .env file.
- Easy to distribute, single self-contained binary.

**Installation**

See [installation guideline](https://github.com/amacneil/dbmate?tab=readme-ov-file#installation)

# How to use?

Summary:

- Creating a new migration: `dbmate new <script_description>`
- Write migration content to the generated file. 
- Applying migration script: `dbmate up`
- Rollback migration: `dbmate rollback`
- Dump schema without performing any actions: `dbmate dump`

## First, dump a database schema from the existing database

**Warning**: only do this on the develop environments.

1. Dump the schema from the existing database

> `dbmate dump`

> This command generates the schema file: `db/schema.sql`. 

2. Generating `import_existing` migration file.

> `dbmate new import_existing`

> This command generates a new migration script under the folder `db/migrations/`

3. Copy the relevant table definitions into this file.

4. Recreating a new development database

> `dbmate drop && dbmate up`

## Creating Migrations

To create a new migration, run:

> `dbmate new <migration_description>`

A new migration file are generated, like that: `db/migrations/20240112031824_{migration_description}.sql`:

```sql
-- migrate:up

-- migrate:down
```

Adding migration and rollback SQL scripts to this file, e.g.

```sql
-- migrate:up
create table users (
  id integer,
  name varchar(255),
  email varchar(255) not null
);

-- migrate:down
drop table users;
```


Performing migratation by command:

> `dbmate up`


If something wrong happens, you can rollback by:

> `dbmate rollback`


# Reading:

- https://github.com/amacneil/dbmate
- https://github.com/amacneil/dbmate/issues/96
