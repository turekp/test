package pl.mediabit.test.docker;

public class ValueObject {

    private final int number;

    public ValueObject(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
