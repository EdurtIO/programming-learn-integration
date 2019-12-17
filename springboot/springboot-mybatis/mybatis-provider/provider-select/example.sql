create database spring;

use spring;

drop table if exists user;
create table user
(
    id       int(20) auto_increment,
    userName varchar(20),
    title    varchar(20),
    primary key (id)
)
    default charset 'utf8';

insert into user(userName, title)
VALUES ('1', '1'),
       ('2', '2'),
       ('3', '3'),
       ('4', '4'),
       ('5', '5'),
       ('6', '6'),
       ('7', '7'),
       ('8', '8'),
       ('9', '9');
