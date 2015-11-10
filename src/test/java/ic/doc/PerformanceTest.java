package ic.doc;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class PerformanceTest {

    final FunctionalList<Integer> list
            = new FunctionalList<> (Arrays.asList(1,2,3,4));

    @Test
    public void longRunningTimeTest() {

        long startTime = System.currentTimeMillis();

        UnaryFunction<Integer> slowSquare = new UnaryFunction<Integer>() {

            @Override
            public Integer applyTo(Integer n) {

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // interrupted
                    e.printStackTrace();
                }

                return n * n;
            }
        };

        FunctionalList<Integer> squares = list.map(slowSquare);
        assertThat(squares, contains(1, 4, 9, 16));

        long estimatedTime = System.currentTimeMillis() - startTime;

        System.out.println("Running time: roughly " +
                                                    estimatedTime + "ms");
    }
}
//
//
//    @Test
//    public void fastRunningTimeTest(){
//
//        long startTime = System.currentTimeMillis();
//
//
//        long estimatedTime = System.currentTimeMillis() - startTime;
//
//        System.out.println("Running time: roughly " +
//                estimatedTime / 1000 + "s");
//
//    }
