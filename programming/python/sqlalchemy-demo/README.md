# Overview

The SQLAlchemy SQL Toolkit and Object Relation Mapper is a comprehensive set of tools for working with databases and Python. It has several distinct areas of functionality which can be used individually or combined together. Its major components are organized into layers:

- SQLAlchemy ORM
- SQLAlchemy Core
    - Schema/Types
    - SQL Expresion Language
    - Engine
        - Connection Pool
        - Dialect
- DBAPI

**Core** contains the breadth of SQLAchemy's SQL and database integration and description services. **SQL Expression Language** provides a system of constructing SQL expresions represented by composable objects, which can be "executed" against a target database within the scope of a specific transaction, returing a result set.

The **ORM** builds upon Core to provide a means of working with a domain object model mapped to a database schema. When using the ORM, SQL statements are constructed in mostly the same way as when using Core, however the task of DML, which here refers to the persistence of business objects in a database, is automated using a pattern called **unit of work**, which translates changes in state against mutable objects into INSERT, UPDATE and DELETE constructs which are then invoked in terms of those objects.

The difference is that Core/SQL Expression language is command oriented whereas the ORM is state oriented.

Reading:
- https://docs.sqlalchemy.org/en/20/intro.html#documentation-overview

# Installation

Packages:
- sqlalchemy
- psycopg (Postgresql client driver)

```sh
poetry add sqlalchemy psycopg
```

# Establishing Connectivity - the Engine

