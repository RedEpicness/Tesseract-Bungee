package me.redepicness.bungee.database;

import java.sql.*;

public class Database {

    private static Connection connection = null;

    public static void init(){
        try {
            System.out.println("loading class!");
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("loading connection");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/battlerealms?autoReconnect=true", "root", "cocksteelers");
            System.out.println("connection loaded!");
        } catch (Exception ex) {
            throw new RuntimeException("Error connecting to database, aborting startup!", ex);
        }
    }

    public static void end(){
        try {
            connection.close();
        } catch (SQLException ex) {
            throw new RuntimeException("Error connecting to database, aborting startup!", ex);
        }
    }

    public static Connection getConnection(){
        if(connection == null) throw new RuntimeException("Connection is null!!!");
        return connection;
    }

    public static <T> T getProperty(String username, String property){
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM PlayerData WHERE Name=?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            resultSet.first();
            return (T) resultSet.getObject(property);
        }
        catch (SQLException e){
            throw new RuntimeException("Could not obtain "+property+" for "+username+"!", e);
        }
    }

}
