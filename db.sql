create database demo;
use demo;

DROP TABLE IF EXISTS t_user;
create table t_user(
id serial primary key,
user_name varchar(50) unique,
password varchar(200) not null,
name varchar(200),
register_date datetime
) ENGINE=InnoDB DEFAULT CHARSET=utf8;