CREATE TABLE users (
	id int8 NOT NULL,
	created_date timestamp NULL,
	updated_date timestamp NULL,
	"version" int4 NULL,
	identity_number varchar(11) NOT NULL,
	"name" varchar(255) NULL,
	phone_number varchar(255) NULL,
	salary numeric(19, 2) NOT NULL,
	surname varchar(255) NULL,
	CONSTRAINT users_pkey PRIMARY KEY (id),
	CONSTRAINT users_salary_check CHECK ((salary >= (0)::numeric))
);

CREATE TABLE application (
	id int8 NOT NULL,
	created_date timestamp NULL,
	updated_date timestamp NULL,
	"version" int4 NULL,
	application_status int4 NULL,
	credit_limit numeric(19, 2) NULL,
	user_id int8 NULL,
	CONSTRAINT application_pkey PRIMARY KEY (id)
);

ALTER TABLE application ADD CONSTRAINT application_constraint FOREIGN KEY (user_id) REFERENCES users(id);


CREATE TABLE score (
	id int8 NOT NULL,
	created_date timestamp NULL,
	updated_date timestamp NULL,
	"version" int4 NULL,
	identity_number varchar(255) NULL,
	score int8 NULL,
	CONSTRAINT score_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE user_seq
    INCREMENT 1
    START 5
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE application_seq
    INCREMENT 1
    START 5
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE score_seq
    INCREMENT 1
    START 5
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;