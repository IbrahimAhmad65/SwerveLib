package main.cas;

public class CASTest {

    public static void main(String[] args) {
        Blob b = new Blobulus(new Add(new Multiply(new Power(new Variable("b"),new Constant(3)), new Constant(5)),new Variable("b")));
        System.out.println(b);
        b.replace(new Variable("b"),new Constant(67));
        System.out.println(b);
        b.cascade();
        System.out.println(b);
    }
}
