import com.sun.net.httpserver.SimpleFileServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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

    public static void main(String[] args) throws IOException {
        String result = TEMPLATE.formatted(args[0]);
        Files.writeString(Path.of("index.html"), result, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        var server = SimpleFileServer.createFileServer(
            new InetSocketAddress(8000),
            Path.of(".").toAbsolutePath(),
            SimpleFileServer.OutputLevel.INFO);

        server.start();
    }
}
