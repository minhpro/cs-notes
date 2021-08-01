SSH login without password

1. Generate your ssh keys on your machine

`ssh-keygen -t rsa -b 4096 -c 'comment on this key'`

2. Use ssh to create a directory ~/.ssh on the remote machine

`ssh user@remote-machine mkdir -p .ssh`

3. Finally append the public key (created above) to file `~/.ssh/authorized_keys` on the remote machine

`cat ~/.ssh/id_rsa.pub | ssh user@remote-machine 'cat >> .ssh/authorized_keys'`

Now you can ssh login to the remote machine without enter password

`ssh user@remote-machine`

If you cannot login, you may check that:

* Change the permission of .ssh to 700 `chmod 700 .ssh`
