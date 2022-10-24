CREATE TABLE post(
id serial PRIMARY KEY,
name varchar(255),
text text,
link varchar(255) NOT NULL UNIQUE,
created date
);