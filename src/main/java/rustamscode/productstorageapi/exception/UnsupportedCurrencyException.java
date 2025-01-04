package rustamscode.productstorageapi.exception;

public class UnsupportedCurrencyException extends RuntimeException {
    public UnsupportedCurrencyException(String currencyName) {
        super("Currency with name " + currencyName + "was not found");
    }
}
