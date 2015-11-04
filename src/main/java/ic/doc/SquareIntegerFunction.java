package ic.doc;

public class SquareIntegerFunction implements UnaryFunction<Integer> {

    @Override
    public Integer applyTo(Integer input) {
        return input * input;
    }

}
