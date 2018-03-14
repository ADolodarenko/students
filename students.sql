DROP DATABASE IF EXISTS students;
 
CREATE DATABASE students DEFAULT CHARACTER SET 'utf8';
 
USE students;
 
create table groups
(
  group_id int unsigned not null auto_increment,
  groupName varchar(255) not null,
  curator varchar(255) not null,
  speciality varchar(255) not null,
  primary key (group_id)
) engine=InnoDB;
 
create table students
(
  student_id int unsigned not null auto_increment,
  firstName varchar(255) not null,
  surName varchar(255) not null,
  patronymic varchar(255) not null,
  dateOfBirth date not null,
  sex char(1),
  group_id int not null,
  educationYear int not null,
  primary key (student_id)
) engine=InnoDB;
 
set names 'utf8';
 
insert into groups (groupName, curator, speciality) 
values ('Первая', 'Доктор Борменталь', 'Создание собачек из человеков');
insert into groups (groupName, curator, speciality) 
values ('Вторая', 'Профессор Преображенский', 'Создание человеков из собачек');
 
insert into students (firstName, patronymic, surName, sex, dateOfBirth, group_id, educationYear)
values ('Иван', 'Сергеевич', 'Степанов', 'М', '1990-03-20', 1, 2006);
 
insert into students (firstName, patronymic, surName, sex, dateOfBirth, group_id, educationYear)
values ('Наталья', 'Андреевна', 'Чичикова', 'Ж', '1990-06-10', 1, 2006);
 
insert into students (firstName, patronymic, surName, sex, dateOfBirth, group_id, educationYear)
values ('Виктор', 'Сидорович', 'Белов', 'М', '1990-01-10', 1, 2006);
 
insert into students (firstName, patronymic, surName, sex, dateOfBirth, group_id, educationYear)
values ('Петр', 'Викторович', 'Сушкин', 'М', '1991-03-12', 2, 2006);
 
insert into students (firstName, patronymic, surName, sex, dateOfBirth, group_id, educationYear)
values ('Вероника', 'Сергеевна', 'Ковалева', 'Ж', '1991-07-19', 2, 2006);
 
insert into students (firstName, patronymic, surName, sex, dateOfBirth, group_id, educationYear)
values ('Ирина', 'Федоровна', 'Истомина', 'Ж', '1991-04-29', 2, 2006);