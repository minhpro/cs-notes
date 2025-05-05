from fastapi import APIRouter
from . import queries
from .db import Session
from .schemas import Product, ProductResponse

router = APIRouter()

@router.get("/products")
def get_products() -> ProductResponse:
    query = queries.get_products()
    with Session() as session:
        # Using scalar items
        product_rows = session.execute(query).scalars().all()
        products = [Product(**r.to_dict()) for r in product_rows]
        # Using tuple
        # product_rows = session.execute(query).all()
        # products = [Product(**r._tuple()[0].to_dict()) for r in product_rows]
        return ProductResponse(data=products)
    