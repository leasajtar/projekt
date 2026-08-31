package org.example.entities;

@FunctionalInterface
public interface Comparator<T> {
    int compare(T o1, T o2);
}
