# Installation

MacOS

`brew install postgresql@16`

Init db: https://www.postgresql.org/docs/15/app-initdb.html

```
This formula has created a default database cluster with:
  initdb --locale=C -E UTF-8 /opt/homebrew/var/postgresql@16
For more details, read:
  https://www.postgresql.org/docs/16/app-initdb.html

postgresql@16 is keg-only, which means it was not symlinked into /opt/homebrew,
because this is an alternate version of another formula.

If you need to have postgresql@16 first in your PATH, run:
  echo 'export PATH="/opt/homebrew/opt/postgresql@16/bin:$PATH"' >> ~/.zshrc

For compilers to find postgresql@16 you may need to set:
  export LDFLAGS="-L/opt/homebrew/opt/postgresql@16/lib"
  export CPPFLAGS="-I/opt/homebrew/opt/postgresql@16/include"

To start postgresql@16 now and restart at login:
  brew services start postgresql@16
Or, if you don't want/need a background service you can just run:
  LC_ALL="C" /opt/homebrew/opt/postgresql@16/bin/postgres -D /opt/homebrew/var/postgresql@16
```

## User

Create an user

```
CREATE USER dev WITH ENCRYPTED PASSWORD 'abc#123';
```

Create a database

```
CREATE DATABASE blog;
```

Grant all privileges of a database to the user

Try this:

```sh
psql postgres
\c blog;
grant all on schema public to dev;
grant all privileges on database blog to dev;
```

If not working, try this:

```
GRANT CONNECT ON DATABASE blog TO minhnt;

GRANT ALL ON SCHEMA public TO minhnt;

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO minhnt;

GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO minhnt;
```

## Stop

Stop postgresql 12 on MacOS

```
cd /Library/PostgreSQL/12
sudo su postgres
bin/pg_ctl -D data stop
```

## Note

Local  postgresql: postgres / Abc@1234

Them brew install libpq trước khi gem install pg

## Backup and restore with docker

`docker exec -t your-db-container pg_dumpall -c -U postgres > dump_`date +%d-%m-%Y"_"%H_%M_%S`.sql`

`cat your_dump.sql | docker exec -i your-db-container psql -U postgres`

## Terminate session

```SQL
SELECT 
    pg_terminate_backend(pid) 
FROM 
    pg_stat_activity 
WHERE 
    -- don't kill my own connection!
    pid <> pg_backend_pid()
    -- don't kill the connections to other databases
    AND datname = 'kong112'
    ;
```