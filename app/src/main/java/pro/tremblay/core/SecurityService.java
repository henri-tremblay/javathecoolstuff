package pro.tremblay.core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SecurityService {

    private final Object mutex = new Object();
    private volatile List<Security> allSecurities;

    public List<Security> allSecurities() {
        if (allSecurities == null) {
            synchronized (mutex) {
                allSecurities = readFile(Path.of("../listing_status.csv"), line -> {
                    String[] fields = line.split(",");
                    return new Security(
                        fields[0],
                        fields[1],
                        fields[2],
                        fields[3],
                        LocalDate.parse(fields[4])
                    );
                });
            }
        }
        return allSecurities;
    }

    private <T> List<T> readFile(Path file, Function<String, T> mapper) {
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file.toFile()), Charset.forName("UTF-8")));
        }
        catch(FileNotFoundException e) {
            throw new UncheckedIOException(e);
        }
        try {
            List<T> list = new ArrayList<>();
            String s = in.readLine(); // skip first line
            while((s = in.readLine()) != null) {
                list.add(mapper.apply(s));
            }
            return list;
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
        finally {
            try {
                in.close();
            }
            catch(IOException e) {
                // ignore silently
            }
        }
    }
}
