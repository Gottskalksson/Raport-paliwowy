create table users
(
	user_id int auto_increment
		primary key,
	user_name varchar(255) null,
	password varchar(255) null,
	constraint user_name
		unique (user_name)
);

