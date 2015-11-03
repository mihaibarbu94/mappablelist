package ic.doc;

import java.util.*;

public class MappableList implements Iterable<Integer> {

    final private List<Integer> delegate;

    public MappableList(List<Integer> delegate) {
        this.delegate = delegate;
    }

    /* Map Function */

    public List<Integer> map(Function mapper) {
        List<Integer> result = new ArrayList<>();
        for (Integer element : delegate) {
            result.add(mapper.applyTo(element));
        }
        return result;
    }

    public Iterator<Integer> iterator() {
        return delegate.iterator();
    }

}
