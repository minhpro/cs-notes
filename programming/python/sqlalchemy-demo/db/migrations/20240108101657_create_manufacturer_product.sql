-- migrate:up
create table manufacturers (
    id int generated always as identity primary key,
    name text not null unique
);

create table products (
    id int generated always as identity primary key,
    name text not null unique,
    manufacturer_id int not null,
    "year" int not null,
    cpu text null,
    constraint fk_products_manufacturer_id foreign key (manufacturer_id) references manufacturers (id)
);

create index products_year_idx on products ("year");

-- migrate:down
drop index products_year_idx;
drop table products;
drop table manufacturers;
