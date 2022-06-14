## Step 1 — Installing and Configuring Redis

```
sudo apt update
sudo apt install redis-server
```

Open the configuration file

`sudo vim /etc/redis/redis.conf`

Inside the file, find the supervised directive. This directive allows you to declare an init system to manage Redis as a service, providing you with more control over its operation. The supervised directive is set to no by default. Since you are running Ubuntu, which uses the systemd init system, change this to systemd:

```
supervised systemd
```

Restart redis server

`sudo systemctl restart redis`

Check redis status

`sudo systemctl status redis`

## Step 2 — Testing Redis

`redis-cli`

In the prompt that follows, test connectivity with the ping command:

## Step 3 — Binding to interfaces

By default, Redis is only accessible from localhost. You can configure Redis to listen to other interfaces

`sudo nano /etc/redis/redis.conf`

```
bind 127.0.0.1 ::1 192.168.59.1 10.22.30.11
```

Replace the example IPs by your IPs

Restart redis 

`sudo systemctl restart redis`

To check that this change has gone into effect, run the following netstat command:

`sudo netstat -lnp | grep redis`
