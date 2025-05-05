import re
from time import sleep
import psycopg2
from psycopg2 import extensions
import threading

def get_connection():
    # Connect to your postgres DB
    conn =  psycopg2.connect("dbname=forumdb user=postgres password=Abc@1234")
    conn.autocommit = False
    return conn

def get_records(cur):
    # Execute a query
    cur.execute("SELECT * FROM tags FOR UPDATE")

    # Retrieve query results
    records = cur.fetchall()

    for r in records:
        print(r)

def read_transaction():
    conn = get_connection()
    conn.set_isolation_level(extensions.ISOLATION_LEVEL_REPEATABLE_READ)
    # Open a cursor to perform database operations
    cur = conn.cursor()
    get_records(cur)
    sleep(5)
    get_records(cur)
    cur.close()
    conn.commit()
    conn.close()

def update_transaction():
    conn = get_connection()
    sleep(2)
    # Open a cursor to perform database operations
    cur = conn.cursor()
    # Insert a tag
    # cur.execute("INSERT INTO tags( tag ) VALUES( %s )", ("Erlang",))
     # Insert a tag
    cur.execute("DELETE FROM tags WHERE tag = %s", ("Erlang",))
    conn.commit()
    conn.close()

t1 = threading.Thread(target=read_transaction)
t2 = threading.Thread(target=update_transaction)

t1.start()
t2.start()

t1.join()
t2.join()

print("Finish!")