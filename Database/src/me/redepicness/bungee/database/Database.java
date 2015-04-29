package me.redepicness.bungee.database;

import java.sql.*;

public class Database {

    private static Connection connection = null;

    static void init(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/battlerealms?autoReconnect=true", "root", "cocksteelers");
        } catch (Exception ex) {
            throw new RuntimeException("Error connecting to database, aborting startup!", ex);
        }
    }

    private static void checkConnection(){
        try {
            if(!connection.isValid(0)){
                System.out.println("Connection check failed! Connection invalid! Trying to refresh!");
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://localhost/battlerealms?autoReconnect=true", "root", "cocksteelers");
                if (!connection.isValid(0)){
                    new RuntimeException("Connection invalid after refresh! Retrying!").printStackTrace();
                    checkConnection();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error checking database!!!", e);
        }
    }

    static void end(){
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
            checkConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM PlayerData WHERE Name=?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.isBeforeFirst()){
                throw new IllegalArgumentException(username + " could not be found in the database!");
            }
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

    public static <T> void updateProperty(String username, String propertyName, T property){
        try{
            checkConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE PlayerData SET "+propertyName+"=? WHERE Name=?");
            statement.setObject(1, property);
            statement.setString(2, username);
            statement.executeUpdate();
            statement.close();
        }
        catch (SQLException e){
            throw new RuntimeException("Could not update "+propertyName+" to "+property+" for "+username+"!", e);
        }
    }

    public static void generateNewUser(String username, String UUID){
        try{
            checkConnection();
            if(!CustomPlayer.get(username).exists()){
                throw new RuntimeException("Tried to generate new user in database, while new user is offline! Aborted due to inconsistency with the call!");
            }
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
