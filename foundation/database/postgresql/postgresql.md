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
GRANT CONNECT ON DATABASE blog TO minhnt;

GRANT USAGE ON SCHEMA public TO minhnt;

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