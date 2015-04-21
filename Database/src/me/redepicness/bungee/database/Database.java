package me.redepicness.bungee.database;

import java.sql.*;

public class Database {

    private static Connection connection = null;

    public static void init(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/battlerealms?autoReconnect=true", "root", "cocksteelers");
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
            T object = (T) resultSet.getObject(property);
            statement.close();
            resultSet.close();
            return object;
        }
        catch (SQLException e){
            throw new RuntimeException("Could not obtain "+property+" for "+username+"!", e);
        }
    }

    public static void generateNewUser(String username, String UUID){
        try{
            PreparedStatement statement = connection.prepareStatement("INSERT INTO PlayerData (UUID, Name) VALUES ('?', '?')");
            statement.setString(1, UUID);
            statement.setString(2, username);
            statement.executeUpdate();
            statement.close();
        }
        catch (SQLException e){
            throw new RuntimeException("Could not generate new User data for "+username+" with UUID "+UUID+"!", e);
        }
    }

}
