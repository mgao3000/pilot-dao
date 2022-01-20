# Pilot V3  Hibernate - V2
The purpose of this sample project (V4) is to show how to handle one-to-many 
and many-to-many relationship using Hibernate API. The sample 
code is still against the three dummy tables (MAJOR, STUDENT, PROJECT) 
The relationship between MAJOR and STUDENT is ONE to MANY and the 
relationship between STUDENT and PROJECT is MANY to MANY. 

## About the HIBERNATE implementation in this version 
In this Version, the sample code of the Pilot project shows the best practice promoted by 
Hibernate on handling one-to-many and many-to-many relationships. __No Cascade is configured
and the FetchType is always LAZY. Whenever there are requirements for retrieving relational 
data, HQL with "left join fetch" are used.__ 

## Hibernate learning tips: 
### 1. Hibernate relationship mapping can be unidirectional and bidirectional mappings. Please be aware the difference between them. 
### 2. Why FetchType.EAGER is not recommended. 
### 3. The association has two sides, the owning side and the inverse side. Please try to understand the difference. 
### 4. Hibernate interview question: what is the difference between session.get() and session.load(). 

### Here are the DDL used to define the three tables:
```SQL DDL
DROP TABLE IF EXISTS major CASCADE;
DROP TABLE IF EXISTS student CASCADE;
DROP TABLE IF EXISTS project CASCADE;

CREATE TABLE major (
    /*id                INTEGER NOT NULL default nextval('major_id_seq'), */
    id                BIGSERIAL NOT NULL,
    name              VARCHAR(30) not null unique,
    description       VARCHAR(150)
);

ALTER TABLE major ADD CONSTRAINT major_pk PRIMARY KEY ( id );
ALTER TABLE MAJOR ADD CONSTRAINT UQ_major_name UNIQUE(name);

CREATE TABLE student (
    /*id              INTEGER NOT NULL default nextval('student_id_seq'),*/
    id              BIGSERIAL NOT NULL,
    login_name            VARCHAR(30) not null unique,
    password        VARCHAR(64),
    first_name      VARCHAR(30),
    last_name       VARCHAR(30),
    email           VARCHAR(50),
    address         VARCHAR(150),
    enrolled_date      date default CURRENT_DATE,
    major_id   bigint NOT NULL
);

ALTER TABLE student ADD CONSTRAINT student_pk PRIMARY KEY ( id );
ALTER TABLE STUDENT ADD CONSTRAINT UQ_login_name UNIQUE(login_name);
ALTER TABLE STUDENT ADD CONSTRAINT UQ_email UNIQUE(email);

CREATE TABLE project (
    /*id             INTEGER NOT NULL default nextval('project_id_seq'),*/
    id             BIGSERIAL NOT NULL,
    name   VARCHAR(30),
    description       VARCHAR(150),
    create_date    date default CURRENT_DATE
);

ALTER TABLE project ADD CONSTRAINT project_pk PRIMARY KEY ( id );
ALTER TABLE PROJECT ADD CONSTRAINT UQ_project_name UNIQUE(name);

CREATE TABLE student_project (
    student_id    BIGINT NOT NULL,
    project_id    BIGINT NOT NULL
);

ALTER TABLE student
    ADD CONSTRAINT student_major_fk FOREIGN KEY ( major_id )
        REFERENCES major ( id );

ALTER TABLE student_project
    ADD CONSTRAINT student_fk FOREIGN KEY ( student_id )
        REFERENCES student ( id );

ALTER TABLE student_project
    ADD CONSTRAINT project_fk FOREIGN KEY ( project_id )
        REFERENCES project ( id );
```

## DAO pattern 
DAO pattern is used to separate the actual implementation of data 
accessing API to DB tables from the high level business services. for 
those who are not familiar with DAO pattern, please refer to 
[Data Access Object Pattern](https://www.tutorialspoint.com/design_pattern/data_access_object_pattern.htm)

## About Unit Test code
Unit Tests are written using JUnit4 to make sure the JDBC implementation 
is correct. For those who are not familiar with JUnit, please refer 
[Most Common JUnit 4 Annotations](https://www.swtestacademy.com/junit4/)
You only need to understand the usage of the following JUnit annotations now: 
### @Test, @Before, @After, @BeforeClass and @AfterClass

