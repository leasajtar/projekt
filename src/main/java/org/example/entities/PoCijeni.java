package org.example.entities;

public class PoCijeni implements Comparator<Item>{
    @Override
    public int compare(Item o1, Item o2) {
        return o1.getPrice().compareTo(o2.getPrice());
    }
}
