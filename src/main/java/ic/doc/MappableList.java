package ic.doc;

import java.util.*;

public class MappableList<T> implements Iterable<T> {

    private final List<T> delegate;

    public MappableList(List<T> delegate) {
        this.delegate = delegate;
    }

    /* Map Function */

    public List<T> map(Function mapper) {
        List<T> result = new ArrayList<>();
        for (T element : delegate) {
            result.add((T) mapper.applyTo(element));
        }
        return result;
    }

    public Iterator<T> iterator() {
        return delegate.iterator();
    }

}
