package rustamscode.productstorageapi.scheduler;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rustamscode.productstorageapi.aspect.annotation.MeasureTime;
import rustamscode.productstorageapi.util.ProductInfoLogger;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
@ConditionalOnExpression("'${spring.scheduling.mode:none}'.equals('optimized')")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OptimizedProductPriceScheduler implements ProductPriceScheduler {

  final DataSource dataSource;

  final static String UPDATE_QUERY = """
          UPDATE products SET price = price + (price * (? / 100)) 
          RETURNING *;
          """;
  final static String LOCK_QUERY = "LOCK TABLE products IN EXCLUSIVE MODE;";

  @Value("${spring.scheduling.priceIncreasePercentage}")
  BigDecimal increasePercentage;

  @MeasureTime
  @Scheduled(fixedRateString = "${spring.scheduling.period}")
  public void increasePrice() throws SQLException {
    log.info("Optimized scheduler started");

    Connection connection = dataSource.getConnection();
    connection.setAutoCommit(false);

    try (connection) {
      final PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
      preparedStatement.setBigDecimal(1, increasePercentage);
      preparedStatement.setFetchSize(1000);
      final Statement statement = connection.createStatement();

      statement.execute(LOCK_QUERY);
      final ResultSet resultSet = preparedStatement.executeQuery();
      ProductInfoLogger.logProductPriceUpdateInfo(resultSet);

      connection.commit();
    } catch (Exception e) {
      connection.rollback();
      throw new RuntimeException(e);
    }

    log.info("Optimized scheduler finished");
  }
}

