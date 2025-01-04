package rustamscode.productstorageapi.currency;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import rustamscode.productstorageapi.enumeration.Currency;

@Data
@Component
@SessionScope
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurrencyProvider {

    @Value("${currency.default:RUB}")
    Currency currency;
}
