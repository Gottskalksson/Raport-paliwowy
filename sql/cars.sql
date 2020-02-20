create table cars
(
	car_id int auto_increment
		primary key,
	car_number varchar(15) not null,
	mark varchar(100) not null,
	model varchar(100) not null,
	fuel_type varchar(10) not null,
	year_production year null,
	owner_id int null,
	constraint cars_ibfk_1
		foreign key (owner_id) references users (user_id)
);

create index owner_id
	on cars (owner_id);

