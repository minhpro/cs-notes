CREATE TABLE public.provider (
    id serial NOT NULL,
    "name" varchar(255) NOT NULL,
    description TEXT NULL,
    address TEXT NULL,
    status varchar(20) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT provider_pk PRIMARY KEY (id)
);

CREATE TABLE public.product (
    id serial NOT NULL,
    provider_id integer NOT NULL,
    "name" varchar(255) NOT NULL,
    description TEXT,
    price integer,
    mfg_date date,
    exp_date date,
    total integer NOT NULL DEFAULT 0,
    status varchar(20) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT product_pk PRIMARY KEY (id),
    CONSTRAINT product_provider_fk FOREIGN KEY (provider_id) REFERENCES public.provider(id)
);

CREATE TABLE public.customer (
    id serial NOT NULL,
    "name" varchar(255) NOT NULL,
    phone_number varchar(20) NOT NULL,
    email varchar(50),
    note TEXT,
    status varchar(20) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT customer_pk PRIMARY KEY (id)
);

CREATE TABLE public.product_order (
    id serial NOT NULL,
    customer_id integer NOT NULL,
    address TEXT,
    total integer NOT NULL,
    paid boolean NOT NULL DEFAULT FALSE,
    status varchar(50) NOT NULL DEFAULT 'PREPARING',
    CONSTRAINT product_order_pk PRIMARY KEY (id),
    CONSTRAINT product_order_customer_fk FOREIGN KEY (customer_id) REFERENCES public.customer(id)
);

COMMENT ON COLUMN public.product_order.status IS 'PREPARING, DELIVERING, DELIVERIED, CANCELED';

CREATE TABLE public.order_item (
    order_id integer NOT NULL,
    product_id integer NOT NULL,
    quantity integer NOT NULL,
    CONSTRAINT order_item_pk PRIMARY KEY (order_id, product_id),
    CONSTRAINT order_item_order_fk FOREIGN KEY (order_id) REFERENCES public.product_order(id),
    CONSTRAINT order_item_product_fk FOREIGN KEY (product_id) REFERENCES public.product(id)
);

