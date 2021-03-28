import java.sql.*;

public class DatabaseConnnect {
    public static void main(String[] args) {
        Connection connection=null;
        Statement statement=null;
        try{
            String url="jdbc:postgresql://localhost:5432/CS307_SustechStudentClass";
            String user="byll";
            String password="123456";
            connection= DriverManager.getConnection(url,user,password);
            System.out.println("Connection success"+connection);
            String sql="select * from t1";
            statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(sql);
            while (resultSet.next()){
                String name=resultSet.getString(1);
                System.out.println(name);
            }
        }catch (Exception e){
            System.out.println("Connection failed");
            e.printStackTrace();
        }finally {
            try{
                statement.close();
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
                try{
                    connection.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
