# pcur.execute('SAVEPOINT sp1')
# try:
#    pcur.execute(sql, values)
# except psycopg2.IntegrityError:
#    pcur.execute('ROLLBACK TO SAVEPOINT sp1')
#    value = ...
#    pcur.execute(sql, values)
# else:
#    pcur.execute('RELEASE SAVEPOINT sp1')

import psycopg2

# Connect to your postgres DB
conn = psycopg2.connect("dbname=forumdb user=postgres password=Abc@1234")

# Open a cursor to perform database operations
cur = conn.cursor()

# Insert a tag
cur.execute("INSERT INTO tags( tag ) VALUES( %s )", ("Erlang",));

# Define a savepoint
cur.execute("SAVEPOINT my_savepoint");

# Insert other tag
cur.execute("INSERT INTO tags( tag ) VALUES( %s )", ("Ruby",));

# Rollback to the savepoint
cur.execute("ROLLBACK TO my_savepoint");

# Execute a query
cur.execute("SELECT * FROM tags")

# Retrieve query results
records = cur.fetchall()

for r in records:
    print(r)

