create table employee (
    id serial primary key not null,
    name varchar(50),
    surname varchar(50),
    inn int,
    hiring timestamp
);

create table person (
    id serial primary key not null,
    login varchar(2000),
    password varchar(2000),
    empl_id int references employee(id)
);

insert into employee (name, surname, itx, hiring) values ('Sergei', 'Ivanov', 1234, '2022-06-07 14:11');
insert into person (login, password) values ('parsentev', '123');
insert into person (login, password) values ('ban', '123');
insert into person (login, password) values ('ivan', '123');