## Datetime

https://dev.mysql.com/doc/refman/8.0/en/timestamp-initialization.html

## Start mysqld

```
sudo su
mysqld --user=root &
```


I suggest using Terminal commands.

Start MySQL

sudo /usr/local/mysql/support-files/mysql.server start
Stop MySQL

sudo /usr/local/mysql/support-files/mysql.server stop
Restart MySQL

sudo /usr/local/mysql/support-files/mysql.server restart

## Stop mysql

Finally got around the autostart - Thanks to all who contributed and allowed me to see the way.

To stop the auto start I used:

sudo launchctl unload -w /Library/LaunchDaemons/com.oracle.oss.mysql.mysqld.plist

And to kill the service I used:

sudo pkill mysqld

## CTE
https://saveriomiroddi.github.io/Generating-sequences-ranges-via-mysql-8.0-ctes/


## Reset root pass

https://dev.mysql.com/doc/refman/8.0/en/resetting-permissions.html

