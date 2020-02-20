create table reports
(
	report_id int auto_increment
		primary key,
	car_id int not null,
	data date not null,
	fuel_litters double(11,0) not null,
	price_per_litter double(11,0) not null
);

