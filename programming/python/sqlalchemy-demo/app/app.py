# from sqlalchemy import text
from fastapi import FastAPI
# import uvicorn

# from .db import engine, Session
# from .queries import get_products
from .routers import router

app = FastAPI()
app.include_router(router)

# if __name__ == "__main__":
    # with engine.connect() as conn:
    #     result = conn.execute(text("select 'Hello, World!'"))
    #     print(result.all())

    # with Session() as session:
    #     query = get_products()
    #     print(query)
    #     print(type(query))
    #     results = session.execute(query).all()
    #     for r in results:
    #         print(r)
        

