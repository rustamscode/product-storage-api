package rustamscode.productstorageapi.enumeration;

import rustamscode.productstorageapi.exception.UnsupportedOperationTypeException;

public enum OperationType {
    EQUAL("="),
    LESS_THAN_OR_EQUAL("<="),
    GREATER_THAN_OR_EQUAL(">="),
    LIKE("~");

    final String sign;

    OperationType(String sign) {
        this.sign = sign;
    }

    public static OperationType fromOperation(String operation) {
        for (OperationType operationType : OperationType.values()) {
            if (operationType.name().equals(operation) || operationType.sign.equals(operation)) {
                return operationType;
            }
        }
        throw new UnsupportedOperationTypeException(operation);
    }
}
