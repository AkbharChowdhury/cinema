import org.postgresql.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB {

    private static volatile DB instance;

    Connection connect() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/cinema", "admin", "password");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return connection;

    }

    public static DB getInstance() {
        try {
            if (instance == null) {
                synchronized (DB.class) {
                    if (instance == null) {
                        instance = new DB();
                    }
                }
            }
        } catch (Exception ex) {

            System.err.println(ex.getMessage());
        }

        return instance;


    }


    public void genres() {
        List<String> list = new ArrayList<>();
        try (Connection con = connect();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM genres")
        ) {


            while (rs.next()) {
                String s = rs.getString("genre");

            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
//        return ticketList;
    }


    public int getLastID(String table, String id) {
        int lastID = 0;

//        List<String> list = new ArrayList<>();
        try (Connection con = connect();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT %s FROM %s ORDER BY %s LIMIT 1".formatted(id, table, id))
        ) {

            while (rs.next()) {
                lastID = rs.getInt(id);
                System.out.println("ID " + rs.getString(id));


            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return lastID;
//        return ticketList;
    }


    //    public void addMovie(String title, String genres) {
//        try (var con = connect(); Statement stmt = con.createStatement()) {
//            stmt.execute("INSERT INTO movies(title,genres) VALUES(?, ?)");
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//
//        }
//
//    }
    public void addMovie(String title, String genres) {
//"INSERT INTO movies(title,genres) VALUES(?, ?) RETURNING movie_id")
        try (var con = connect();
             var stmt = con.prepareStatement("INSERT INTO movies(title,genres) VALUES(?, ?)")) {
            stmt.setString(1, title);
            stmt.setString(2, genres);
            stmt.executeUpdate();


        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
//        return 0;

    }
}
