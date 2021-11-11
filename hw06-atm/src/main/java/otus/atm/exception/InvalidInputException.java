package otus.atm.exception;

public class InvalidInputException extends Exception {
    public static String errorMessage = "Transaction or amount value are invalid";

    public InvalidInputException() {
        super(errorMessage);
    }
}
