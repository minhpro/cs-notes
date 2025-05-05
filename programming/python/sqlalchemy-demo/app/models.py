from typing import Optional
import sqlalchemy as sa
import sqlalchemy.orm as so
from .db import Model

# Define the relation table with sa.Table instead of defin a class that bases on Model
# The relation table should be used to build relations only
ProductCountry = sa.Table(
    'products_countries',
    Model.metadata,
    sa.Column('product_id', sa.ForeignKey('products.id'), primary_key=True, nullable=False),
    sa.Column('country_id', sa.ForeignKey('countries.id'), primary_key=True, nullable=False),
)

class Product(Model):
    __tablename__ = 'products'

    id: so.Mapped[int] = so.mapped_column(primary_key=True)
    name: so.Mapped[str] = so.mapped_column(sa.String(64), index=True, unique=True)
    manufacturer_id: so.Mapped[int] = so.mapped_column(
        sa.ForeignKey('manufacturers.id'), index=True)
    year: so.Mapped[int] = so.mapped_column(index=True)
    cpu: so.Mapped[Optional[str]] = so.mapped_column(sa.String(32))

    manufacturer: so.Mapped['Manufacturer'] = so.relationship(
        back_populates='products')
    countries: so.Mapped[list['Country']] = so.relationship(
        lazy='selectin', secondary=ProductCountry, back_populates='products'
    )

    def __repr__(self):
        return f'Product({self.id}, "{self.name}")'
    
    def to_dict(self):
        return {
            'id': self.id,
            'name': self.name,
            'year': self.year,
            'cpu': self.cpu,
            'manufacturer': self.manufacturer.to_dict(),
            'countries': [c.to_dict() for c in self.countries]
        }


class Manufacturer(Model):
    __tablename__ = 'manufacturers'

    id: so.Mapped[int] = so.mapped_column(primary_key=True)
    name: so.Mapped[str] = so.mapped_column(sa.String(64), index=True, unique=True)

    products: so.Mapped[list['Product']] = so.relationship(
        cascade='all, delete-orphan', back_populates='manufacturer')

    def __repr__(self):
        return f'Manufacturer({self.id}, "{self.name}")'
    
    def to_dict(self):
        return {
            'id': self.id,
            'name': self.name
        }
    
class Country(Model):
    __tablename__ = 'countries'

    id: so.Mapped[int] = so.mapped_column(primary_key=True)
    name: so.Mapped[str] = so.mapped_column(sa.String(32), index=True, unique=True)

    products: so.Mapped[list['Product']] = so.relationship(
        lazy='selectin', secondary=ProductCountry,
        back_populates='countries'
    )

    def __repr__(self) -> str:
        return f'Country({self.id}, "{self.name}")'
    
    def to_dict(self):
        return {
            'id': self.id,
            'name': self.name
        }
