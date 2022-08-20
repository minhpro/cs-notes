Prepare database

`psql -U postgres template1 < 00-forum-database.sql`

Generate data

`psql -U postgres -d forumdb < 01-random-data.sql`