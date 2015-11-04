package ic.doc;

import org.junit.Test;
import java.io.InvalidClassException;
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
            = new ProductFunction();

    @Test
    public void functionalListIsIterableThrough() {
        assertThat(fList, contains(1, 2, 3, 4));
    }

    @Test
      public void functionalListAppliesAGivenUnaryFunctionToAllItsElements() {

        List<Integer> outputList = fList.map(squareFunction);
        assertThat(outputList, contains(1, 4, 9, 16));
    }

    @Test
    public void functionalListAppliesAGivenBinaryFunctionToAllItsElements()
            throws InvalidClassException {

        Integer result = (Integer) fList.fold(productFunction);
        assertThat(result, is(24));
    }

    @Test
    public void foldFunctionAppliedToEmptyListThrowsException()
            throws InvalidClassException {
        boolean exceptionHasBeenThrown = false;

        // Creating a new empty list for this test only
        FunctionalList<Integer> fList
                = new FunctionalList(Arrays.asList());
        try {
            fList.fold(productFunction);
        } catch (NullPointerException e) {
            exceptionHasBeenThrown = true;
        }
        assertThat(exceptionHasBeenThrown, is(true));
    }

}
