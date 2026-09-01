package org.example.collections;

import java.util.Objects;

/**
 * Generička veza (par) između dva entiteta bilo kojeg tipa —
 * npr. korisnik i vrsta događaja koju je rezervirao.
 *
 * @param <A> tip prvog entiteta u vezi
 * @param <B> tip drugog entiteta u vezi
 */
public class Relation<A, B> {

    private final A first;
    private final B second;

    public Relation(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return first + " - " + second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Relation<?, ?> other)) return false;
        return Objects.equals(first, other.first) && Objects.equals(second, other.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}