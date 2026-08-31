package org.example.utility;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class UtilityBounds {
    // WILDCARDS ----------------------------------------------------------------------------------------------------
    public static void printList(List<?> list) {
        for (Object o : list) {
            System.out.println(o);
        }
    }

    // PECS -------------------------------------------------------------------------------------------------------
    public static <T> void copySetToList(Set<? extends T> set, List<? super T> list) {
        list.addAll(set);
    }

    public static <T> void copyListToSet(Set<? super T> set, List<? extends T> list) {
        set.addAll(list);
    }

    // LOWER BOUNDED -----------------------------------------------------------------------------------------------
    public static void addToList(List<? super org.example.enteties.Record> list, org.example.enteties.Record record) {
        list.add(record);
    }

    // FILTER & UPPER BOUNDED ----------------------------------------------------------------------------------------
    public static void printCheapEvents(List<? extends org.example.enteties.Record> events) {
        events.stream()
                .filter(e -> e.getItemPrice().compareTo(new BigDecimal("300")) < 0)
                .forEach(System.out::println);
    }

    //MOULLTIPLE BOUNDS----------------------------------------------------------------------------------------------
    public static <T extends Number & Comparable<T>> T comparePrices(T item1, T item2) {
        return item1.compareTo(item2) > 0 ? item1 : item2;
    }

}
