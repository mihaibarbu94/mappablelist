package ic.doc;

import java.util.*;
import java.util.concurrent.*;

public class FunctionalList<T> implements Iterable<T> {

//    public static final int TIMEOUT = 10;
    private final List<T> delegate;

    public FunctionalList(List<T> delegate) {
        this.delegate = delegate;
    }

    /* Don't call me Java, nor Haskell. Call me... JASKELL! Not Hava? */

    public FunctionalList<T> applyMap(UnaryFunction<T> mapper) {
        ExecutorService executor = Executors.newFixedThreadPool(delegate.size());
        List<T> mappedList = new ArrayList<>();
        List<Callable<T>> functionApplications = new ArrayList<>();
        for (T elem : delegate) {
            functionApplications.add(new MapFunction(mapper, elem));
        }
        List<Future<T>> fList;
        try {
            fList = executor.invokeAll(functionApplications);
            for(Future<T> future : fList) {
                mappedList.add(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {e.printStackTrace();}
        return new FunctionalList<>(mappedList);
    }


    public T fold(BinaryFunction<T> folder, T accumulator) {
        for (T element : delegate) {
            accumulator =  folder.applyTo(accumulator, element);
        }
        return accumulator;
    }

    public void add(T elem) {
        delegate.add(elem);
    }

    public Iterator<T> iterator() {
        return delegate.iterator();
    }

    private class MapFunction implements Callable<T> {

        private UnaryFunction<T> function;
        private T elem;

        MapFunction(UnaryFunction<T> function, T elem) {
            this.function = function;
            this.elem     = elem;
        }

        @Override
        public T call() {
            System.out.println("Mapping on " + elem.toString()
                                                                + " started");
            return function.applyTo(elem);
        }

    }
}
