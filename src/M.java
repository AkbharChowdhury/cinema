import dbs.Database;
import enums.MovieEnum;

public class M {
    public static void main(String[] args) {
        Database db = Database.getInstance();
        System.out.println(db.getMovieGenres());
    }
}
