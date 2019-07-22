package service.exception;

public class ResourceNotFoundException extends Exception {

    private String message;

    public ResourceNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
