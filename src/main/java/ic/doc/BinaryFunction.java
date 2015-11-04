package ic.doc;

public interface BinaryFunction<T extends Number> {

    T applyTo(T first, T second);

}
