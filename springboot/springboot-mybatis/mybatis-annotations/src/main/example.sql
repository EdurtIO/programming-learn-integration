create database spring;

use spring;

drop table if exists user;
create table user (
    id       int(20) auto_increment,
    userName varchar(20),
    primary key (id)
)
    default charset 'utf8';
