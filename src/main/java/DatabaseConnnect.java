import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseConnnect{
    public static void main(String[] args) {

    }

    static Connection connection;
    static Statement statement;
    static PreparedStatement preparedStatement;

    // change to your own content to connect this database
    //String url = "jdbc:postgresql://localhost:5432/CS307_SustechStudentClass";
    //String user = "byll";
    //String password = "123456";

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

    static void CloseConnection() {
        try {
            statement.close();
            System.out.println("CloseConnection Successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    static BufferedWriter bw1;
    static BufferedWriter bw2;

    static void openFileWrite() throws IOException {
        bw1=new BufferedWriter(new FileWriter("src/main/java/data/students.sql"));
        bw2=new BufferedWriter(new FileWriter("src/main/java/data/students_pre.sql"));
    }

    static void writeToFile(ArrayList<Student> ss) throws FileNotFoundException {
        File fout=new File("src/main/java/data/students.sql");
        System.out.println("Start to write students.sql");
        FileChannel fcout=new RandomAccessFile(fout,"rws").getChannel();
        ByteBuffer wbuffer=ByteBuffer.allocate(100);
        String sql1 = "insert into Student (name,gender,college,student_id) values (\'%s\',\'%s\',\'%s\',\'%s\')\n";
        String sql12 = "insert into CourseDone (student_id,course_id) values (\'%s\',\'%s\')\n";
        try{
            int i=0;
            int n=0;
            for (Student s:ss) {
                i++;
                fcout.write(
                        wbuffer.wrap(
                                String.format(sql1, s.name,s.gender,s.college,s.student_id).getBytes(StandardCharsets.UTF_8))
                                            ,fcout.size());
                if(i>=1000){
                    n+=i;
                    i=0;
                    System.out.println(String.format("%d of %d has been written",n,ss.size()));
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }

//        File fout2=new File("src/main/java/data/students.sql");
    }

    static void writeToFileS(ArrayList<Student>ss){
        try{
            Long time=System.currentTimeMillis();
            bw1=new BufferedWriter( new FileWriter("src/main/java/data/students.sql"));
            String sql1 = "insert into Student (name,gender,college,student_id) values (\'%s\',\'%s\',\'%s\',\'%s\')  ON CONFLICT DO NOTHING\n";
            long i=0;
            long size=ss.size();
            for (Student student:ss){
                bw1.append(String.format(sql1,student.name,student.gender,student.college,student.student_id));
                i++;
                if(i%1000==0){
                    System.out.println(String.format("Run %d of %d data",i,size));
                }
            }

            bw1.close();
            System.out.println(String.format("Run %d ms",System.currentTimeMillis()-time));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void writeToFile(Student student){
        try{
            if(bw1==null)
                bw1=new BufferedWriter(new FileWriter("src/main/java/data/students.sql"));
            if(bw2==null)
                bw2=new BufferedWriter(new FileWriter("src/main/java/data/students_pre.sql"));
            String sql1 = "insert into Student (name,gender,college,student_id) values (\'%s\',\'%s\',\'%s\',\'%s\')  ON CONFLICT DO NOTHING\n";
            String sql12 = "insert into CourseDone (student_id,course_id) values (\'%s\',\'%s\')  ON CONFLICT DO NOTHING\n";
            bw1.append(String.format(sql1,student.name,student.gender,student.college,student.student_id));
            for (String course : student.courses_done) {
                bw2.append(String.format(sql12,student.student_id,course));
            }

        }catch (IOException e){
            e.printStackTrace();
        }finally {
//            try{
//                bw1.close();;
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//            try{
//                bw2.close();;
//            }catch (IOException e){
//                e.printStackTrace();
//            }
        }
    }

    static void closeFileWrite(){
            try{
                if(bw1!=null)
                bw1.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            try{
                if(bw2!=null)
                bw2.close();
            }catch (IOException e){
                e.printStackTrace();
            }
    }


    static void SendToDataBase(ArrayList<Student> students) throws IOException {
        Long beginTime=System.currentTimeMillis();
        String sql = "insert into Student (name,gender,college,student_id) values (?,?,?,?)";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            long i=0;
            long size=students.size();
            for (Student student:students){
                i++;
                preparedStatement.setString(1, student.name);
                preparedStatement.setString(2, student.gender);
                preparedStatement.setString(3, student.college);
                preparedStatement.setString(4, student.student_id);
                preparedStatement.addBatch();
                if(i%1000==0){
                    preparedStatement.executeBatch();
                    connection.commit();
                    preparedStatement.clearBatch();
                    System.out.println(String.format("%d of %d has been added",i,size));
                }
            }
            connection.setAutoCommit(true);
            preparedStatement.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            System.out.println(String.format("Run %d ms.",System.currentTimeMillis()-beginTime));
        }
    }
    static void SendToDataBase(ArrayList<Student> students,int in) throws IOException {
        Long beginTime=System.currentTimeMillis();
        String sql = "insert into CourseDone (student_id,course_id) values (?,?)";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            long i=0;
            long size=students.size();
            for (Student student:students){
                i++;
                for (String str:student.courses_done) {
                    preparedStatement.setString(1,student.student_id);
                    preparedStatement.setString(2,str);
                    preparedStatement.addBatch();
                }
                if(i%1000==0){
                    preparedStatement.executeBatch();
                    connection.commit();
                    preparedStatement.clearBatch();
                    System.out.println(String.format("%d of %d has been added",i,size));
                }
            }
            connection.setAutoCommit(true);
            preparedStatement.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            System.out.println(String.format("Run %d ms.",System.currentTimeMillis()-beginTime));
        }
    }
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

    static void SendToDataBase(Course course) {
        if (connection != null) {
            String sql = "insert into course(courseID,totalCapacity,courseName," +
                    "courseHour,courseDept,courseCredit,standard_name,prerequisite) values(?,?,?,?,?,?,?,?)  ON CONFLICT DO NOTHING";
            try {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, course.course_id);
                preparedStatement.setInt(2, course.total_capacity);
                preparedStatement.setString(3, course.course_name);
                preparedStatement.setInt(4, course.course_hour);
                preparedStatement.setString(5, course.course_departure);
                preparedStatement.setFloat(6, course.course_credit);
                String str= course.course_name.replaceAll("\\(|\\)|\\s+","");
                preparedStatement.setString(7,str);
                preparedStatement.setString(8,course.prerequisite);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {

            }
        }
    }

    static void SendToDataBase(Teacher teacher) {
        if (connection != null) {
            String sql = "insert into teachers (name) values(?)  ON CONFLICT DO NOTHING";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                //preparedStatement.setInt(1, teacher.teacher_id);
                preparedStatement.setString(1, teacher.names);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {

            }
        }
    }

    static HashMap<String, Integer> teacherID;

    static void getTeacherID() {
        teacherID=new HashMap<>();
        String sql = "select teacherId,name from teachers";
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                Teacher teacher = JwxtParser.teacherHashMap.get(name);
                teacherID.put(teacher.names,id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void SendToDataBase(Class class_with_teacher, int in) {
        getTeacherID();
        String sql = "insert into teacher_linker(classId,teacherId) values(?,?)  ON CONFLICT DO NOTHING";
        List<Teacher> teachers = class_with_teacher.teachers;
        if (teachers.size() == 0) return;
        for (Teacher one : teachers) {

            int id = teacherID.get(one.names);
            try{
                preparedStatement=connection.prepareStatement(sql);
                preparedStatement.setString(1,class_with_teacher.class_id);
                preparedStatement.setInt(2,id);
                preparedStatement.execute();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    static void SendToDataBase(Class class1) {
        if (connection != null) {
            String sql = "insert into class(classId,className) values(?,?)  ON CONFLICT DO NOTHING";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, class1.class_id);
                preparedStatement.setString(2, class1.class_name);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {

            }
        }
    }

    static void SendToDataBase(ClassList classlist, Class class1) {
        if (connection != null) {
            String sql = "insert into class_list(classId,weekList,location,startTime,endTime,weekday) values(?,?,?,?,?,?)  ON CONFLICT DO NOTHING";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, class1.class_id);
                preparedStatement.setString(2, classlist.week_list);
                preparedStatement.setString(3, classlist.location);
                preparedStatement.setInt(4, classlist.start_time);
                preparedStatement.setInt(5, classlist.end_time);
                preparedStatement.setInt(6, classlist.week_day);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {

            }
        }
    }

    static void SendToDataBase(Course course, Class class1) {
        if (connection != null) {
            String sql = "insert into cc_linker(courseId,classId) values(?,?)  ON CONFLICT DO NOTHING";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, course.course_id);
                preparedStatement.setString(2, class1.class_id);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {

            }
        }
    }
//    static void SendToDataBase(Student student,Course course){
//        if(connection!=null){
//            String sql="insert into CourseDone(student_id,course_id values(?,?)";
//            try {
//                PreparedStatement preparedStatement = connection.prepareStatement(sql);
//                preparedStatement.setString(1,student.student_id);
//                preparedStatement.setString(2,course.course_id);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            } finally {
//
//            }
//        }
//    }
}

