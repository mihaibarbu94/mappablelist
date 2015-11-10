package ic.doc;

public interface UnaryFunction<T> extends Function<T> {

    T applyTo(T input);

}
