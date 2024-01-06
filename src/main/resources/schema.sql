drop table IF EXISTS BOOKS;
create table IF NOT EXISTS BOOKS(
                      ID int not null AUTO_INCREMENT,
                      TITLE varchar(100) not null,
                      AUTHOR varchar(100) not null,
                      GENRE varchar(50) not null,
                      PRIMARY KEY ( ID )
);