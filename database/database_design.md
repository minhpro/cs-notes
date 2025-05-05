# Database design convention

Some design conventions (special in Postgresql):
- Primary key design
- Data types
- Foreign keys

## Primary key

By default, the normal table should have an primary key with type of integer with an automatic generated value mechanism, e.g. in Postgresql:

```sql
id INT ALWAYS GENERATED AS IDENTITY,
CONSTRAINT pk_{table_name}s PRIMARY KEY (id)
```

- Field name should be `id`: useful to make a generic CRUD operations bases on a common `id` primary key.
- `GENERATED ALWAYS AS IDENTITY`: column's value is generated automatically by an increasing mechanism. This is the standard SQL syntax.
- Using `bigint` instead of `int` with some large tables.

## Data types

- Prefer `integer` or `float` types over the `numeric` types:
    - Caculate on `numberic` types is slower.
    - Should use `numeric` types to store very large number of digits or where exactness is required.
- If you desire to store long strings with no specific upper limit, use `text` or `character varying` (varchar) without a length specifier. There is no different performance with three type: `text`, `varchar` and `char`.
- Enumerated types: they are equivalent to the enum types supported in a number of programming languages.
    - ENUM values should be in uppercase as the convention.
- JSON type: Postgresql supports both type `json` and `jsonb` to store the JSON data.
    - Prefer `jsonb`, which stores data in a decomposed binary format (no need to parse again), supports indexing, which can be a significant advantage.
    - In SQLAlchemy, json type is `sqlalchemy.dialects.postgresql.JSONB`.
- ARRAY type:
    - Prefer standard ARRAY types over custom type (comma delimiter, ...)
    - In SQLAlchemy, ARRAY type is `sqlalchemy.dialects.postgresql.ARRAY`.

## Foreign keys

The relations betweens models should be implemented in the database schema using `Foreign Key` constraints.
