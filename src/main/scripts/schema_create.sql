create table custom_user_role (custom_user_id int8 not null, role_id int8 not null, primary key
(custom_user_id, role_id));
create table custom_user (id  bigserial not null, date_created timestamp, last_updated timestamp,
 enabled boolean, encrypted_password varchar(255), username varchar(255), primary key (id));
create table daily_forecast (id  bigserial not null, date_created timestamp, last_updated
timestamp, summary varchar(255), time int8 not null, formatted_temperature_high_time varchar(255), formatted_temperature_low_time varchar(255), temperature_high float8 not null, temperature_high_time int8 not null, temperature_low float8 not null, temperature_low_time int8 not null, search_id int8, primary key (id));
create table hourly_forecast (id  bigserial not null, date_created timestamp, last_updated
timestamp, summary varchar(255), time int8 not null, temperature float8 not null, search_id int8, primary key (id));
create table role (id  bigserial not null, date_created timestamp, last_updated timestamp, role
varchar(255), primary key (id));
create table search (id  bigserial not null, date_created timestamp, last_updated timestamp,
address varchar(255), formatted_date_search varchar(255), user_id int8, primary key (id));
alter table custom_user_role add constraint FKh7qj6cm8a96rd71kra0vari0u foreign key (role_id)
references role;
alter table custom_user_role add constraint FKdtaxphll3nif2qrbxpgl0j6lh foreign key
(custom_user_id) references custom_user;
alter table daily_forecast add constraint FKrhxt3lmrm1agwb8cgtqiuetwg foreign key (search_id)
references search;
alter table hourly_forecast add constraint FK72ytphoknhug1919iboxwkh9n foreign key (search_id)
references search;
alter table search add constraint FKd9pxpeo2tnvxjuc3l01frk9h8 foreign key (user_id) references
custom_user;
