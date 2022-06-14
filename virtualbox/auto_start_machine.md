## Auto start machine at the boot time

Add below variables to the `/etc/default/virtualbox` file

`sudo nano /etc/default/virtualbox`

```
# virtualbox defaults file
VBOXAUTOSTART_DB=/etc/vbox
VBOXAUTOSTART_CONFIG=/etc/vbox/autostart.cfg
```

VBOXAUTOSTART_DB which contains an absolute path to the autostart database directory and
VBOXAUTOSTART_CONFIG which contains the location of the autostart config settings. The file should look similar to this:

Create autostart directory

`sudo mkdir /etc/vbox`

Now we need to create the /etc/vbox/autostart.cfg file and add

`sudo nano /etc/vbox/autostart.cfg`

```
# Default policy is to deny starting a VM, the other option is "allow".
default_policy = deny
# Create an entry for each user allowed to run autostart
teamg = {
    allow = true
}
```

Set permissions on directory to the vboxuser group and make sure users can write to the directory as well as sticky bit

```
sudo chgrp vboxusers /etc/vbox
sudo chmod 1775 /etc/vbox // equal two detail below commands
```

Assign the group write permissions on the autostart database directory.

`sudo chmod g+w /etc/vbox`

To shield the directory from being modified or deleted by other users except the owner or the root user, set sticky bit.

`sudo chmod +t /etc/vbox`


Add each of the users to the vboxusers group. 

`sudo usermod -a -G vboxusers USERNAME`

(replace USERNAME with the username `sudo usermod -a -G vboxusers teamg`)

## Enable Virtual Machine Autostart

As a user, you can enable autostart for individual machines. This requires that you define the path to the database directory first.

`VBoxManage setproperty autostartdbpath /etc/vbox/`

Once that is done, you can now setup the virtual machine to automatically start on system boot.

`vboxmanage modifyvm minikube --autostart-enabled on`

Now restart the vboxautostart-service to read in the changes.

`sudo systemctl restart vboxautostart-service`
