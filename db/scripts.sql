create table users (
id bigserial not null primary key,
name varchar(100) not null,
age integer not null
);

create table posts (
id bigserial not null primary key,
post_text varchar(150) not null,
dateTime timestamp,
user_id bigint not null references USERS(id)
);

create table followers (
id bigserial not null primary key,
user_id bigint not null references USERS(id),
follower_id bigint not null references USERS(id)
);