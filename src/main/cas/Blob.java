package main.cas;

public interface Blob {
    Blob c = null;

    default Blob getInstance() {
        return c;
    }

    default Blob[] peel() {
        return new Blob[]{c};
    }
}
