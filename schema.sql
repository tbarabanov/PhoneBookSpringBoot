create schema if not exists phone_book collate utf8mb4_general_ci;

create table if not exists contact
(
    id int auto_increment
        primary key,
    first_name varchar(255) not null,
    last_name varchar(255) not null
);

create table if not exists phone_number
(
    id int auto_increment
        primary key,
    code varchar(255) not null,
    number varchar(255) not null,
    contact_id int null,
    constraint FKan4ytphn797jyb5734vg3j7in
        foreign key (contact_id) references contact (id)
);
