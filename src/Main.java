import models.Movie;

import java.text.MessageFormat;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DB db = DB.getInstance();
        System.out.println(db.getMovies("","mystery"));
//

    }

    private  static  String param(String s){
        return MessageFormat.format("%{0}%", s);
    }
}
