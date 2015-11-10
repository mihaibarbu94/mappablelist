package ic.doc;

public interface BinaryFunction<T> extends Function<T> {

    T applyTo(T first, T second);

}
