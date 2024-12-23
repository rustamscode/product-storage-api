package rustamscode.productstorageapi.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rustamscode.productstorageapi.persistance.entity.ProductEntity;
import rustamscode.productstorageapi.persistance.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
@ConditionalOnExpression("'${spring.scheduling.mode:none}'.equals('simple')")
public class SimpleProductPriceScheduler implements ProductPriceScheduler {

    final ProductRepository productRepository;

    @Value("${spring.scheduling.priceIncreasePercentage}")
    BigDecimal increasePercentage;

    @Transactional
    @Scheduled(fixedRateString = "${spring.scheduling.period}")
    public void increasePrice() {
        log.info("Simple scheduler started");

        List<ProductEntity> products = productRepository.findAll();

        products.forEach(product -> {
            BigDecimal increase = product.getPrice().multiply(increasePercentage.divide(BigDecimal.valueOf(100)));
            product.setPrice(product.getPrice().add(increase));
        });

        productRepository.saveAll(products);
        log.info("Simple scheduler finished");
    }
}
