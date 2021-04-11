<h1 align = "center">Spring 2021 CS307 Project Report</h1>



## 1. Title:

<font size=4>Educational Administration System Inplementing and Testing </font>



## 2. Group info and Contribution

| Student Name: | Student ID: | Specific contribution content | Percentage |
| :------------ | :---------- | ----------------------------- | ---------- |
| Tan           |             |                               |            |
| Zhao          |             |                               |            |
| Jiang         |             |                               |            |



## 3. Catalog

[TOC]

## 4. Brief Introduction

The 21th century is filled with machines and data, thus we call it an era of big data and AI. Therefore, how to manage these data may be a very challenging as well as troublesome task.
With the development of science and technology, DBMS (database management system) springs
up and shows how powerful it is, helping numerous computer scientists and engineers to study
and research, in order to push up the efficiency and accuracy of dealing with data. Nowadays,
these systems are totally commercial and somehow mature to certain extent.

In this project, we want to use DBMS to implement a multi-dimension educational information database which is easy to extend and can have further evolved into an educational administration system(this part is also roughly accomplished by the time we submit our report). We import all the given data and assured the proper functioning of our database. All kinds of instructions were given for testing the performance and efficiency of our database as well.



## 5.  DB Design 

###   Task 1 Database Design

    #### <1.1> The structure of the whole table 

<img src="C:\Users\jiangli\Desktop\Table_Stucture.png" alt="Table_Stucture" title="style=&quot;zoom:33%;" style="zoom:73%;" />



#### <1.2> Data Table(except prerequisite)

