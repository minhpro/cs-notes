## Install Fedora alongside with Windows 10

Select `Installation Destination`

* Storage Configuration: Custom

Then `Done` to go to manual create partitions

First, create the `root` partition:

* Mount point: `/`
* Size: any size you want to user with Fedora

Second, in the `Unknown` section, select the `EFI` partition (this is existed Windows EFI partition), set `Mount Point` to it as `/boot/efi`

Then `Done` to install Fedora. That's all.