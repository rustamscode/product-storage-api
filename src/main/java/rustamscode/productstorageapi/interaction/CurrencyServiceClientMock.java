package rustamscode.productstorageapi.interaction;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import rustamscode.productstorageapi.interaction.dto.CurrencyRateDetails;

import java.math.BigDecimal;

@Profile("test")
@Service
public class CurrencyServiceClientMock implements CurrencyServiceClient {
    @Override
    public CurrencyRateDetails getCurrencyRateDetails() {
        return CurrencyRateDetails.builder()
                .RUB(BigDecimal.TWO)
                .USD(BigDecimal.TEN)
                .CNY(BigDecimal.ONE)
                .build();
    }
}
