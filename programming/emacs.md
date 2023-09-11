## Install emacs on MacOS

https://github.com/doomemacs/doomemacs/blob/master/docs/getting_started.org

With Homebrew

First, Doom’s dependencies:

```sh
# required dependencies
brew install git ripgrep
# optional dependencies
brew install coreutils fd
# Installs clang
xcode-select --install
```

Install emacs-mac

```sh
brew tap railwaycat/emacsmacport
brew install emacs-mac --with-modules
ln -s /usr/local/opt/emacs-mac/Emacs.app /Applications/Emacs.app
```

