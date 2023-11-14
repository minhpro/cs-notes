1. Download iso and check sum

2. Burn iso to usb

Use refus (choose GPT partition)

3. Boot from USB

4. Manual partition

I'll install FreeBSD alongside any linux distro with the following requirements:

* Keep the Linux GRUB and add an entry of FreeBSD to it.
* Use different SWAP partitions for Linux and BSD.
* Do not destroy Linux /boot/efi

Create at paritions

The root partition:

* Type: `freebsd-ufs`
* Size: `2G`
* Mount point: `/`
* Label: `freebsdroot`

The swap partition:

* Type: `freebsd-swap`
* Size: `4G`
* Label: `freebsdswap`

The var partition:

* Type: `freebsd-ufs`
* Size: `2G`
* Mount point: `/var`
* Label: `freebsdvar`

The tmp partition:

* Type: `freebsd-ufs`
* Size: `1G`
* Mount point: `/tmp`
* Label: `freebsdtmp`

The usr partition:

* Type: `freebsd-ufs`
* Size: `remaining`
* Mount point: `/usr`
* Label: `freebsdusr`

Choose finish then select Commit to start the installation

Once the installation is done, reboot into `Linux` (Fedora), open the `/etc/grub.d/40_custom` file

```
menuentry "FreeBSD" {
  insmod part_gpt
  insmod fat
  set root='hd0,gpt1'
  chainloader /EFI/freebsd/loader.efi
}
```

Where:
* hd0: the first disk
* gpt1: the first partition
* ESP (EFI system partition `/boot/efi`) is (hd0,gpt1)
* `/EFI/freebsd/loader.efi` is the FreeBSD loader inside the `/boot/efi` partition

Update Grub by

```
grub2-mkconfig -o /boot/grub2/grub.cfg

/etc/grub2.cfg and /etc/grub2-efi.cfg are links to /boot/grub2/grub.cfg

/boot/efi/EFI/fedora/grub.cfg is used to point to /boot/grub2/grub.cfg

ref: https://fedoraproject.org/wiki/Changes/UnifyGrubConf
```

For Debian, use `update-grub2` tool.

Reboot into FreeBSD to install anything else ...

If you want, you can also configure FreeBSD as the default entry by editing /etc/default/grub and change DEFAULT=0 to DEFAULT=FreeBSD.

**Other way (Not tested)**

Boot FreeBSD directly from UEFI by copy FreeBSD boot loader into the EFI partition and register it. Assume that `/dev/ada0p1` is your EFI partition.

```
# We mount the EFI partition on /boot/efi similarly to Linux.
mkdir /boot/efi
echo '/dev/ada0p1 /boot/efi msdosfs rw,noatime 0 0' >> /etc/fstab
mount /boot/efi
# other than /etc/fstab using mount -t msdosfs /dev/ada0p1 /boot/efi

# Install the FreeBSD UEFI loader.
mkdir /boot/efi/EFI/freebsd
cp /boot/boot1.efi /boot/efi/EFI/freebsd/bootx64.efi
```

Now let’s create an UEFI entry for this loader. Note that this is for FreeBSD’s efibootmgr, not the Linux’s one.

```
# Create the boot variable.
efibootmgr -c -l /boot/efi/EFI/freebsd/bootx64.efi -L "FreeBSD"

# Check the variable number for the new boot variable and activate it.
efibootmgr
efibootmgr -a 15

# Change the boot order to leave Debian and GRUB in charge.
efibootmgr -o 14,15
```

https://www.linuxbabe.com/command-line/how-to-use-linux-efibootmgr-examples

Reboot!

5. Add user (with default and invite to other group `wheel`)

or `pw groupmod wheel -M <username>`

Act as root by command `su`

6. Install sudo

```
pkg install sudo
su -
visudo
```

Then to give a user access to everything as root:

`userX ALL=(ALL) ALL`

To become root (as userX):

```
$ sudo -s
-or-
$ sudo -i
```

7. Install display server - Wayland

Users of `Wayland` will need to be members of the `video` group. To make it, use the `pw` command.

`pw groudmod video -m user`

Install Wayland

`pkg install wayland seatd`

All compositors using Wayland will need a runtime directory defined in the environment, which can be achieved with the following command in the bourne shell:

```sh
export XDG_RUNTIME_DIR=/var/run/user/`id -u`
```

To enable and start the seatd daemon now, and on system initialization

```
# sysrc seatd_enable=”YES”
# service seatd start
```

## Install drm-kmod (graphic drivers)

`pkg install drm-mod`

Enable drm-510-kmod through `kld_list` in `/etc/rc.conf`

* admgpu: kld_list="amdgpu"
* Intel: kld_list="i915kms"
* radeonkms: kld_list="radeonkms"

e.g. `sysrc kld_list="i915kms"`

## Setup Wayfire compositor

```
# pkg install wayfire wf-shell alacritty swaylock-effects swayidle wlogout kanshi mako wlsunset
```

```
% cp /usr/local/share/examples/wayfire/wayfire.ini ~/.config/wayfire.ini
```