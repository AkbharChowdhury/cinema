import models.Movie;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DB db = DB.getInstance();
        System.out.println(db.getMovies("f","drama"));


    }
}
