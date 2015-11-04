package ic.doc;

import java.io.InvalidClassException;
import java.util.*;

public class FunctionalList<T> implements Iterable<T> {

    private final List<T> delegate;

    public FunctionalList(List<T> delegate) {
        this.delegate = delegate;
    }

    public List<T> map(UnaryFunction mapper) {
        List<T> result = new ArrayList<>();
        for (T element : delegate) {
            result.add((T) mapper.applyTo(element));
        }
        return result;
    }

    public Number fold(BinaryFunction folder) throws InvalidClassException {
        if (!delegate.isEmpty()) {
            T firstElem = delegate.get(0);
            if (firstElem instanceof Number) {
                Number accumulator = (Number) firstElem;
                for (T element : delegate) {
                    accumulator = folder.applyTo(accumulator, (Number) element);
                }
                return accumulator;
            } else {
                throw new InvalidClassException("Fold can only apply"
                        + " numerical operations to numerical values");
            }
        } else {
            throw new NullPointerException("Cannot apply fold to an"
                        + " empty list!");
        }
    }

    public Iterator<T> iterator() {
        return delegate.iterator();
    }

}
