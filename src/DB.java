import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB {
    private DB() {

    }

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

    public List<String> genres() {
        List<String> list = new ArrayList<>();
        try (Connection con = connect();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM genres ORDER BY genre")
        ) {

            while (rs.next()) {
                list.add(rs.getString("genre"));
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return list;

    }


    public List<Genre> getAllGenres() {
        List<Genre> list = new ArrayList<>();
        try (Connection con = connect();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM genres ORDER BY genre")
        ) {

            while (rs.next()) {
                list.add(new Genre(rs.getInt("genre_id"), rs.getString("genre")));

            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return list;

    }


    public int getLastID(String idField, String table) {

        try (Connection con = connect();
             var stmt = con.prepareStatement("SELECT %s FROM %s order by %s desc limit 1".formatted(idField, table, idField))
        ) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) return rs.getInt(idField);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return 0;
    }


    public boolean addMovie(String title, String genres) {
        try (var con = connect();
             var stmt = con.prepareStatement("INSERT INTO movies(title,genres) VALUES(?, ?)")) {
            stmt.setString(1, title);
            stmt.setString(2, genres);
            return stmt.execute();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return false;
    }


    public void addMovieGenres(int movieID, List<Integer> genreIDs) {
        try (var con = connect()) {
            for (int genreId : genreIDs) {
                var stmt = con.prepareStatement("INSERT INTO movie_genres(genre_id, movie_id ) VALUES(?, ?)");
                stmt.setInt(1, genreId);
                stmt.setInt(2, movieID);
                stmt.executeUpdate();
            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
}
