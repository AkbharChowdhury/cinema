import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ENVManager {
    public static Properties getENV(){
        var props = new Properties();
        var envFile = Paths.get("src/config.env");
        try (var inputStream = Files.newInputStream(envFile)) {
            props.load(inputStream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return props;
    }
}
