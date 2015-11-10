package ic.doc;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FunctionalList<T> implements Iterable<T> {

    private final List<T> delegate;

    public FunctionalList(List<T> delegate) {
        this.delegate = delegate;
    }

    /* Don't call me Java, nor Haskell. Call me... JASKELL! */

    public FunctionalList<T> map(UnaryFunction<T> mapper) {

        ExecutorService queue = Executors.newFixedThreadPool(1);
        List<T> result = new ArrayList<>();

        for (T element : delegate) {
            queue.execute(new UnaryFunctionApplication(result, mapper, element));
        }
        return new FunctionalList(result);
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

    private class UnaryFunctionApplication implements Runnable {

        private UnaryFunction<T> function;
        private List<T> results;
        private T elem;

        UnaryFunctionApplication(List<T> results, UnaryFunction<T> function, T elem) {
            this.results  = results;
            this.function = function;
            this.elem     = elem;
        }

        @Override
        public void run() {
            System.out.println("Application of function on " + elem.toString() + " started");
            results.add(function.applyTo(elem));
            System.out.println("Application of function on " + elem.toString() + " finished");
        }

    }
}