SQLAlchemy Engine class `sqlalchemy.engine.Engine` connects a `Pool` and `Dialect` together to provide a source of database connectivity and behavior.
- `sqlalchemy.pool.Pool`: abstract class for connection pools.
- `sqlalchemy.engine.Dialect`: define the behavior of a specific database and DB-API combination. [Dialect](https://docs.sqlalchemy.org/en/20/core/internals.html#sqlalchemy.engine.Dialect)

The `Engine` object provides a factory as well as a holding space called a `connection pool` for database connections. The engine is typically a global object created just once for a particular database server.

To create an Engine object, we have provide three important facts:
- What kind of database are we communicating with (called `dialect`) ? e.g. `sqlite`, `mysql`, `postgresql`, ...
- What [DBAPI](https://peps.python.org/pep-0249/) using? This is a third party driver that SQLAlchemy uses to interact with a particular database and this driver will implement the DBAPI specification. If omitted, SQLAlchemy will use a default driver for the particular database selected.
- The URL to connect to the database:
    - Format: `dialect+driver://username:password@host:port/database`
    - See more: https://docs.sqlalchemy.org/en/20/core/engines.html#database-urls
    - https://docs.sqlalchemy.org/en/20/core/engines.html#creating-urls-programmatically

See [database.py](./app/db.py)

```python
import sqlalchemy as sa
from .config import setting

db_url = sa.URL.create(
    drivername="postgresql+psycopg",
    username=setting.db_user,
    password=setting.db_password,
    host=setting.db_host,
    database=setting.db_name,
)

engine = sa.create_engine(db_url)
Session = so.sessionmaker(engine) # to work with ORM
```

# Define Database Metadata

The central element of both SQLAlchemy Core and ORM is the SQL Expression Language which allows for fluent, composable construction of SQL queries. The foundation of these queries are `database metadata`: tables, columns.

The most foundation objects for database metadata in SQLAlchemy are: `MetaData`, `Table`, and `Column`.

Basic data-holding structure in the database which we query from is known as `Table`. In SQLAlchemy, the database "table" is represented by a Python object named `Table`.

The `Table` is constructed programmatically, either directly by using the `Table` contructor, or indirectly by using ORM Mapped classes. Whichever kind of approach is used, all `Table` objects are stored in a `Metadata` object.

```python
import sqlalchemy as sa
metadata_obj = sa.Metadata()
```

**Declare Table directly**

Once we have a `Metadata` object, we can declare some `Table` objects.

```python
import sqlalchemy as sa

user_table = Table(
    "users",
    metadata_obj,
    sa.Column("id", sa.Integer, primary_key=True),
    sa.Column("name", sa.String(30)),
    sa.Column("fullname"), sa.String
)
```

**Declare Table metadata by using ORM Mapped classes**

To declare Table with ORM Mapped classes, we will extend a `DeclarativeBase` class.

```python
import sqlalchemy as sa
import sqlalchemy.orm as so

class Model(so.DeclarativeBase):
    metadata = metadata_obj

class User(Model):
    __table_name__ = "users"

    id: so.Mapped[int] = so.mapped_column(primary_key=True)
    name: so.Mapped[str] = so.mapped_column(sa.String(30))
    fullname: so.Mapped[str] = so.mapped_column(sa.String)
```

# Work with Data

There are two primary ways to work with data:
- Using SQL Expression Language and Core API
- Data manipulation with the ORM

When work with data, there are three basic group of operations are:
- INSERT statements
- SELECT statements (querying)
- UPDATE and DELETE statements

We will show how to work with these statements in both ways: Core API and ORM style.

## INSERT statements

A INSERT statement is generated by using `sqlalchemy.insert()` function. This function generates an instance of `Insert` which represents an INSERT statement in SQL.

```python
import sqlalchemy as sa
stmt_obj = sa.insert(user_table).values(name="abc", fullname="xyz")
print(type(stmt_obj))
print(stmt_obj)
# <class 'sqlalchemy.sql.expression.Insert'>
# INSERT INTO users (name, fullname) VALUES (:name, :fullname)
```

Then, executing the Statement (using `engine.connect()` to generate a connection to the database)

```python
with engine.connect() as conn:
    result = conn.execute(stmt)
    conn.commit()
```

### INSERT ...RETURNING

```python
insert_obj = sa.insert(user_table).returning(
    user_table.id
)
print(insert_obj)
# INSERT INTO users (name, fullname)
# VALUES (:name, :fullname)
# RETURNING users.id
```

Reading:
- https://docs.sqlalchemy.org/en/20/tutorial/data_insert.html

## SELECT statements

For both Core and ORM, the `select()` function generates a `Select` construct which is used for all SELECT queries. Passed to methods like `Connection.execute()` in Core and `Session.execute()` in ORM, a SELECT statement is emitted in the current transaction and the result rows available via the returned `Result` object.

With Core API

```python
stmt = sa.select(user_table).where(user_table.c.name == "abc")
print(stmt)
# SELECT users.id, users.name, users.fullname
# FROM users
# WHERE users.name = :name_1
with engine.connect() as conn:
    result = conn.execute(stmt)
    for r in result:
        print(r)
```

With ORM

```python
stmt = sa.select(User).where(User.name == "abc")
with Session() as session:
    result = session.execture(stmt)
    for r in result:
        print(r)
```

Reading:
- https://docs.sqlalchemy.org/en/20/tutorial/data_select.html

## UPDATE and DELETE statements

The `update()` function generates a new instance of `Update` which represents an UPDATE statement in SQL.

```python
stmt = sa.update(user_table)
        .where(user_table.c.name == "abc")
        .values(fullname = "xyz")
print(stmt)
# UPDATE users SET fullname=:fullname WHERE users.name = :name_1
```

The `Update.values()` method controls the contents of the SET elements of the UPDATE statement. Parameters can be passed using column names as keyword arguments:

```python
stmt = sa.update(user_table).values(fullname="Username: " + user_table.c.name)
print(stmt)
# UPDATE users SET fullname=(:name_1 || users.name)
```

The `delete` function generates a new instance of `Delete` which represents an DELETE statement in SQL.

```python
stmt = sa.delete(user_table).where(user_table.c.name == "abc")
print(stmt)
# DELETE FROM users WHERE users.name = :name_1
```

Reading:
- https://docs.sqlalchemy.org/en/20/tutorial/data_update.html


# Data Manipulation with the ORM

While work with Core API, we use the `engine.connect()` to execute the statement, with the ORM, we use the `Session` to work with data.

## Inserting Rows with ORM

When using the ORM, the `Session` object is responsible for constructing `Insert` constructs and emitting them as INSERT statements within the ongoing transaction. The `Session` makes sure these new entries will be emitted to the database when they are needed, using a process known as a `flush`. The process used by the `Session` to persist objects is known as the [**unit of work**](https://docs.sqlalchemy.org/en/20/glossary.html#term-unit-of-work) pattern.

See an example how to add an object to the session

```python
user = User(name="abc", fullname="xyz")
with Session() as session:
    session.add(user)
    session.commit()
```

## Update ORM Objects

When using ORM, an UPDATE statement is emitted automatically  on a per-primary key basis corresponding to individual object that have changes on them.

```python
with Session() as session:
    sandy = session.excute(sa.select(User).filter(User.name == "sandy")).scalar_one()
    print(sandy) # User(id=2, name='sandy', fullname="Sandy Cheeks")
    sandy.fullname = "Sandy Squirrel"
    sandy in session.dirty # True
    sandy_fullname = session.execute(sa.select(User.fullname).where(User.id == 2)).scalar_one()
    print(sandy_fullname) # Sandy Squirrel
    # UPDATE users SET fullname=? WHERE users.id = ?
    # [...] ('Sandy Squirrel', 2)
    # SELECT users.fullname
    # FROM users
    # WHERE users.id = ?
    # [...] (2,)
```

We can see above that we requested the `Session` execute a single `select()` statement. However the SQL emitted shows that an UPDATE were emitted as well. This flush occurs automatically before we emit an SELECT - called **autoflush** behavior.

## Deleting ORM Objects

```python
with Session() as session:
    patrick = session.get(User, 3)
    session.delete(patrick)
```

# Work with raw SQL

We can use the raw SQL with SQLAlchemy by using `text` function to generate a `TextClause` instance and then be executed by `engine.connect().execute()` and return a `CursorResult`.

```python
stmt = sa.text("SELECT * FROM users")
with engine.connect() as conn:
    cursor_result = conn.execute(stmt)
    # fetching result by one of following methods
    # result = cursor_result.fetchall()
    result = cursor_result.fetchmany()
    # ...
    for r in result:
        # Iterate over the result
        # r is type of `sqlalchemy.engine.row.Row`
        # there are some useful information with Row, such as
        # r._asdict, r._mapping, t.tuple()
        print(r)
insert_stmt = sa.text("INSERT INTO users(name, fullname) VALUES(:name, :fullname)")
data = [
    {"name": "user1", "fullname": "User 1"},
    {"name": "user2", "fullname": "User 2"},
]
with engine.connect() as conn:
    for line in data:
        conn.execute(insert_stmt, **line)
```

# Reading

- https://docs.sqlalchemy.org/en/20/index.html
- https://docs.sqlalchemy.org/en/20/tutorial/data.html
- https://docs.sqlalchemy.org/en/20/core/sqlelement.html
- Python DB-API: https://peps.python.org/pep-0249/
- https://blog.miguelgrinberg.com/post/what-s-new-in-sqlalchemy-2-0
- https://github.com/miguelgrinberg/retrofun/tree/main
- Load environment variables from the `.env` file: https://github.com/theskumar/python-dotenv
- https://www.youtube.com/watch?v=Uym2DHnUEno

# Migration from SQLAchemy 1.4 to 2.0

- https://github.com/sqlalchemy/sqlalchemy/discussions/9565
- https://docs.sqlalchemy.org/en/20/changelog/migration_20.html#the-1-4-2-0-migration-path

# Debug

Show SQLALchemy log

```py
import logging

logging.basicConfig()
logging.getLogger('sqlalchemy.engine').setLevel(logging.INFO)
```