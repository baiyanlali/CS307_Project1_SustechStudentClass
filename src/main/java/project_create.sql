CREATE TABLE course(
	courseId varchar(16)  not null primary key ,
	totalCapacity smallint not null check ( totalCapacity > 0 ),
	courseName varchar(30)  not null,
	courseHour smallint not null check ( courseHour>0 ),
	courseDept varchar(20) not null,
	courseCredit smallint check((courseCredit>0) and (courseCredit<100))
);

CREATE TABLE class(
	classId varchar(46) primary key,
	className varchar(20) not null
);

CREATE TABLE class_list(
	classId varchar(46) references class(classId),
	--TODO: change this str to another table
	weekList varchar(24) not null,
	location varchar(20) not null,
	-- start time should between 1 and 10,between是左闭右开[,)[1,11)
    startTime smallint not null check ( startTime between 1 and 11),
    -- [2,12)
    endTime smallint not null check ( endTime>startTime and endTime between 2 and 12),
    weekday smallint check(weekday between 1 and 8)--[1,8)
);

CREATE TABLE cc_linker(
	courseId varchar(16) references course(courseId),
	classId varchar(46) references class(classId)
);

CREATE TABLE Teacher_linker(
	classId varchar(46) references class(classId),
	teacherId varchar(10) references Teachers(teacherId)
);


CREATE TABLE Teachers(
	teacherId numeric(10) primary key,
	--因为有可能会有一个很长的英文名:GARG NAVEEN KUMAR
	name varchar(30) not null
);

CREATE TABLE Student(
      name varchar(30) not null,
      gender varchar(2),
      college varchar(20) not null,
      student_id varchar(8) primary key

);

CREATE TABLE CourseDone(
    student_id varchar(8) references Student(student_id),
    course_id varchar(16) references course(courseId)

);