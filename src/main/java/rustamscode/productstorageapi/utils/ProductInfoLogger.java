package rustamscode.productstorageapi.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.util.stream.Collectors.joining;

@Slf4j
@UtilityClass
public class ProductInfoLogger {
    private final String PRICE_UPDATE_LOGFILE = "price_update_log";

    public void logProductPriceUpdateInfo(List<String> entries) {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(PRICE_UPDATE_LOGFILE))) {
            writer.write(entries.stream()
                    .map(string -> String.join(" ", string.split(",")))
                    .collect(joining("\n")));
        } catch (IOException e) {
            log.error("There was a problem logging product price update info: {}",
                    e.getMessage());
        }
    }
}
