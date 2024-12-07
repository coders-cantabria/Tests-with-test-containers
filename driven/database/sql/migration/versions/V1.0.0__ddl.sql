CREATE EXTENSION unaccent;

create sequence address_id_seq;
create table addresses (
  id bigint not null,
  city varchar(255),
  country_code varchar(255),
  street varchar(255),
  zip_code varchar(255),
  constraint address_pk primary key (id)
);

create table products (
  barcode varchar(255) not null,
  description varchar(255),
  image_url varchar(255),
  name varchar(255),
  purchase_price double precision not null,
  selling_price double precision not null,
  constraint products_pk primary key (barcode)
);

create table products_tags (
  product_entity_barcode varchar(255) not null,
  tags varchar(255),
  constraint products_tags_products_fk foreign key (product_entity_barcode)
    references products(barcode)
);

create table stores (
  code bigint not null,
  name varchar(255),
  constraint stores_pk primary key (code)
);

create table store_stock (
  product_barcode varchar(255) not null,
  store_code bigint not null,
  quantity bigint not null,
  constraint store_stock_pk primary key (product_barcode, store_code),
  constraint store_stock_products_fk foreign key (product_barcode)
    references products,
  constraint store_stock_stores_fk foreign key (store_code)
    references stores
);

create table suppliers (
  id bigint not null,
  iban varchar(255),
  name varchar(255),
  billing_address_id bigint,
  constraint suppliers_pk primary key (id),
  constraint suppliers_addresses_fk foreign key (billing_address_id)
    references addresses
);

create table products_suppliers (
  product_barcode varchar(255) not null,
  supplier_id bigint not null,
  constraint products_suppliers_pk primary key (product_barcode, supplier_id),
  constraint products_suppliers_products_fk foreign key (product_barcode)
    references products,
  constraint products_suppliers_suppliers_fk foreign key (supplier_id)
    references suppliers
);

create table pallets (
  id varchar(18),
  product_barcode varchar(255),
  store_code bigint,
  supplier_id bigint,
  batch_id varchar(20),
  units int,
  production_date date,
  constraint pallets_pk primary key (id)
);
