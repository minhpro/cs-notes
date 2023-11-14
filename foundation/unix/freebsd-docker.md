# Setup docker

First, you need to install a docker-client, docker-machine and virtualbox by:

sudo pkg install docker docker-machine virtualbox-ose

After installing virtualbox, you need to load the vboxdrv kernel module by adding vboxdrv_load="YES" in /boot/loader.conf. You should also add your current user to your vboxusers group in order to use vbox:

sudo pw groupmod vboxuser -m <username>

Once you are done, reboot your workstation in order to load the virtualbox kernel modules

Using docker-machine for setting up docker because it lets me easily create Docker hosts on my computer by creating servers, installing docker on them and configuring Docker client to talk to them 

-- Create the virtual box host

docker-machine create -d virtualbox default

-- List all the hosts present:

docker-machine ls

Now you should set up your environment variables, so that your docker client can be able to use docker installed inside virtualbox:

eval "$(docker-machine env default)"

You can opt to stick that fella ^ in your .shrc, .bashrc or .zshrc

# Setup docker-compose

Install python3

pkg install python3

Install pip

pkg install py37-pip

Install docker-compose

pkg install docker-compose

You counld see python error when running docker-compose. You have to install pip package, could be paramiko

python3 -m pip install paramiko

# Virtualbox VM port forwarding

You need setup port forwarding on your virtualbox machine, inorder to connect to docker expose ports from the host machine

VBoxManage controlvm "default" natpf1 "my-service,tcp,,8080,,8080"

or if machine if poweroff

VBoxManage modifyvm "VM name" --natpf1 "my-service,tcp,,8080,,8080"

# Disable firewall

sysrc firewall_enable="YES"

sysrc firewall_type="open"

To stick with that, edit /etc/rc.conf by adding

firewall_enable="YES"

firewall_type="open"


