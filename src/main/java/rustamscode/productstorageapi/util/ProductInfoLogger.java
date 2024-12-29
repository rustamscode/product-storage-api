package rustamscode.productstorageapi.util;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@UtilityClass
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductInfoLogger {

  private final String PRICE_UPDATE_LOGFILE = "price_update_log";

  public void logProductPriceUpdateInfo(ResultSet resultSet) {
    try (BufferedWriter writer = Files.newBufferedWriter(Path.of(PRICE_UPDATE_LOGFILE))) {
      while (resultSet.next()) {
        writer.write(buildString(resultSet));
        writer.newLine();
      }

    } catch (IOException | SQLException e) {
      log.error(
              "There was a problem logging product price update info: {}",
              e.getMessage()
      );
      throw new RuntimeException(e);
    }
  }

  private String buildString(final ResultSet resultSet) throws SQLException {
    return String.join(" ",
            resultSet.getString("id"),
            resultSet.getString("name"),
            resultSet.getString("product_number"),
            resultSet.getString("info"),
            resultSet.getString("category"),
            resultSet.getString("price"),
            resultSet.getString("amount"),
            resultSet.getString("last_amount_update"),
            resultSet.getString("creation_time"),
            resultSet.getString("version"));
  }
}

