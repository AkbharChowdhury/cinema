import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class a {
    public static void main(String[] args) {
//        Database d = Database.getInstance();
//        System.out.println(d.getMovieName(40));



//        var props = new Properties();
//        var envFile = Paths.get("src/config.env");
//        try (var inputStream = Files.newInputStream(envFile)) {
//            props.load(inputStream);
//            System.out.println( props.get("VONAGE_API_KEY"));;
//
//        } catch (IOException e) {
////            throw new RuntimeException(e);
//        }
//        var props = ENVManager.getENV();
//        System.out.println(props.get("username"));
        var s = Database.getInstance();
        System.out.println(s.genres());



    }
}
