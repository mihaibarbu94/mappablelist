package ic.doc;


public class SquareIntegerFunction implements Function<Integer> {

    @Override
    public Integer applyTo(Integer input) {
        return input * input;
    }
}
