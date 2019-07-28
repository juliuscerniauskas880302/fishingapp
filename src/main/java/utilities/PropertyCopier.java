package utilities;

public interface PropertyCopier<T extends Object, K extends Object> {

    Object copy(K dest, T source);
}
