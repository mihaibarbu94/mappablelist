package ic.doc;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class FunctionalListTest {

    final FunctionalList<Integer> fList
            = new FunctionalList<>(Arrays.asList(1, 2, 3, 4));
    final FunctionalList<Integer> emptyList
            = new FunctionalList(Arrays.asList());
    final UnaryFunction<Integer> squareFunction
            = new SquareIntegerFunction();
    final BinaryFunction<Integer> productFunction
            = new ProductIntegerFunction();
    private Integer result;

    @Test
    public void functionalListIsIterableThrough() {
        assertThat(fList, contains(1, 2, 3, 4));
    }

    @Test
      public void functionalListAppliesAGivenUnaryFunctionToAllItsElements() {

        FunctionalList<Integer> outputList = fList.applyMap(squareFunction);
        assertThat(outputList, contains(1, 4, 9, 16));
    }

    @Test
    public void functionalListAppliesAGivenBinaryFunctionToAllItsElements() {

        result = fList.applyFold(productFunction, 1);
        assertThat(result, is(24));
    }

    @Test
    public void foldFunctionAppliedToEmptyListReturnsAccumulator() {
        result = emptyList.applyFold(productFunction, 1);
        assertThat(result, is(1));
    }

    @Test (expected = IllegalArgumentException.class)
    public void applyMapWithAnEmptyListTest() {
        FunctionalList<Integer> outputList = emptyList.applyMap(squareFunction);
    }

    @Test
    public void applyFoldFunctionAppliedToEmptyListReturnsAccumulator() {
        BinaryFunction<Integer> sum = new BinaryFunction<Integer>() {
            @Override
            public Integer applyTo(Integer n, Integer m) {
                return n + m;
            }
        };
        Integer result = emptyList.applyFold(sum, 0);
        assertThat(result, is(0));
    }
}