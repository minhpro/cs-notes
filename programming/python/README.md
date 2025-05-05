# Python programming list

- Project tools
  - https://python-poetry.org/docs/
- Database
  - Psycopg (Postgresql Python): https://www.psycopg.org/psycopg3/docs/basic/usage.html
- XML, HTML Parser
  - https://beautiful-soup-4.readthedocs.io/en/latest/#quick-start
- Web
  - https://github.com/Neoteroi/BlackSheep
  - https://github.com/sanic-org/sanic
  - https://www.youtube.com/watch?v=_2nF4e7FN9Y&list=PL3kg5TcOuFlp9x-MJO-sN9ucMUdjrDSOL
- Async
  - https://github.com/magicstack/uvloop
  - https://github.com/python-trio/trio

# Tool

- pipx: https://github.com/pypa/pipx
- poetry: https://github.com/python-poetry/poetry
- Linter: Ruff, Pyprc

## Poetry

Install

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

# Documenting Python Code

https://realpython.com/documenting-python-code/

https://github.com/google/styleguide/blob/gh-pages/pyguide.md#google-python-style-guide

# Courses

- https://cs61a.org/denero.html

# Typing

- https://docs.python.org/3.11/library/typing.html