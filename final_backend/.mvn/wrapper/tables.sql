create table user_details(
	uid varchar(64) not null primary key,
	email varchar(64) not null
);



create table user_activity(
	id int not null auto_increment primary key,
	uid varchar(64) not null,
	score int not null default 0,
	last_login datetime,
	current_login datetime default now(),
	constraint fk_user foreign key(uid) references user_details(uid)
);