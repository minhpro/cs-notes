## Partial match search for text, textarea, and email fields in the record list

### Solution 1: Excute partial search for all fields

**API parameters**

```
?_fulltext=hello
```

Doesn't need anymore API parameters

**Implementation idea**

1. Get the list of all fields of the DB

2. Perform LIKE filter on text fields (text, textarea, and email address). The WHERE clause looks like:

```
WHERE (textField LIKE '%hello%') OR (textareaField LIKE '%hello%') OR (emailAddress LIKE '%hello%')
```

**Pros**
* Don't need any DB settings
* API will be simple (only one query string _fulltext will be added)

**Cos**
* User cannot specify which fields will be searched
* LIKE operator is inefficient

### Solution 2: Target fields are specified by the query parameter

**API parameters**

```
?_fulltext=hello&fulltextfield=1,2,3
```

**Implementation idea**

1. Get fieldId or field's name from the query parameter.

2. Perform LIKE filter on the above fields. The WHERE clause looks like:

```
WHERE (field1 LIKE '%hello%') OR (field2 LIKE '%hello%') OR (field3 LIKE '%hello%')
```

**Pros**
* User can specify which fields will be searched

**Cos**
* API is more complex (more parameters)
* The field that user choose may not be text field (text, textarea, email address)
* Every the query, user have to specify which fields will be searched (Usually, search fields are not changed often)
* LIKE operator is inefficient

### Solution 3: Target fields are configured in the DB schema setting

**API parameters**

```
?_fulltext=hello
```

Doesn't need anymore API parameters

**Implementation idea**

When creating (or updating) DB, allow user can mark each text field will be search-target or not. That needs to add one property to each field (is_search_target::boolean)

1. Get the list of all fields of the DB which are configured as a search-field target

2. Perform LIKE filter on that fields. The WHERE clause looks like:

```
WHERE (field1 LIKE '%hello%') OR (field2 LIKE '%hello%') OR (field3 LIKE '%hello%')
```

**Pros**
* User can setting which fields will be search-target
* API is simple

**Cos**
* Need modify the DB schema setting. Each field need store search-tart information
* LIKE operator is inefficient

### Using the full-text search feature of Postgresql

Link: https://www.postgresql.org/docs/9.6/textsearch-tables.html

Same idea as the Solution 3, but we don't use LIKE operator. We will use the full-text search feature of the Postgresql.

Each field marked as the search-target will be INDEX using a full-text search index such as: **GIN, pgroonga**. The INDEX looks like:

```
CREATE INDEX textField_idx ON table_name USING GIN (to_tsvector(textField))
```

The full-text search query looks like:

```
SELECT someField, textField
FROM table_name
WHERE to_tsvector(textField) @@ to_tsquery('search_key')
```

The **GIN** INDEX is limited to alphabet based languages. To support other languages, using **[PGroonga](https://pgroonga.github.io/overview/)** will be better choice.

This idea has great performance than LIKE operator. But we need more effort to research about the full-text search feature of Postgresql and **PGroonga**.
