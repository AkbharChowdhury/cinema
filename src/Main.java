import models.Movie;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Movie> movieList = DB.getInstance().movieList();
        Search search  = new Search(movieList);
        search.setGenre("comedy");
//        search.setTitle("adventures");
        System.out.println(search.filterResults());
//        System.out.println(search);

    }
}
