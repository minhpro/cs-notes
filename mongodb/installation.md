## Install mongodb on Ubuntu

https://www.mongodb.com/docs/v4.4/tutorial/install-mongodb-on-ubuntu/

Import the public key used by the package management system

`wget -qO - https://www.mongodb.org/static/pgp/server-4.4.asc | sudo apt-key add -`

Create a list file for MongoDB

`echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu focal/mongodb-org/4.4 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-4.4.list`

Reload local package database

`sudo apt-get update`

Install the MongoDB packages

`sudo apt-get install -y mongodb-org=4.4.14 mongodb-org-server=4.4.14 mongodb-org-shell=4.4.14 mongodb-org-mongos=4.4.14 mongodb-org-tools=4.4.14`


## Run mongodb

**ulimit Considerations**

UNIX ulimit Settings

Starting in MongoDB 4.4, a startup error is generated if the ulimit value for number of open files is under 64000.

You can use the ulimit command at the system prompt to check system limits

`ulimit -a`

Increase number of files can be opened

`ulimit -n 65536`

**Directories**

If you installed via the package manager, the data directory /var/lib/mongodb and the log directory /var/log/mongodb are created during the installation.

By default, MongoDB runs using the mongodb user account. If you change the user that runs the MongoDB process, you must also modify the permission to the data and log directories to give this user access to these directories.

**Configuration File**

The official MongoDB package includes a configuration file (/etc/mongod.conf).

**Init System**

To run and manage your mongod process, you will be using your operating system's built-in init system.

If you are unsure which init system your platform uses, run the following command:

`ps --no-headers -o comm 1`

**Start MongoDB**

`sudo systemctl daemon-reload`

`sudo systemctl start mongod`

`sudo systemctl enable mongod`

## Network binding interfaces

By default, mongod bind to only localhost interface. To listen on other, add addresses to the configuration file

`sudo nano /etc/mongod.conf`

```
net:
  port: 27017
  bindIp: 127.0.0.1,10.22.30.11,192.168.59.1
```

Restart mongod service

`sudo systemctl restart mongod`
