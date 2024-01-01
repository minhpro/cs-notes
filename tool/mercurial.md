# Install

Download source:

`wget https://www.mercurial-scm.org/release/mercurial-6.6.1.tar.gz`

Per-user installation

```sh
pip3 install docutils # build docs
tar xvzf mercurial-<ver>.tar.gz
cd mercurial-<ver>
make install-home
```

Add some path variable to your shell (`.bashrc`, or `.zshrc`)

```sh
export PYTHONPATH=${HOME}/lib/python ## ???
export PATH=${HOME}/bin:$PATH
```


System-wide installation

To install system-wide, you'll need root privileges.

`make install`
