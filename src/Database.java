import models.Genre;
import models.Movie;
import org.apache.commons.lang3.text.WordUtils;

import java.sql.*;
import java.util.*;


import static models.Messages.errorMsg;

public class Database {
    private Database() {}

    private static volatile Database instance;

    private Connection connect() {
        Connection connection = null;
        Properties props = ENVManager.getENV();

        try {
            String dbName = "cinema";
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbName, props.get("USERNAME").toString(), props.get("PASSWORD").toString());
        } catch (Exception ex) {
            errorMsg(ex.getMessage());
        }
        return connection;

    }

    public static Database getInstance() {
        try {
            if (instance == null) {
                synchronized (Database.class) {
                    if (instance == null) {
                        instance = new Database();
                    }
                }
            }
        } catch (Exception ex) {

            errorMsg(ex.getMessage());

        }

        return instance;


    }

    public List<String> getMovieGenres() {
        List<String> list = new ArrayList<>();
        try (var con = connect();
             var stmt = con.prepareStatement("SELECT DISTINCT genre FROM movie_genres NATURAL JOIN genres");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next())
                list.add(rs.getString("genre"));

        } catch (SQLException e) {
            errorMsg(String.format("There was an error with fetching genres %s", e.getMessage()));
        }

        return list.stream().sorted(String.CASE_INSENSITIVE_ORDER).toList().stream().map(WordUtils::capitalizeFully).toList();

    }


    public List<Movie> getMovieList() {
        List<Movie> list = new ArrayList<>();
        try (Connection con = connect();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM view_all_movies")
        ) {

            while (rs.next()) {
                int id = rs.getInt("movie_id");
                String title = rs.getString("title");
                String genres = rs.getString("genres");
                list.add(new Movie(id, title, genres));

            }

        } catch (Exception e) {
            errorMsg(e.getMessage());

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
            errorMsg(e.getMessage());

        }
        return list;

    }


    public String getMovieName(int movieID) {

        try (var con = connect(); var stmt = con.prepareStatement("SELECT title FROM movies WHERE movie_id = ?")) {
            stmt.setInt(1, movieID);
            try (var rs = stmt.executeQuery()) {
                rs.next();
                return rs.getString("title");
            }


        } catch (Exception e) {
            errorMsg(e.getMessage());
        }
        return "error fetching movie name by movie id";

    }


    public List<String> getSelectedMovieGenres(int movieID) {
        List<String> list = new ArrayList<>();
        try (var con = connect();
             var stmt = con.prepareStatement("SELECT genre FROM fn_get_genres(?)")) {
            stmt.setInt(1, movieID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("genre"));
            }
        } catch (Exception e) {
            errorMsg(e.getMessage());
        }
        return list.stream().toList();

    }


    public boolean hasDeletedRecord(String tableName, String idField, int id) {
        try (var con = connect();
             var stmt = con.prepareStatement(String.format("DELETE FROM %s WHERE %s = ?", tableName, idField))) {
            stmt.setInt(1, id);
            return stmt.execute();
        } catch (Exception ex) {
            errorMsg(ex.getMessage());
        }
        return false;
    }


    public void addMovieGenres(int movieID, List<Integer> genreIDs) {
        try (var con = connect()) {
            for (int genreId : genreIDs) {
                var stmt = con.prepareStatement("INSERT INTO movie_genres(genre_id, movie_id) VALUES(?, ?)");
                stmt.setInt(1, genreId);
                stmt.setInt(2, movieID);
                stmt.executeUpdate();
            }

        } catch (Exception ex) {
            errorMsg(ex.getMessage());
        }
    }


    public int updateMovieTitle(String title, int movieID) {
        try (var con = connect()) {
            var stmt = con.prepareStatement("UPDATE movies SET title = ? WHERE  movie_id = ?");
            stmt.setString(1, title);
            stmt.setInt(2, movieID);
            return stmt.executeUpdate();

        } catch (Exception ex) {
            errorMsg(ex.getMessage());
        }
        return 0;
    }


    public boolean addMovieAndGenres(String title, Set<Integer> genres) {
        try (var con = connect()) {
            var stmt = con.prepareStatement("CALL pr_add_movie_and_genres(?, ?)");
            Array array = con.createArrayOf("INTEGER", new Object[]{genres.toArray(new Integer[0])});
            stmt.setString(1, title);
            stmt.setArray(2, array);
            return stmt.executeUpdate() == -1;

        } catch (Exception ex) {
            errorMsg(ex.getMessage());
        }
        return false;
    }
}



