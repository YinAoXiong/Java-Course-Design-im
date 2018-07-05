package server;

import java.sql.*;

class Database {
    private static  String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static  String DATABASE = "java";
    private static  String DB_URL = "jdbc:mysql://localhost:3306/";
    private static  String USER = "root";
    private static  String PASSWORD = "123456";
    private static Connection connection = null;
    private static Statement statement = null;

    Database(String database, String user, String password){
        DATABASE=database;
        USER=user;
        PASSWORD=password;
        DB_URL+=DATABASE+"?autoReconnect=true&useSSL=false";
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            statement = connection.createStatement();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
    * @description: 进行登录验证操作
    * @param: [userID, password]
    * @return: java.lang.String
    * @author: 尹傲雄 yinaoxiong@gmail.com
    * @date: 2018/7/4
    */
    String login(int userID, String password){
        String sql = "SELECT nickName FROM user WHERE uid = "+userID+" AND password = "+password;
        try{
            ResultSet resultSet=statement.executeQuery(sql);
            resultSet.next();
            return resultSet.getString("nickName");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return "";
    }

    int registered(String nickName, String password)
    {
        String sql="INSERT INTO user(nickName,password) values('"+nickName+"',"+password+")";
        try {
            statement.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet=statement.getGeneratedKeys();
            if(resultSet!=null&&resultSet.next()){
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
