## Connect database by ssh tunnel

Normal way:

1. SSH to SSH Server (e.g ec2-user@35.78.141.226)

`ssh -i <path-to-key-file> ec2-user@35.78.141.226`

2. Connect to private DB from SSH Server (e.g mysql 52.4.1.150, port 3306)

`mysql -u <user> -h 52.4.1.150 -p`

Using SSH Tunnel

1. Create a tunnel

`ssh -o "IdentitiesOnly yes" -i <path-to-key-file> -L 3307:52.4.1.150:3306 ec2-user@35.78.141.226`

2. Connect to database from you machine (using local ip `127.0.0.1` not localhost)

`mysql -h 127.0.0.1 -P 3307 -u myuser -p`


References:
* https://linuxize.com/post/how-to-setup-ssh-tunneling
