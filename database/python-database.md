# Connecting to database

Popular Python database libs like `psycopg2` (Postgresql), `mysql.connector` implement the Python Database API Specification: 
* https://peps.python.org/pep-0249
* using `connect` method to create a connection to the database

```python
import psycopg2

conn = psycopg2.connect(
    host="localhost",
    database="dbName",
    user="username"
)

import mysql.connector

conn = mysql.connector.connect(
    host="localhost",
    database="dbName",
    user="username"
)
```