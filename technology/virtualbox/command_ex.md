VBoxManage createvm --name spiral01 --ostype "Debian_64" --register --basefolder `pwd`

VBoxManage modifyvm spiral01 --ioapic on                     
VBoxManage modifyvm spiral01 --memory 1024 --vram 128       
<!-- VBoxManage modifyvm spiral01 --nic1 nat -->
vboxmanage modifyvm spiral01 --nic1 bridged --bridgeadapter1 enp3s0

VBoxManage createhd --filename `pwd`/spiral01/spiral01_DISK.vdi --size 80000 --format VDI                     
VBoxManage storagectl spiral01 --name "SATA Controller" --add sata --controller IntelAhci       
VBoxManage storageattach spiral01 --storagectl "SATA Controller" --port 0 --device 0 --type hdd --medium  `pwd`/spiral01/spiral01_DISK.vdi                
VBoxManage storagectl spiral01 --name "IDE Controller" --add ide --controller PIIX4       
VBoxManage storageattach spiral01 --storagectl "IDE Controller" --port 1 --device 0 --type dvddrive --medium `pwd`/ubuntu-1804.iso       
VBoxManage modifyvm spiral01 --boot1 dvd --boot2 disk --boot3 none --boot4 none 


VBoxManage modifyvm spiral01 --vrde on                  
VBoxManage modifyvm spiral01 --vrdemulticon on --vrdeport 10001

VBoxHeadless --startvm spiral01 

## clone vm

VBoxManage clonevm spiral01 --name spiral02 --basefolder `pwd` --register
