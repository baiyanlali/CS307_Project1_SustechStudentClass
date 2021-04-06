import java.sql.*;
import java.util.HashMap;
import java.util.List;

public class DatabaseConnnect {
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

    static void SendToDataBase(Student student) {
        if (connection != null) {
            String sql = "insert into Student (name,gender,college,student_id) values (?,?,?,?)";
            String sql2 = "insert into CourseDone (student_id,course_id) values (?,?)";
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
                    preparedStatement.setString(2, course.trim());
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
                    "courseHour,courseDept,courseCredit) values(?,?,?,?,?,?)";
            try {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, course.course_id);
                preparedStatement.setInt(2, course.total_capacity);
                preparedStatement.setString(3, course.course_name);
                preparedStatement.setInt(4, course.course_hour);
                preparedStatement.setString(5, course.course_departure);
                preparedStatement.setFloat(6, course.course_credit);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {

            }
        }
    }

    static void SendToDataBase(Teacher teacher) {
        if (connection != null) {
            String sql = "insert into teachers (name) values(?)";
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

    static HashMap<Teacher, Integer> teacherID;

    static void getTeacherID() {
        teacherID=new HashMap<>();
        String sql = "select teacherId,name from teachers";
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                Teacher teacher = JwxtParser.teacherHashMap.get(name);
                teacherID.put(teacher,id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void SendToDataBase(Class class_with_teacher, int in) {
        getTeacherID();
        String sql = "insert into teacher_linker(classId,teacherId) values(?,?))";
        List<Teacher> teachers = class_with_teacher.teachers;
        if (teachers.size() == 0) return;
        for (Teacher one : teachers) {
            int id = teacherID.get(one);
            try{
                preparedStatement=connection.prepareStatement(sql);
                preparedStatement.setString(1,class_with_teacher.class_id);
                preparedStatement.setInt(2,id);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    static void SendToDataBase(Class class1) {
        if (connection != null) {
            String sql = "insert into class(classId,className) values(?,?))";
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
            String sql = "insert into class_list(classId,weekList,location,startTime,endTime,weekday) values(?,?,?,?,?,?))";
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
            String sql = "insert into cc_linker(courseId,classId) values(?,?))";
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
//            String sql="insert into CourseDone(student_id,course_id values(?,?))";
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

