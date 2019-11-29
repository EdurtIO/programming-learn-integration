create database spring;

use spring;

drop table if exists user;
create table user
(
    id       int(20) auto_increment,
    userName varchar(20),
    primary key (id)
)
    default charset 'utf8';

insert into user(userName) value ('1');
insert into user(userName) value ('2');
insert into user(userName) value ('3');
insert into user(userName) value ('4');
insert into user(userName) value ('5');
insert into user(userName) value ('6');
insert into user(userName) value ('7');
insert into user(userName) value ('8');
insert into user(userName) value ('9');
insert into user(userName) value ('10');
insert into user(userName) value ('11');
