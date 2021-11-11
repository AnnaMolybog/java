package otus.atm.exception;

public class MinimalDenominationException extends Exception {
    public static String errorMessage = "Minimal denomination is: %d";

    public MinimalDenominationException(int denomination) {
        super(String.format(errorMessage, denomination));
    }
}
