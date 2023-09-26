sudo apt-get install virtualbox
sudo apt-get install virtualbox-ext-pack


https://www.andreafortuna.org/2019/10/24/how-to-create-a-virtualbox-vm-from-command-line/

```
VBoxManage createvm --name [MACHINE NAME] --ostype "Debian_64" --register --basefolder `pwd`

VBoxManage modifyvm [MACHINE NAME] --ioapic on                     
VBoxManage modifyvm [MACHINE NAME] --memory 1024 --vram 128       
VBoxManage modifyvm [MACHINE NAME] --nic1 nat

VBoxManage createhd --filename `pwd`/[MACHINENAME]/[MACHINE NAME]_DISK.vdi --size 80000 --format VDI                     
VBoxManage storagectl [MACHINE NAME] --name "SATA Controller" --add sata --controller IntelAhci       
VBoxManage storageattach [MACHINE NAME] --storagectl "SATA Controller" --port 0 --device 0 --type hdd --medium  `pwd`/[MACHINE NAME]/[MACHINE NAME]_DISK.vdi                
VBoxManage storagectl [MACHINE NAME] --name "IDE Controller" --add ide --controller PIIX4       
VBoxManage storageattach [MACHINE NAME] --storagectl "IDE Controller" --port 1 --device 0 --type dvddrive --medium `pwd`/debian.iso       
VBoxManage modifyvm [MACHINE NAME] --boot1 dvd --boot2 disk --boot3 none --boot4 none 


VBoxManage modifyvm [MACHINE NAME] --vrde on                  
VBoxManage modifyvm [MACHINE NAME] --vrdemulticon on --vrdeport 10001

VBoxHeadless --startvm [MACHINE NAME] 
```


Connect to vrde server by Microsoft Remote Desktop với cổng 10001 (you can change this port like 10002), user mặc định là admin, không cần password

`VBoxManage modifyvm [MACHINE NAME] --vrdemulticon on --vrdeport 10002`

Ví dụ 10.22.34.78:10001

## clone vm

VBoxManage clonevm spiral01 --name spiral02 --basefolder `pwd` --register

## Setup network brigde

Lấy interface của host machine

`ifconfig -a` 

Giả sử interface của host là `enp3s0`

`vboxmanage modifyvm spiral01 --nic1 bridged --bridgeadapter1 enp3s0`

## Assign static ip to virtual after dhcp

Lấy thông tin ip, gateway sau khi dhcp

`ifconfig -a` 

`cat /etc/resolve.conf` -- nameservers

Sửa file

`sudo nano /etc/netplan/01-netcfg.yaml`

```
network:
  version: 2
  renderer: networkd
  ethernets:
    ens3:
      dhcp4: no
      addresses:
        - 10.22.0.13/24
      gateway4: 10.22.0.1
      nameservers:
          addresses: [8.8.8.8, 1.1.1.1]
``

## Start server headless

`vboxmanage startvm spiral01 --type headless`

## Change the hostname

```
sudo vim /etc/hostname
sudo vim /etc/hosts
```

