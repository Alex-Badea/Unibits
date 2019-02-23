package parser;

public interface Pointable<T> {
    void shiftPointer();

    T getPointedElem();

    boolean hasNext();
}
