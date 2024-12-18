package rustamscode.productstorageapi.exception;

public class UnsupportedOperationTypeException extends RuntimeException {
    public UnsupportedOperationTypeException(String operationType) {
        super("Operation type " + operationType + " is not supported.");
    }

    public UnsupportedOperationTypeException(String operationType, String criteriaName) {
        super("Operation type " + operationType + " is not supported for "
                + criteriaName + ".");
    }
}
