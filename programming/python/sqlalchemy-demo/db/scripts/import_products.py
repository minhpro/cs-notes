import csv
import sqlalchemy as sa
from app.db import Session
from app.models import Country, Product, ProductCountry, Manufacturer


def import_products():
    with Session() as session:
        with session.begin():
            session.execute(sa.delete(ProductCountry))
            session.execute(sa.delete(Product))
            session.execute(sa.delete(Manufacturer))
            session.execute(sa.delete(Country))

    with Session() as session:
        with session.begin():
            with open('db/data/products.csv') as f:
                reader = csv.DictReader(f)
                all_manufacturers: dict[str, Manufacturer] = {}
                all_countries: dict[str, Country] = {}

                for row in reader:
                    row['year'] = int(row['year'])

                    manufacturer = row.pop('manufacturer')
                    countries = row.pop('country').split('/')
                    p = Product(**row)

                    if manufacturer not in all_manufacturers:
                        m = Manufacturer(name=manufacturer)
                        session.add(m)
                        all_manufacturers[manufacturer] = m
                    all_manufacturers[manufacturer].products.append(p)

                    for country in countries:
                        if country not in all_countries:
                            c = Country(name=country)
                            session.add(c)
                            all_countries[country] = c
                        all_countries[country].products.append(p)