Three given files are respectively provided , with many columns which have not been separated and are not total clear. In order to show the pristine organization of the data, here presents several tables with original data, data type and extra short explanation. (basic info can be easily recognized from columns' name, so only important parts are explained specifically)

##### Table: course

| Columns' Name | Data Type        | Short Explanation                               |
| ------------- | ---------------- | ----------------------------------------------- |
| courseid      | varchar(16)      | not null, primary key                           |
| totalcapacity | smallint         | not null , will do the check to assure it's> 0  |
| coursename    | varchar(30)      | not null, course name                           |
| coursehour    | smallint         | not null , will do the check to assure it's>= 0 |
| coursedept    | varchar(30)      | not null, get the department                    |
| coursecredit  | double precision | will do the check to assure it's>= 0 and<100    |
| standard_name | varchar(100)     |                                                 |
| prerequisite  | varchar(100)     |                                                 |

##### Table: class_list

| Columns' Name | Data Type    | Short Explanation                                           |
| ------------- | ------------ | ----------------------------------------------------------- |
| classid       | varchar(46)  | foreign key                                                 |
| weeklist      | weeklist(24) | not null                                                    |
| location      | text         | not null                                                    |
| starttime     | smallint     | not null , will do the check to assure it's between 1 to 11 |
| endtime       | smallint     | not null , will do the check to assure it's between 2 to 12 |
| weekday       | smallint     | will do the check to assure it's between 1 to 8             |

##### Table: class

| Columns' Name | Data Type   | Short Explanation |
| ------------- | ----------- | ----------------- |
| classId       | varchar(46) | primary key       |
| className     | varchar(20) |                   |

##### Table: teachers

| Columns' Name | Data Type   | Short Explanation |
| ------------- | ----------- | ----------------- |
| teacherId     | serial      | primary key       |
| name          | varchar(30) | not null          |

##### Table: student

| Columns' Name | Data Type   | Short Explanation |
| ------------- | ----------- | ----------------- |
| student_id    | varchar(8)  | primary key       |
| name          | varchar(30) | not null          |
| gender        | varchar(3)  | m/f/null          |
| college       | varchar(20) | not null          |

##### Table: coursedone

| Columns' Name | Data Type   | Short Explanation |
| ------------- | ----------- | ----------------- |
| student_id    | varchar(8)  | foreign key       |
| course_id     | varchar(16) |                   |



####      <1.3> Linker Table

##### Table: teacher_linker

connect table-class to table-teachers

| Columns' Name | Data Type   | Short Explanation |
| ------------- | ----------- | ----------------- |
| classid       | varchar(46) | foreign key       |
| teacherid     | int         | foreign key       |

##### Table: cc_linker

connect table-course to table-class

| Columns' Name | Data Type   | Short Explanation |
| ------------- | ----------- | ----------------- |
| courseId      | varchar(16) | foreign key       |
| classId       | varchar(30) | foreign key       |



     #### <1.4> Prerequisite Table

**This part is significant for its unique implementation**

##### Table: pre_std_name

| Columns' Name | Data Type   | Short Explanation |
| ------------- | ----------- | ----------------- |
| courseid      | varchar(16) |                   |
| standard_name | varchar(30) |                   |
| num           | integer     |                   |

##### Table: pre_encode

| Columns' Name  | Data Type | Short Explanation |
| -------------- | --------- | ----------------- |
| course_id      | text      |                   |
| encode_pattern | text      |                   |
| length         | integer   |                   |

**Explanation**:









#### <1.5> Code

```sql
CREATE TABLE course(
	courseId varchar(16)  not null primary key ,
	totalCapacity smallint not null check ( totalCapacity > 0 ),
	courseName varchar(30)  not null,
	courseHour smallint not null check ( courseHour>=0 ),
	courseDept varchar(20) not null,
	courseCredit float check((courseCredit>=0) and (courseCredit<100)),
	standard_name varchar(20),
	prerequisite varchar(100)
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


CREATE TABLE Teachers(
	teacherId serial primary key,
	--因为有可能会有一个很长的英文名:GARG NAVEEN KUMAR
	name varchar(30) not null
);

CREATE TABLE Teacher_linker(
	courseId varchar(46) references course(courseId),
	teacherId int references Teachers(teacherId)
);

CREATE TABLE Student(
      name varchar(30) not null,
      gender varchar(3),
      college varchar(20) not null,
      student_id varchar(8) primary key

);

CREATE TABLE CourseDone(
    student_id varchar(8) references Student(student_id),
    course_id varchar(16) references course(courseId)
);

create table pre_encode(
    courseid varchar(16),
    encode varchar(100),
    length integer
);

create table pre_std_name(
    courseid varchar(16),
    standard_name varchar(100),
    num integer
);
```



### Task 2 Import Data

#### <2.1> Data preprocessing

if we directly open the select_course.csv, course_info.json , They will perform in form below:

![image-20210411170902230](C:\Users\jiangli\AppData\Roaming\Typora\typora-user-images\image-20210411170902230.png)

​                                                             **select_course.csv**

![image-20210411170739647](C:\Users\jiangli\AppData\Roaming\Typora\typora-user-images\image-20210411170739647.png)

​                                                             **course_info.json**

As we can see here, it is far away from our expectation. So we turn to the Java to do some pre-processing. By the way, now our data have some error format like the mix-use of "**,**" and "**，**". We also corrected these errors in order to smoothly load the data.

##### select_course.csv-java-preprocessing:

```java

```



##### course_info.json-java-preprocessing:

```java
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
public class JwxtParser {
    public static List<CourseRAW> courses;
    static DatabaseConnnect databaseConnnect;
    static HashMap<String, Course> courseHashMap;
    static ArrayList<Class> classes;
    static HashSet<Teacher> teachers;
    static HashMap<String,Teacher> teacherHashMap;
    static ArrayList<Student> students;
    // Run as: jshell JwxtParser.java <json file>
    public static void main(String[] args) throws IOException {
        Path path = Path.of(args[0]);
        String content = Files.readString(path);
        content = content.replaceAll("）", ")");
        content = content.replaceAll("（", "(");
        Gson gson = new Gson();
        courses = gson.fromJson(content, new TypeToken<List<CourseRAW>>() {
        }.getType());
//        parseCourseRAW();
//        putJWXTinData();
//        exportPre();
        parseStudent();
        putStudentIntoData();
    }
    public static void exportPre() {
        HashSet<String> hasAdded = new HashSet<>();
        FileOutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream("src/main/java/data/Pre.csv");
            osw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            bw = new BufferedWriter(osw);
            //加上UTF-8文件的标识字符
            out.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            for (CourseRAW c : courses) {
                if (hasAdded.contains(c.courseId)) continue;
                String insert = String.format("%s,%s\n", c.courseId, c.prerequisite);
                System.out.println(insert);
                bw.append(insert);
                hasAdded.add(c.courseId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert bw != null;
                bw.close();
                osw.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void parseStudent() throws IOException {
        students = new ArrayList<>();
        File student_info = new File("src/main/java/data/select_course.csv");
        BufferedReader reader = null;
        try {
            String one_student = null;
            reader = new BufferedReader(new FileReader(student_info));
            while ((one_student = reader.readLine()) != null) {
//                System.out.println(one_student);
                String[] s_info = one_student.split(",");
                Student student = new Student(s_info);
                students.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }
    }
    public static void parseCourseRAW() {
        courseHashMap = new HashMap<>();
        classes = new ArrayList<>();
        teachers = new HashSet<>();
        teacherHashMap=new HashMap<>();
        HashSet<Location> locations = new HashSet<>();
        for (CourseRAW course_raw : courses) {
            //Course info_去重
            if (!courseHashMap.containsKey(course_raw.courseName.trim())) {
                Course co = new Course(course_raw.courseId.trim(),
                        course_raw.totalCapacity,
                        course_raw.courseName.trim(),
                        course_raw.courseHour,
                        course_raw.courseCredit,
                        course_raw.courseDept);
                co.prerequisite=course_raw.prerequisite;
                courseHashMap.put(co.course_name, co);
            }
            //Class info 不需要去重
            Class clAss = new Class(course_raw.className.trim(), courseHashMap.get(course_raw.courseName.trim()));
//            clAss.class_info_list.addAll(course_raw.classList);
            for (ClassListRAW cl : course_raw.classList) {
                ClassList classList = new ClassList(cl);
                clAss.class_info_list.add(classList);
            }
            if(course_raw.teacher!=null){

                if (course_raw.teacher.contains(",")) {
                    String[] manyTeacher = course_raw.teacher.split(",");
                    for (String oneTeacher : manyTeacher) {

                        Teacher teacher = new Teacher(oneTeacher.trim());
                        teachers.add(teacher);
                        teacherHashMap.put(teacher.names, teacher);
                        clAss.teachers.add(teacher);
                    }
                } else {
                    Teacher teacher = new Teacher(course_raw.teacher.trim());
                    clAss.teachers.add(teacher);
                    teachers.add(teacher);
                    teacherHashMap.put(teacher.names, teacher);
                }
            }
            classes.add(clAss);
        }
    }
    public static void putJWXTinData(){
        databaseConnnect = new DatabaseConnnect("jdbc:postgresql://localhost:5432/CS307_SustechStudentClass",
                "byll",
                "123456");
        for (Course c:courseHashMap.values()) {
            DatabaseConnnect.SendToDataBase(c);
        }
        for (Teacher teacher:teachers){
            DatabaseConnnect.SendToDataBase(teacher);
        }
        for (Class c:classes){
            DatabaseConnnect.SendToDataBase(c);
            DatabaseConnnect.SendToDataBase(c.course,c);
            for (ClassList classList:c.class_info_list){
                DatabaseConnnect.SendToDataBase(classList,c);
            }
            DatabaseConnnect.SendToDataBase(c,1);
        }
        DatabaseConnnect.CloseConnection();
    }
    static void putStudentIntoData() throws IOException {
        databaseConnnect = new DatabaseConnnect("jdbc:postgresql://localhost:5432/CS307_SustechStudentClass",
                "byll",
                "123456");
//        long size = students.size();
//        long now = 0;
//        long total = 0;
        DatabaseConnnect.SendToDataBase(students);
        DatabaseConnnect.SendToDataBase(students,10);
        DatabaseConnnect.CloseConnection();
//        DatabaseConnnect.writeToFile(students);
//        DatabaseConnnect.openFileWrite();
//        for (Student s : students) {
////            DatabaseConnnect.SendToDataBase(s);
//            DatabaseConnnect.openFileWrite();
//            DatabaseConnnect.writeToFile(s);
//            now++;
//            if (now / 1000 > 0) {
//                total += now;
//                now = 0;
//                System.out.println(String.format("%d/%d has done", total, size));
//            }
//        }
//        DatabaseConnnect.closeFileWrite();
//        DatabaseConnnect.CloseConnection();
    }
    class CourseRAW {
        public int totalCapacity;
        public String courseId;
        public String prerequisite;
        public String teacher;
        public ClassListRAW[] classList;
        public int courseHour;
        public float courseCredit;
        public String courseName;
        public String courseDept;
        public String className;
    }
    class ClassListRAW {
        public int[] weekList;
        public String location;
        public String classTime;
        public int weekday;
    }
}

```

In order to solve prerequisite more conveniently, we load out the prerequisite and save them into file: Pre.csv. In order to make our database as easy to expand as possible. We had to split the long string of prerequisite and separately store them into the table. We used library pandas in python to do this part.

![image-20210411173832123](C:\Users\jiangli\AppData\Roaming\Typora\typora-user-images\image-20210411173832123.png)

​                                                               **Pre.csv**

##### (extra)Pre.csv-python-preprocessing:

```python
import re
import pandas as pd
import csv
import numpy as np
def encode(raw_pre):
    """
    Parameters
    ----------
    raw_pre : TYPE
        raw_pre is a string that contain the raw infomation of pre

    Returns
    -------
    final : TYPE
        return the encoded format of pre, with ease to do logic calculation to check pre
    """
    raw_pre=re.sub(r"（","(", raw_pre)     #change to standard ()
    raw_pre=re.sub(r"）",")", raw_pre)
    discard=re.sub(r"[(][^(\(|\))]{,9}[)]","-",raw_pre)   #remove all inner () and set flag
    discard=re.sub(r"\s","",discard)            #remove space, save relational ()
    remove_all_p=noP=re.sub(r"\(|\)", "",discard)   #remove all () to split couse name
    names=re.split("或者|并且", remove_all_p)     #get names
    final=discard
    for name in names:
        final=re.sub(name, "%d", final, count=1)          #change all names to %d
    final=re.sub("或者"," or ", final)                #change to or and
    final=re.sub("并且", " and ", final)
    return final, len(names)
def get_course_name(raw_pre):
    """
    This function is used to get list of name of courses, in standard form(without any "()") 
    Parameters
    ----------
    raw_pre : Str
        DESCRIPTION.
    Returns
    -------
    coursese : List
        list of course names
    """
    raw_pre=re.sub(r"（","(", raw_pre)     #change to standard ()
    raw_pre=re.sub(r"）",")", raw_pre)
    clean_pre=re.sub(r"\(|\)|\s", "", raw_pre)     #remove all () both inner and outer or space
    courses=re.split("或者|并且", clean_pre)
    return courses
def check_satisfy(encoded, pre_list, satified_list):
    """
    THIS function check whether pre are satisfied.
    Parameters
    ----------
    encoded : TYPE
        DESCRIPTION.
    pre_list : TYPE
        DESCRIPTION.
    satified_list : TYPE
        DESCRIPTION.
    Returns
    -------
    TYPE
        Note that to get local var, locals() needed.

    """
    logic=[0 for i in range(len(pre_list))]     #initialize logical list
    
    for index in satified_list:               # flag satisfied courses index as 1
        logic[index]=1
        

    loc=locals()                               #this very tricky
    expression=f"satisfied= {encoded}"%tuple(logic)     #replace
    
    exec(expression)
    
    return loc['satisfied']
if __name__=='__main__':
    pre=open("Pre.csv", 'r',encoding='utf-8')

    with pre:
        tt=pd.read_csv(pre, names=['course','pre']) 
    pattern=[]
    lenth=[]
    courses=[]
    nums=[]
    cid=[]  
    raw_id=tt['course']
    raws=tt['pre']
    raws[raws.isnull()]=0
    for k in range(len(raws)):
        c=raw_id[k]
        r=raws[k]
        if r==0:
            pattern.append(nan)
            lenth.append(0)
            continue
        tmp_p, tmp_len=encode(r)
        pattern.append(tmp_p)
        lenth.append(tmp_len)
        tmp=get_course_name(r)
        for i in range(len(tmp)):
            cid.append(c)
            courses.append(tmp[i])
            nums.append(i)
            
    
    # pattern={'encode': pattern}
    # lenth={'len': lenth}
    
    # a=pd.DataFrame(pattern)
    # b=pd.DataFrame(lenth)
    
    # tt=tt.append(a, axis=1)
    # tt=tt.append(b,axis=1)
    code=np.array(tt['course']).tolist()
    
    df=pd.DataFrame({'course':code, 'encode':pattern,'len':lenth})
    df.to_csv('encode.csv',index=False, header=False)
    
    df2=pd.DataFrame({'id':cid, 'pre':courses, 'num':nums})
    df2.to_csv('pre_course.csv', index=False, header=False,encoding='utf_8_sig')
```

 #### <2.2> Insert data in DBMS 

First of all , we created a database "CS307_SustechStudentClass"  with user "byll" and set the password "123456". Then we wrote a java file "DatabaseConnect.java" to insert all the data from file into our database. In this file ,we firstly get connection with our database:

```java
 DatabaseConnnect(String url, String user, String password) {
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection success" + connection);
            statement = connection.createStatement();
            statement.execute("set search_path = \"Public\"");
//
//            ResultSet resultSet = statement.executeQuery("select * from course");
//
//            while (resultSet.next()) {
//                System.out.println(resultSet.getString(1));
//            }

        } catch (Exception e) {
            System.out.println("Connection failed");
            e.printStackTrace();
        }

    }
```

Then we load the data from .csv file and store them into our DB step by step. We can see below part which insert the student information into two tables as an example:

```java
    static void SendToDataBase(Student student) throws IOException {
        if (connection != null) {
            String sql = "insert into Student (name,gender,college,student_id) values (?,?,?,?)  ON CONFLICT DO NOTHING";
            String sql2 = "insert into CourseDone (student_id,course_id) values (?,?)  ON CONFLICT DO NOTHING";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, student.name);
                preparedStatement.setString(2, student.gender);
                preparedStatement.setString(3, student.college);
                preparedStatement.setString(4, student.student_id);

                preparedStatement.execute();

                for (String course : student.courses_done) {
                    preparedStatement = connection.prepareStatement(sql2);
                    preparedStatement.setString(1, student.student_id);
                    preparedStatement.setString(2, course);
                    preparedStatement.execute();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {

            }
        }
    }
```

#### <2.3> Follow-up Check

At last, we use count() command as well as the error warning in datagrib itself to ensure all the data are successfully imported into our table. 



















 ### Task 3 Compare Database And File

#### <3.1> Data and environment

All the data are remained. We never change any origin data for convenience. All the disposals are accomplished in the program. 

All of our team-members' operating system is windows-10. We chose Java and Python to organize the data.



#### <3.2> Experiment design

##### comparison-1:





##### comparison-2:





##### comparison-3：





##### comparison-4：





##### comparison-5





##### comparison-6







#### <3.3> High concurrency and transaction management

A project starts with the goal of implementing basic functionality, and as versions and features iterate, big data and high concurrency become necessary to be considered!

The essence is very simple, one is slow, one is to wait.

This two reasons are interrelated, slow causes waiting, waiting results in slow. 

Our group wrote the following .java file to test the high concurrency problem:(only the core part is shown below)

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class HCM implements Runnable {
    public static void main(String[] args) throws Exception {
        start=0;
        for(long i=0;i<100;i++) {
            HCM hcm=new HCM("jdbc:postgresql://10.17.118.214:5432/CS307_SustechStudentClass","byll","123456");
            Thread thread=new Thread(hcm);
            thread.start();
            start++;
        }
    }
    static long end;
    static long start;
    static long sTime;
    static long eTime;
    HCM(String url, String user, String password) {
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection success" + connection);
            statement = connection.createStatement();
            statement.execute("set search_path = \"Public\"");
        } catch (Exception var) {
            System.out.println("Connection failed");
            var.printStackTrace();
        }
    }

    Connection connection;
    Statement statement;
    ResultSet rst = null;
    @Override
    public void run() {
        if(start==0){
            sTime=System.nanoTime();
        }
        System.out.println("start"+this);
        if (connection != null) {
            String sql = "select c.student_id from \"CS307_SustechStudentClass\".\"Public\".coursedone c\n" +
                    "where c.course_id='CH316';";
            try {
                statement = connection.createStatement();
                rst = statement.executeQuery(sql);
                rst.close();
                statement.close();
                connection.close();
            } catch (SQLException var) {
                var.printStackTrace();
            } finally {
                end++;
                System.out.println("end"+this);
                if(end>=100){
                    eTime = System.nanoTime();
                    System.out.println("用时：" + (eTime - sTime)/1000000000+"s");
                }
            }
        }
    }
}
```

In this file, we simulate 100 users using the select function at almost the same time and record the total time. This help test the efficiency of our database. The test time is:   



We can see from the result that there still remains a lot of room for improvement. Our group member still learned a lot through analyzing this problem and did a lot of research on it. We list some solution for further study and optimizing:

1. Create unique key.
2. The infrequently queried ones are put in a table, and the frequently queried ones are put in another table.
3. Do not go through the full table query, this will be slow.
4.  use a UUID or a self-incrementing sequence by date.
5. ........

Since the ddl is close, we don't have much time to practice them one by one . So, we chose the first solution only: created the unique key to improve the performance, The second time result is shown below:



As we can see, the performance is improved. In the future, we will do more modification to manage high concurrency problems.

#### <3.4> User privileges management

User is a very significant key to the dataset with DBMS. For example, we want user worker just has the access of select privileges, that is he/she cannot change the dataset in any attempt (inserting, drop, alter attribute, etc.). Also, there should exist superusers who can do anything to the dataset without limitation. In the aspect, we call it user privileges.
In the DBMS, we can easily create ,give him some privilege and drop a user, the codes is displayed below:

```sql
CREATE USER worker PASSWORD '123456' ;  --创建用户时授权可创建数据库,并赋密码
ALTER USER worker CREATEDB; --赋权worker可创建数据库
GRANT CONNECT ON DATABASE "CS307_SustechStudentClass" TO worker; --将数据库的连接权限赋予给worker用户
```

And then we check out the result with SQL shell:

![image-20210411202027912](C:\Users\jiangli\AppData\Roaming\Typora\typora-user-images\image-20210411202027912.png)

Then we drop user student(which was created before to test) and grant all privileges to worker:

```sql
drop user student;
GRANT ALL PRIVILEGES ON DATABASE "CS307_SustechStudentClass" TO worker;
```

Then we turn to SQL Shell to check the updated result:

![image-20210411202814795](C:\Users\jiangli\AppData\Roaming\Typora\typora-user-images\image-20210411202814795.png)

We can say that it's very simple and convenient for user privilege operations in DBMS.

#### <3.5> Database index and file IO



#### <3.6> Performance comparison



## 6. Conclusion












