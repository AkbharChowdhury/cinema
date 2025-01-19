import models.Genre;
import models.Movie;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class DB {
    private DB() {

    }

    private String param(String s) {
        return MessageFormat.format("%{0}%", s);
    }

    private static volatile DB instance;

    Connection connect() {
        Connection connection = null;

        try {
            String dbName = "cinema";
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbName, "admin", "password");
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

    public List<String> getMovieGenres() {
        List<String> list = new ArrayList<>();
        try (var con = connect();
             var stmt = con.prepareStatement("""
                     
                     select distinct genre from movie_genres natural join genres order by genre;
                     
                     """)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("genre"));

            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return list;

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


    public List<Movie> movieList() {
        List<Movie> list = new ArrayList<>();
        try (Connection con = connect();
             Statement stmt = con.createStatement();

//             "SELECT * FROM view_all_movies"
             ResultSet rs = stmt.executeQuery("SELECT * FROM get_movies('%%','%%')")
        ) {

            while (rs.next()) {
                int id = rs.getInt("movie_id");
                String title = rs.getString("title");
                String genres = rs.getString("genres");
                list.add(new Movie(id, title, genres));

            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return list;

    }


//    public List<Movie> getMovies(String selectedTitle, String selectedGenre) {
//        List<Movie> list = new ArrayList<>();
//        try (Connection con = connect();
//             Statement stmt = con.createStatement();
//             ResultSet rs = stmt.executeQuery(STR."SELECT * FROM get_movies('%\{selectedTitle}%','%\{selectedGenre}%')")
//        ) {
//
//            while (rs.next()) {
//                int id = rs.getInt("movie_id");
//                String title = rs.getString("movie_title");
//                String genres = rs.getString("film_genre");
//                list.add(new Movie(id, title, genres));
//
//            }
//
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
//        return list;
//
//    }


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


    public String getMovieName(int movieID) {


        try (var con = connect();
             var stmt = con.prepareStatement("SELECT title FROM movies WHERE movie_id = ?")) {
            stmt.setInt(1, movieID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                return rs.getString("title");

            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return "error fetching movie name by movie id";

    }


    public List<String> getSelectedMovieGenres(int movieID) {
        List<String> list = new ArrayList<>();

        try (var con = connect();
             var stmt = con.prepareStatement("""
                     
                     select mg.movie_id, g.genre, g.genre_id
                     from movie_genres mg
                     natural join genres g
                     where mg.movie_id=?
                     order by g.genre;
                     
                     """)) {
            stmt.setInt(1, movieID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("genre"));

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


    // public boolean addMovie(String title, String genres) {
    //     try (var con = connect();
    //          var stmt = con.prepareStatement("INSERT INTO movies(title, genres) VALUES(?, ?)")) {
    //         stmt.setString(1, title);
    //         stmt.setString(2, genres);
    //         return stmt.execute();
    //     } catch (Exception ex) {
    //         System.err.println(ex.getMessage());
    //     }
    //     return false;
    // }

    public boolean addMovie(String title) {

        try (Connection con = connect();
             var stmt = con.prepareStatement("INSERT INTO movies(title) VALUES(?)")) {
            stmt.setString(1, title);
            return stmt.executeUpdate() == 1;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return false;

    }




    public boolean deleteMovieGenre(int movieID) {
        try (var con = connect();
             var stmt = con.prepareStatement("DELETE FROM movie_genres WHERE movie_id = ?")) {
            stmt.setInt(1, movieID);
            return stmt.execute();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
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
            System.err.println(ex.getMessage());
        }
    }
}
