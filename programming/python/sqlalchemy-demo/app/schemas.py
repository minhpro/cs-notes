from pydantic import BaseModel

class Manufacturer(BaseModel):
    id: int
    name: str

class Country(BaseModel):
    id: int
    name: str

class Product(BaseModel):
    id: int
    name: str
    year: int
    cpu: str

    manufacturer: Manufacturer
    countries: list[Country]

class ProductResponse(BaseModel):
    data: list[Product]