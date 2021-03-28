import java.sql.*;

public class DatabaseConnnect {
    public static void main(String[] args) {

    }

    Connection connection;
    Statement statement;


    DatabaseConnnect(String url,String user,String password) {
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

    void CloseConnection() {
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
}


