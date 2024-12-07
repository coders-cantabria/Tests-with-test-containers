-- Products inserts
INSERT INTO products(purchase_price, selling_price, barcode, description, image_url, name) VALUES(5.0, 6.5, '00000000000001', 'Arrocete', 'http://image.com', 'Arroz de Valencia');
INSERT INTO products(purchase_price, selling_price, barcode, description, image_url, name) VALUES(2.2, 2.6, '00000000000002', 'Galletas', 'http://image.com', 'Galletas sin gluten');
INSERT INTO products(purchase_price, selling_price, barcode, description, image_url, name) VALUES(1.0, 1.5, '00000000000003', 'Leche', 'http://image.com', 'Leche de vaca');
INSERT INTO products(purchase_price, selling_price, barcode, description, image_url, name) VALUES(1.0, 1.6, '00000000000004', 'Yogur', 'http://image.com', 'Yougur Griego dietético');
INSERT INTO products(purchase_price, selling_price, barcode, description, image_url, name) VALUES(2.0, 2.5, '00000000000005', 'Pan sin gluten', 'http://image.com', 'Pan de molde sin gluten');

INSERT INTO products_tags(product_entity_barcode, tags) VALUES('00000000000001', 'arroz');
INSERT INTO products_tags(product_entity_barcode, tags) VALUES('00000000000002', 'reposteria');
INSERT INTO products_tags(product_entity_barcode, tags) VALUES('00000000000002', 'sin gluten');
INSERT INTO products_tags(product_entity_barcode, tags) VALUES('00000000000003', 'lacteos');
INSERT INTO products_tags(product_entity_barcode, tags) VALUES('00000000000004', 'lacteos');
INSERT INTO products_tags(product_entity_barcode, tags) VALUES('00000000000005', 'sin gluten');

INSERT INTO addresses(id, city, country_code, street, zip_code) VALUES(1, 'Valencia', 'ES', 'Calle de la Paz, 123', '46001');
INSERT INTO addresses(id, city, country_code, street, zip_code) VALUES(2, 'Valencia', 'ES', 'Carrer Andres Estelles, 34', '46002');
INSERT INTO addresses(id, city, country_code, street, zip_code) VALUES(3, 'Valencia', 'ES', 'Carrer Jaume I, 12', '46002');

INSERT INTO suppliers(billing_address_id, id, iban, name) VALUES(1, 1, 'ES6621000418401234567891', 'Arroces la Fallera');
INSERT INTO suppliers(billing_address_id, id, iban, name) VALUES(2, 2, 'ES6621000418401234567892', 'Arroces del Ebro');
INSERT INTO suppliers(billing_address_id, id, iban, name) VALUES(3, 3, 'ES6621000418401234567893', 'Lácteos y Repostería S.A.');
ALTER SEQUENCE address_id_seq RESTART WITH 4;

INSERT INTO products_suppliers(supplier_id, product_barcode) VALUES (1, '00000000000001');
INSERT INTO products_suppliers(supplier_id, product_barcode) VALUES (2, '00000000000001');
INSERT INTO products_suppliers(supplier_id, product_barcode) VALUES (3, '00000000000002');
INSERT INTO products_suppliers(supplier_id, product_barcode) VALUES (3, '00000000000003');
INSERT INTO products_suppliers(supplier_id, product_barcode) VALUES (3, '00000000000004');
INSERT INTO products_suppliers(supplier_id, product_barcode) VALUES (3, '00000000000005');

-- Stores inserts
INSERT INTO stores(code, name) VALUES (3000, 'Store 1');
INSERT INTO stores(code, name) VALUES (3001, 'Store 2');
INSERT INTO stores(code, name) VALUES (3002, 'Store 3');

INSERT INTO store_stock(quantity, store_code, product_barcode) VALUES (50, 3000, '00000000000001');
INSERT INTO store_stock(quantity, store_code, product_barcode) VALUES (60, 3000, '00000000000002');
INSERT INTO store_stock(quantity, store_code, product_barcode) VALUES (70, 3000, '00000000000003');
INSERT INTO store_stock(quantity, store_code, product_barcode) VALUES (60, 3000, '00000000000004');
INSERT INTO store_stock(quantity, store_code, product_barcode) VALUES (80, 3001, '00000000000001');
INSERT INTO store_stock(quantity, store_code, product_barcode) VALUES (25, 3001, '00000000000005');
