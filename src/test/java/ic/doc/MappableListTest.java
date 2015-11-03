package ic.doc;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class MappableListTest {

    final MappableList<Integer> mList
            = new MappableList(Arrays.asList(1, 2, 3, 4));
    final Function<Integer> squareFunction
            = new SquareIntegerFunction();


    @Test
    public void mappableListIsIterableThrough() {
        assertThat(mList, contains(1, 2, 3, 4));
    }

    @Test
    public void mappableListAppliesAGivenFunctionToAllItsElements() {

        List<Integer> outputList = mList.map(squareFunction);
        assertThat(outputList, contains(1, 4, 9, 16));
    }
}
