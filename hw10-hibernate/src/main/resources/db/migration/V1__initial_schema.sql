-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- -- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
-- create sequence hibernate_sequence start with 1 increment by 1;

create table address
(
    id   bigserial not null primary key,
    street_name varchar(50)
);

create table client
(
    id   bigserial not null primary key,
    client_name varchar(50),
    address_id bigint null references address
);

create table phone
(
    id   bigserial not null primary key,
    phone_number varchar(50),
    client_id bigint references client
);


