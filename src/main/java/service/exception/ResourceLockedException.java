package service.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class ResourceLockedException extends RuntimeException {

    private String message;

    public ResourceLockedException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
