package org.example.enteties;

@FunctionalInterface
public interface Comparator<T> {
    int compare(T o1, T o2);
// Vraća: negativan (o1 < o2), 0 (jednaki), pozitivan (o1 > o2)
}
