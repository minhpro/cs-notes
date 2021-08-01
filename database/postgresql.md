## User

Create an user

```
CREATE USER minhnt WITH ENCRYPTED PASSWORD 'abc#123';
```

Create a database

```
CREATE DATABASE blog;
```

Grant all privileges of a database to the user

```
GRANT ALL PRIVILEGES ON ALL TABLES IN blog TO minhnt;
```