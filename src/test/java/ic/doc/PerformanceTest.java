package ic.doc;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;

public class PerformanceTest {

    public static final int SLEEP_TIME = 100;
    final FunctionalList<Integer> list
            = new FunctionalList<>(
                    Arrays.asList(43657, 43658, 43659, 43660,
                                                        43661, 43662, 43666));

    @Test
    public void artificiallyLongRunningTimeForConcurrentMappingTest() {
        long startTime = System.currentTimeMillis();
        UnaryFunction<Integer> slowSquare = new UnaryFunction<Integer>() {
            @Override
            public Integer applyTo(Integer n) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return n * n;
            }
        };
        FunctionalList<Integer> squares = list.applyMap(slowSquare);
        assertThat(squares, contains(1905933649, 1906020964, 1906108281,
                               1906195600, 1906282921, 1906370244, 1906719556));
        int estimatedTime = (int) (System.currentTimeMillis() - startTime);
        assertThat(estimatedTime, lessThan(2 * SLEEP_TIME));
    }

    @Test
    public void naturallyLongRunningTimeForConcurrentMappingTest(){
        long startTime = System.currentTimeMillis();
        UnaryFunction<Integer> sumPrime = new SumOfPrimes();
        FunctionalList<Integer> sumPrimesList = list.applyMap(sumPrime);
        assertThat(sumPrimesList, contains(93646090, 93646090, 93646090,
                                     93646090, 93689751, 93689751, 93689751));
        int estimatedTime = (int) (System.currentTimeMillis() - startTime);
        System.out.println("Running time: roughly " + estimatedTime + "ms");
    }

    @Test
    public void concurrentFoldTest() {
        long startTime              = System.currentTimeMillis();
        BinaryFunction<Integer> sum = new BinaryFunction<Integer>() {
            @Override
            public Integer applyTo(Integer n, Integer m) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return n + m;
            }
        };
        Integer result = list.applyFold(sum, 0);
        assertThat(result, is(305623));
        int estimatedTime = (int) (System.currentTimeMillis() - startTime);
        System.out.println("Running time: roughly " + estimatedTime + "ms");
    }

    private static class SumOfPrimes implements UnaryFunction<Integer> {
        @Override
        public Integer applyTo(Integer n) {
            int sum = 0;
            for (int i = 2; i <= n; i++) {
                boolean prime = true;
                for (int j = 2; j <= Math.sqrt(i); j++) {
                    if (i % j == 0) {
                        prime = false;
                    }
                }
                if (prime) {
                    sum += i;
                }
            }
            return sum;
        }
    }
}

