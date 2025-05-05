-- migrate:up
create table countries (
    id int generated always as identity primary key,
    name text not null unique
);

create table products_countries (
    product_id int not null,
    country_id int not null,
    constraint pk_products_countries primary key (product_id, country_id),
    constraint fk_products_countries_product foreign key (product_id)
        references products (id),
    constraint fk_products_countries_country foreign key (country_id)
        references countries (id) 
);

-- migrate:down
drop table products_countries;
drop table countries;
