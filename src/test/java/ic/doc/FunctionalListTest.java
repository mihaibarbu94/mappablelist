package ic.doc;

import org.hamcrest.core.Is;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class FunctionalListTest {

    public static final int SLEEP_TIME = 100;
    final FunctionalList<Integer> fList
            = new FunctionalList(Arrays.asList(1,2,3,4));
    final FunctionalList<Integer> list
            = new FunctionalList<>
            (Arrays.asList(43657,43658,43659,43660,43661,43662, 43666));
    final UnaryFunction<Integer> squareFunction
            = new SquareIntegerFunction();
    final BinaryFunction<Integer> productFunction
            = new ProductIntegerFunction();
    private Integer result;

    @Test
    public void functionalListIsIterableThrough() {
        assertThat(fList, contains(1,2,3,4));
    }

    @Test
      public void functionalListAppliesAGivenUnaryFunctionToAllItsElements() {

        FunctionalList<Integer> outputList = fList.applyMap(squareFunction);
        assertThat(outputList, contains(1, 4, 9, 16));
    }

    @Test
    public void functionalListAppliesAGivenBinaryFunctionToAllItsElements() {

        result = fList.fold(productFunction, 1);
        assertThat(result, is(24));
    }

    @Test
    public void foldFunctionAppliedToEmptyListReturnsAccumulator() {
        FunctionalList<Integer> emptyList = new FunctionalList(Arrays.asList());
        result = emptyList.fold(productFunction, 1);
        assertThat(result, is(1));
    }

    @Test
    public void nonConcurrentFoldTest() {
        long startTime              = System.currentTimeMillis();
        BinaryFunction<Integer> sum = new BinaryFunction<Integer>() {
            @Override
            public Integer applyTo(Integer n, Integer m) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {e.printStackTrace();}
                return n + m;
            }
        };
        Integer result = list.fold(sum, 0);
        assertThat(result, Is.is(305623));
        int estimatedTime = (int) (System.currentTimeMillis() - startTime);
        System.out.println("Running time: roughly " + estimatedTime + "ms");
    }
}
