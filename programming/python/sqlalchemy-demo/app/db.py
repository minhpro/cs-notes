import sqlalchemy as sa
import sqlalchemy.orm as so

from .config import setting

# A convention metadata, see more:
# - https://docs.sqlalchemy.org/en/20/core/constraints.html#constraint-naming-conventions
# Use this metadata object to construct Table objects
# , either directly with Table constructor or ORM Mapped classe
metadata_obj = sa.MetaData(naming_convention={
        "ix": "ix_%(column_0_label)s",
        "uq": "uq_%(table_name)s_%(column_0_name)s",
        "ck": "ck_%(table_name)s_%(constraint_name)s",
        "fk": "fk_%(table_name)s_%(column_0_name)s_%(referred_table_name)s",
        "pk": "pk_%(table_name)s",
    })

# Base declarative model
class Model(so.DeclarativeBase):
    metadata = metadata_obj

    def to_dict(self):
        # Abstract method
        # Convert the object to a dict
        # Subclass should implement this method
        return {}

db_url = sa.URL.create(
    drivername="postgresql+psycopg",
    username=setting.db_user,
    password=setting.db_password,
    host=setting.db_host,
    database=setting.db_name,
)

engine = sa.create_engine(db_url)
Session = so.sessionmaker(engine)
