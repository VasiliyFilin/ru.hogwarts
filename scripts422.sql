CREATE TABLE car
(
    id    serial primary key,
    brand varchar(15),
    model varchar(15),
    price money
);

CREATE TABLE person
(
    id             serial primary key,
    name           varchar(15),
    age            int,
    driver_licence boolean
);

CREATE TABLE car_users
(
    id        serial primary key,
    car_id    integer not null references car,
    person_id integer not null references person,
    unique (car_id, person_id)
);