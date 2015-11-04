package ic.doc;

import java.util.*;

public class FunctionalList<T> implements Iterable<T> {

    private final List<T> delegate;

    public FunctionalList(List<T> delegate) {
        this.delegate = delegate;
    }

    /* Don't call me Java, nor Haskell. Call me... JASKELL! */

    public List<T> map(UnaryFunction<T> mapper) {
        List<T> result = new ArrayList<>();
        for (T element : delegate) {
            result.add(mapper.applyTo(element));
        }
        return result;
    }


    public T fold(BinaryFunction<T> folder, T accumulator) {
        for (T element : delegate) {
            accumulator =  folder.applyTo(accumulator, element);
        }
        return accumulator;

    }

    public Iterator<T> iterator() {
        return delegate.iterator();
    }

}
