package bo.edu.ucb.sis213.util;

// todo: implementar title
public class ATMException extends Exception {
    int exitStatus = -1;

    public ATMException(String message) {
        super(message);
    }

    public ATMException(String message, int exitStatus) {
        super(message);
        this.exitStatus = exitStatus;
    }

    public int getExitStatus() {
        return this.exitStatus;
    }
}
