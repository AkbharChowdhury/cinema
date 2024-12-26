import java.util.List;

public class Main {
    public static void main(String[] args) {
        DB db = DB.getInstance();
        System.out.println(db.getMovieName(110));
        System.out.println(db.getMovieGenres(110));


    }
}
