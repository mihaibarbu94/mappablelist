package ic.doc;

public class ProductIntegerFunction implements BinaryFunction<Integer> {

    @Override
    public Integer applyTo(Integer first, Integer second) {
        return first * second;
    }

}
