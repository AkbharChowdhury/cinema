import java.util.List;

public class Main {
    public static void main(String[] args) {
        DB db = DB.getInstance();
//        int lastId = db.getLastID("movie_id", "movies");
//        System.out.println(lastId);
//        List<String> list = db.genres();
        System.out.println(db.getAllGenres());

//        db.getAllGenres().for
//        list.stream().sorted().forEach(System.out::println);

    }
}
