# Installation

MacOS

`brew install python@3.11`

Notes:
- (2024/01/02) Python3.12 is new and not stable, should use Python3.11

## Setup virtual environment

```sh
python -m venv .venv
```

`py` command may be correct in Windows.

# Package, environment, and project manager

Using tool `poetry`:
- https://python-poetry.org/docs/#installing-manually

Install poetry

```sh
echo "export VENV_PATH=<path-to-your-venv>" > .zprofile
python3 -m venv $VENV_PATH
$VENV_PATH/bin/pip install -U pip setuptools
$VENV_PATH/bin/pip install poetry
```

Add venv path to your PATH

```sh
export PATH="$VENV_PATH/bin:$PATH"
```
