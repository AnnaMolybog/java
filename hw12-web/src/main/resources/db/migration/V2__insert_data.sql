insert into address (street_name) values ('street 1');
insert into address (street_name) values ('street 2');
insert into address (street_name) values ('street 3');
insert into address (street_name) values ('street 4');
insert into address (street_name) values ('street 5');


insert into client (client_name, address_id) values ('client 1', 1);
insert into client (client_name, address_id) values ('client 2', 2);
insert into client (client_name, address_id) values ('client 3', 3);
insert into client (client_name, address_id) values ('client 4', 4);
insert into client (client_name, address_id) values ('client 5', 5);


insert into phone (phone_number, client_id) values ('123456', 1);
insert into phone (phone_number, client_id) values ('1234567', 2);
insert into phone (phone_number, client_id) values ('12345678', 3);
insert into phone (phone_number, client_id) values ('123456789', 4);
insert into phone (phone_number, client_id) values ('1234567890', 5);

insert into users (login, password) values ('admin', 'admin');