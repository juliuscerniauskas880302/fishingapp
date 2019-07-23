package service.exception;

import javax.ejb.ApplicationException;

@ApplicationException
public class ResourceNotFoundException extends Exception {
    private static final long serialVersionUID = 5920904873745325860L;

    private String message;

    public ResourceNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
