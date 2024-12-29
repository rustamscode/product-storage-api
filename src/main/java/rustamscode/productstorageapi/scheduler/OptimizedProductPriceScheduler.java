package rustamscode.productstorageapi.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rustamscode.productstorageapi.persistance.repository.ProductRepository;
import rustamscode.productstorageapi.utils.ProductInfoLogger;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.scheduling.optimization", havingValue = "true")
public class OptimizedProductPriceScheduler implements ProductPriceScheduler {

    final ProductRepository productRepository;

    @Value("${spring.scheduling.priceIncreasePercentage}")
    BigDecimal increasePercentage;

    @Transactional
    @Scheduled(fixedRateString = "${spring.scheduling.period}")
    public void increasePrice() {
        log.info("Optimized scheduler started");

        productRepository.lockTableForUpdate();
        List<String> updatedProducts = productRepository.updatePrice(increasePercentage);

        ProductInfoLogger.logProductPriceUpdateInfo(updatedProducts);
        log.info("Optimized scheduler finished");
    }
}
