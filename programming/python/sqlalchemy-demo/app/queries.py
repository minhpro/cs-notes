import sqlalchemy as sa
from .models import Product

def get_products():
    q = (
        sa.select(Product)
    )

    q = q.offset(0).limit(10)

    return q