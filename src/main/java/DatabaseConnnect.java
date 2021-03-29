import java.sql.*;

public class DatabaseConnnect {
    public static void main(String[] args) {

    }

    static Connection connection;
    static Statement statement;


    DatabaseConnnect(String url, String user, String password) {
        try {
            // change to your own content to connect this database
            //String url = "jdbc:postgresql://localhost:5432/CS307_SustechStudentClass";
            //String user = "byll";
            //String password = "123456";
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection success" + connection);
        } catch (Exception e) {
            System.out.println("Connection failed");
            e.printStackTrace();
        }

    }

    static void CloseConnection() {
        try {
            statement.close();
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
                    preparedStatement.setString(2, course);
                    preparedStatement.execute();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {

            }
        }
    }

    static void SendToDataBase(Course course){
        if(connection!=null){
            String sql="insert into course(courseID,totalCapacity,courseName," +
                        "courseHour,courseDept,courseCredit) value(?,?,?,?,?,?)";
            try {
                    PreparedStatement preparedStatement=connection.prepareStatement(sql);
                    preparedStatement.setString(1,course.course_id);
                    preparedStatement.setInt(2,course.total_capacity);
                    preparedStatement.setString(3,course.course_name);
                    preparedStatement.setInt(4,course.course_hour);
                    preparedStatement.setString(5,course.course_departure);
                    preparedStatement.setFloat(6,course.course_credit);

                    preparedStatement.execute();
            }catch (SQLException e){
                    e.printStackTrace();
            }finally {

            }
        }
    }


}


