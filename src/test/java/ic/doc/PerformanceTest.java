package ic.doc;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.lessThan;

public class PerformanceTest {

    public static final int SLEEP_TIME = 100;
    final FunctionalList<Integer> list
            = new FunctionalList<> (Arrays.asList(1,2,3,4,5,3456, 43657));

    @Test
    public void artificiallyLongRunningTimeTest() {
        long startTime = System.currentTimeMillis();
        UnaryFunction<Integer> slowSquare = new UnaryFunction<Integer>() {
            @Override
            public Integer applyTo(Integer n) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) { e.printStackTrace(); }
                return n * n;
            }
        };
        FunctionalList<Integer> squares = list.applyMap(slowSquare);
        assertThat(squares, contains(1, 4, 9, 16, 25,11943936));
        int estimatedTime = (int) (System.currentTimeMillis() - startTime);
        assertThat(estimatedTime, lessThan(2 * SLEEP_TIME));
    }

    @Test
    public void naturallyLongRunningTimeTest(){
        long startTime = System.currentTimeMillis();
        UnaryFunction<Integer> sumPrime = new UnaryFunction<Integer>() {
            @Override
            public Integer applyTo(Integer n) {
                int sum = 0;
                for (int i = 2; i <= n; i++) {
                    boolean prime = true;
                    for (int j = 2; j <= Math.sqrt(i); j++) {
                        if (i % j == 0) { prime = false; }
                    }
                    if (prime) { sum += i; }
                }
                return sum;
            }
        };
        FunctionalList<Integer> sumPrimesList = list.applyMap(sumPrime);
        assertThat(sumPrimesList, contains(0, 2, 5, 5, 10, 761455, 93646090));
        int estimatedTime = (int) (System.currentTimeMillis() - startTime);
        System.out.println("Running time: roughly " +
                estimatedTime + "ms");
    }

}

