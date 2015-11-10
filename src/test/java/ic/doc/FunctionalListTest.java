package ic.doc;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class FunctionalListTest {

    final FunctionalList<Integer> fList
            = new FunctionalList(Arrays.asList(1, 2, 3, 4));
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

        FunctionalList<Integer> outputList = fList.map(squareFunction);
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


}
