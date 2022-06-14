## Installation

On Mac

```
brew tap mongodb/brew
brew install mongodb-community@4.2
```

Start service

brew services start mongodb/brew/mongodb-community@4.2


## Create admin user

Start mongo shell

`mongo`

type script

```
use admin
db.createUser(
  {
    user: "myAdmin",
    pwd: "mypassword", // or cleartext password
    roles: [ { role: "userAdminAnyDatabase", db: "admin" }, "readWriteAnyDatabase" ]
  }
)
```

shutdown mongod instance

```
db.adminCommand( { shutdown: 1 } )
```

Restart mongod

brew services restart mongodb/brew/mongodb-community@4.2

Connect by mongo with user

mongo -u myAdmin -p mypassword


## Backup and restore

Restore

mongorestore -u myAdmin -p mypassword <path-to-backup>

## Common commands

Show dbs

`show dbs`

`use applog`

List collections in a db

`db.getCollectionNames()`

Get collection indexes

`db.request_log_1.getIndexes()`



