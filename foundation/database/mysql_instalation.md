## Installation

**On Mac**

1. Download DMG file from: https://dev.mysql.com/downloads/mysql/

2. Install the DMG file

After that, mysql was installed at `/usr/local/mysql`, and an lauchdaemon create at `/Library/LaunchDaemons/com.oracle.oss.mysql.mysqld.plist` to start MySQL Server whenever MacOS start.

3. Add `mysql` client program to your `PATH`

Open `~/.zshrc` file (for zsh, or `~/.bash_profile` for bash), and then add the line

```sh
export PATH='/usr/local/mysql/bin:$PATH'
```

4. Check mysql server is running?

```sh
ps -ef | grep mysqld
```

If the server isn't start, run command

```sh
sudo launchctl load -w /Library/LaunchDaemons/com.oracle.oss.mysql.mysqld.plist
```

5. Connect to server

Connect to mysql server by `root` user

```sh
mysql -u root -p
```

Enter the root password

Show list databases:

```sh
mysql> show databases;
```
