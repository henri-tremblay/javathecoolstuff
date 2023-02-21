import com.sun.net.httpserver.SimpleFileServer;
import jdk.incubator.concurrent.StructuredTaskScope;

import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.List;

public class Main {
    private static final String TEMPLATE = """
        <!DOCTYPE html>
        <html lang="fr">
        <head>
            <meta charset="UTF-8">
            <title>Java c'est cool</title>
            <link rel="stylesheet" href="bootstrap.min.css"></head>
        <body>
            <div class="alert alert-primary" role="alert">
                Java c'est comme %s... sauf que ça marche
            </div>
        </body>
        </html>""";

    private static final List<String> LANGUAGES = List.of("JavaScript", "Ruby", "Scala", "Clojure", "Dart", "Go", "Rust");

    public static void main(String[] args) throws Exception {
        try (var scope = new StructuredTaskScope<Void>()) {
            scope.fork(() -> {
                while(true) {
                    for (String language : LANGUAGES) {
                        String result = TEMPLATE.formatted(language);
                        Files.writeString(Path.of("index.html"), result, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                        Thread.sleep(Duration.ofSeconds(2));
                    }
                }
            });

            var server = SimpleFileServer.createFileServer(
                new InetSocketAddress(8000),
                Path.of(".").toAbsolutePath(),
                SimpleFileServer.OutputLevel.INFO);

            server.start();

            scope.join();
        }
    }
}
