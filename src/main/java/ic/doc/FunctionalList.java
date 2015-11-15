package ic.doc;

import java.util.*;
import java.util.concurrent.*;

public class FunctionalList<T> implements Iterable<T> {

    // delegate list
    private final List<T> del;
    private int listSize;

    public FunctionalList(List<T> delegate) {
        this.del = delegate;
        listSize = del.size();
    }

    /* Don't call me Java, nor Haskell. Call me... JASKELL! Not Hava? */

    public FunctionalList<T> applyMap(UnaryFunction<T> mapper) {
        ExecutorService executor = Executors.newFixedThreadPool(listSize);
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

    public T applyFold(BinaryFunction<T> folder, T acc) {
        ExecutorService executor
                = Executors.newFixedThreadPool(6);
        List<T> temporary = del;
        List<Callable<T>> listOfTasks = new ArrayList<>();
        if (temporary.isEmpty()) {
            return acc;
        }
        temporary.set(0, foldHelper(folder, acc, executor,
                                                    temporary, listOfTasks));
        return temporary.get(0);
     }

    private T foldHelper(BinaryFunction<T> folder, T acc, ExecutorService
                    executor, List<T> temp, List<Callable<T>> listOfTasks) {
        int tempSize = temp.size();
        while (tempSize > 1) {
            passThroughArray(folder, acc, temp, listOfTasks, tempSize);
            List<Future<T>> futures;
            try {
                futures = executor.invokeAll(listOfTasks);
                temp = new ArrayList<>();
                for (Future<T> future : futures) {
                    temp.add(future.get());
                }
                listOfTasks.clear();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            tempSize = temp.size();
        }
        return temp.get(0);
    }

    private void passThroughArray(BinaryFunction<T> folder, T acc, List<T> temp,
                                  List<Callable<T>> listOfTasks, int tempSize) {
        for (int i = 0; i < tempSize; i++) {
            if (tempSize % 2 == 1 && i == tempSize - 1) {
                listOfTasks.add(new FoldFunction(folder, temp.get(i), acc));
            } else {
                listOfTasks.add(new FoldFunction(
                                    folder, temp.get(i), temp.get(++i)));
            }
        }
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
            return function.applyTo(elem);
        }
    }

    private class FoldFunction implements Callable<T> {

        private BinaryFunction<T> function;
        private T fst;
        private T snd;

        public FoldFunction(BinaryFunction<T> function, T fst, T snd) {
            this.function = function;
            this.fst = fst;
            this.snd = snd;
        }

        @Override
        public T call() {
            return function.applyTo(fst, snd);
        }
    }
}
