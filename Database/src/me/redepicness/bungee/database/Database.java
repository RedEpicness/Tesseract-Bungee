package me.redepicness.bungee.database;

import java.sql.*;

public class Database {

    private static Connection connection = null;

    public static void init(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/BattleRealms?autoReconnect=true", "root", "cocksteelers");
        } catch (SQLException ex) {
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

    public static Object getProperty(String username, String property){
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT ? FROM 'PlayerData' WHERE 'Username' = ?");
            statement.setString(1, property);
            statement.setString(2, username);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getObject(property);
        }
        catch (SQLException e){
            throw new RuntimeException("Could not obtain "+property+" for "+username+"!", e);
        }
    }

}
