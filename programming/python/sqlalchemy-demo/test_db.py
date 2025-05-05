import sqlalchemy as sa
from app.db import engine

stmt = sa.text("SELECT * FROM products")
print(stmt)
with engine.connect() as conn:
    result = conn.execute(stmt).all()
    for r in result:
        # print(type(r))
        print(r)