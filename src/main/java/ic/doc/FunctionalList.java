package ic.doc;

import java.util.*;
import java.util.concurrent.*;

public class FunctionalList<T> implements Iterable<T> {

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

    public T applyFold(BinaryFunction<T> folder, T accumulator) {
        ExecutorService executor
                = Executors.newFixedThreadPool(delegate.size());
        FoldFunction fold = new FoldFunction(folder, accumulator, delegate, executor);
        return fold.call();
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
            System.out.println("Mapping on " + elem.toString() + " started");
            return function.applyTo(elem);
        }

    }

    private class FoldFunction implements Callable<T> {

        private BinaryFunction<T> function;
        private T acc;
        private List<T> list;
        private ExecutorService executor;
        private int listSize;

        FoldFunction(BinaryFunction<T> function, T acc, List<T> list, ExecutorService executor) {
            this.function = function;
            this.acc = acc;
            this.list = list;
            this.executor = executor;
            listSize = list.size();
        }

        @Override
        public T call() {
            System.out.println("Folding in process... ");
            if (listSize == 0) {
                return acc;
            } else if (listSize == 1) {
                return list.get(0);
            } else {
                int splitInd = listSize == 2 ? (listSize / 2) : (listSize / 2) + 1;
                try {
                    Future<T> first = executor.submit(new FoldFunction(function, acc, list.subList(0, splitInd), executor));
                    Future<T> second = executor.submit(new FoldFunction(function, acc, list.subList(splitInd, listSize), executor));
                    return function.applyTo(first.get(), second.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                return acc;
            }
        }
    }
}
