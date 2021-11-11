package otus.atm.exception;

public class InsufficientFundsException extends Exception {
    public static String errorMessage = "Insufficient funds in the account";

    public InsufficientFundsException() {
        super(errorMessage);
    }
}
