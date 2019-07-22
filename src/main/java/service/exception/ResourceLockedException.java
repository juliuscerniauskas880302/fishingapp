package service.exception;

public class ResourceLockedException extends RuntimeException {
    private static final long serialVersionUID = -1208300942356269951L;

    private String message;

    public ResourceLockedException(String message) {
        this.message = message;
    }

}
