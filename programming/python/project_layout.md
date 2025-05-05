---
header-includes:
  - \usepackage{enumitem}
  - \setlistdepth{20}
  - \renewlist{itemize}{itemize}{10}
  - \renewlist{enumerate}{enumerate}{10}
  - \setlist[itemize]{label=$\cdot$}
  - \setlist[itemize,1]{label=\textbullet}
  - \setlist[itemize,2]{label=--}
  - \setlist[itemize,3]{label=*}
output:
  rmarkdown::pdf_document:
      keep_tex: yes
---

# Project Layout

Project layout in summary:

- **app**
    - **api**
        - routers
        - deps
    - **models**
    - **schemas**
    - **queries**
    - **functions**
    - **main.py**
- **db**
    - **migrations**
    - **schema.sql**
- **pyproject.toml**

Project layout in detail:

- **app**: application package contains all modules to build an application.
    - **api**: contains api construction modules.
        - *routers*: defining API routers (endpoints).
            - *authentication.py*: authentication APIs.
            - *user.py*: user related APIs.
            - *...others*.
            - These APIs have `functions` and `schemas` packages as directly dependencies to build API interfaces and logics.
        - *api.py*: declaring a router that includes all above routers.
        - *deps*: containing API's dependency modules.
            - *common_parameters.py*: common parameters dependencies
            - *...other dependencies*
            - see more about [Dependencies](https://fastapi.tiangolo.com/tutorial/dependencies/).
    - **core**: containing some core functions.
        - *config.py*: reading and interpreting application parameters from environment variables and file based environment, and store them in a **Settings** data.
        - *security.py*: defining security functions, such as: token generation, token verify, password related functions, and so on.
        - *logging.py*: logging functions.
        - *...other modules*.
    - **db**: containing connection's construction code (SQLAlchemy engine, Session), and base model classes.
        - *session.py*: database session module - making an SQLAlchemy Engine and Session to work with the database.
        - *base.py*: declaring base model classes.
        - *...other modules*.
    - **crud**: crud modules located here.
        - *base.py*: Generic models and CRUD operations.
        - *crud_user.py*: User CRUD operations.
        - *...other CRUDs*.
    - **models**: this package contains database Models ([SQLAlchemy table models](https://docs.sqlalchemy.org/en/20/tutorial/metadata.html)).
    - **schemas**: containing [Pydantic](https://github.com/pydantic/pydantic) models: request bodies, response bodies, ...etc.
    - **queries**: this package defines queries to interact with the database.
    - **functions**: application functions (features) are implemented here.
        - This package should define all features of your application.
        - Functions can be separated into many modules.
        - Each function (feature) will compose some code from `db`, `crud`, `models`, `schemas`, `queries` packages, and additional logic to complete a business logic task. 
    - **main.py**: constructing a FastAPI application, include api endpoint, and adding some middlewares.
- **db**: contains database migration scripts, data, ...etc.
    - **migrations**: database migration scripts.
    - **data**: test data (csv, raw, ...).
    - **scripts**: scripts to load test data or something else.
    - **schema.sql**: The database schema.
    - Using [dbmate](https://github.com/amacneil/dbmate) as the migration tool.
- **tests**: Test code here.
- **docs**: Project documentations.
- **.env**: Defining local (test) environment variables.
- **pyproject.toml**: Project management file, using [Poetry](https://github.com/python-poetry/poetry).
- **README.md**: Project readme file: project overview and some basic information, for detail see the `docs` folder.
- **run.py**: That file starts a server with our application (FastAPI), such as [uvicorn](https://github.com/encode/uvicorn).
- **Dockerfile**: Dockerfile to build the docker image for the application.
- **docker-compose.yaml**: docker-compose file to quick start a testing environment.
