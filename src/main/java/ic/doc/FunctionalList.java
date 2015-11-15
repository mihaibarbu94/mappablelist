package ic.doc;

import java.util.*;
import java.util.concurrent.*;

public class FunctionalList<T> implements Iterable<T> {

    // delegate list
    private final List<T> del;

    public FunctionalList(List<T> delegate) {
        this.del = delegate;
    }

    /* Don't call me Java, nor Haskell. Call me... JASKELL! Not Hava? */

    public FunctionalList<T> applyMap(UnaryFunction<T> mapper) {
        ExecutorService executor = Executors.newFixedThreadPool(del.size());
        List<T> mappedList       = new ArrayList<>();
        List<Callable<T>> functionApplications = new ArrayList<>();
        for (T elem : del) {
            functionApplications.add(new MapFunction(mapper, elem));
        }
        List<Future<T>> fList;
        try {
            fList = executor.invokeAll(functionApplications);
            for(Future<T> future : fList) {
                mappedList.add(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return new FunctionalList<>(mappedList);
    }

    public T applyFold(BinaryFunction<T> folder, T accumulator) {
        ExecutorService executor
                = Executors.newFixedThreadPool(del.size() - 1);
        FoldFunction fold
                = new FoldFunction(folder, accumulator, del, executor);
        return fold.call();
    }

    public T fold(BinaryFunction<T> folder, T accumulator) {
        for (T element : del) {
            accumulator =  folder.applyTo(accumulator, element);
        }
        return accumulator;
    }

    public Iterator<T> iterator() {
        return del.iterator();
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
            //System.out.println("Mapping on " + elem.toString() + " started");
            return function.applyTo(elem);
        }
    }

    private class FoldFunction implements Callable<T> {

        private BinaryFunction<T> function;
        private T acc;
        private List<T> list;
        private ExecutorService executor;
        private int lstSize;

        FoldFunction(BinaryFunction<T> function, T acc,
                                       List<T> list, ExecutorService executor) {
            this.function = function;
            this.acc      = acc;
            this.list     = list;
            this.executor = executor;
            lstSize       = list.size();
        }

        @Override
        public T call() {
            //System.out.println("Folding in process... ");
            if (lstSize == 0) {
                return acc;
            } else if (lstSize == 1) {
                return list.get(0);
            } else {
                int splitInd = lstSize == 2 ? (lstSize / 2) : (lstSize / 2) + 1;
                try {
                    Future<T> first  = executor.submit(new FoldFunction(
                           function, acc, list.subList(0, splitInd), executor));
                    Future<T> second = executor.submit(new FoldFunction(
                           function, acc, list.subList(
                           splitInd, lstSize), executor));
                    return function.applyTo(first.get(), second.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                return acc;
            }
        }
    }
}
