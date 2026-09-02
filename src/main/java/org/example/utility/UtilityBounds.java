package org.example.utility;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Demonstracijske generičke metode koje pokrivaju "bounded wildcards"
 * (gornju i donju granicu), PECS princip (Producer Extends, Consumer Super)
 * i višestruka ograničenja tipa.
 */
public class UtilityBounds {
    // WILDCARDS ----------------------------------------------------------------------------------------------------
    /**
     * Ispisuje sve elemente dane liste, neovisno o njenom tipu (wildcard bez ograničenja).
     *
     * @param list lista bilo kojeg tipa elemenata
     */
    public static void printList(List<?> list) {
        for (Object o : list) {
            System.out.println(o);
        }
    }

    // PECS -------------------------------------------------------------------------------------------------------
    /**
     * Kopira sve elemente iz skupa (proizvođač, "extends") u listu (potrošač, "super") — primjer PECS principa.
     *
     * @param set  izvorni skup (proizvođač podataka)
     * @param list odredišna lista (potrošač podataka)
     * @param <T>  zajednički tip elemenata
     */
    public static <T> void copySetToList(Set<? extends T> set, List<? super T> list) {
        list.addAll(set);
    }

    /**
     * Kopira sve elemente iz liste (proizvođač, "extends") u skup (potrošač, "super") — primjer PECS principa.
     *
     * @param set  odredišni skup (potrošač podataka)
     * @param list izvorna lista (proizvođač podataka)
     * @param <T>  zajednički tip elemenata
     */
    public static <T> void copyListToSet(Set<? super T> set, List<? extends T> list) {
        set.addAll(list);
    }

    // LOWER BOUNDED -----------------------------------------------------------------------------------------------
    /**
     * Dodaje zapis u listu čiji je tip elementa {@link org.example.entities.Record} ili neki
     * njegov nadtip (donja granica, "super").
     *
     * @param list   lista u koju treba dodati zapis
     * @param record zapis koji treba dodati
     */
    public static void addToList(List<? super org.example.entities.Record> list, org.example.entities.Record record) {
        list.add(record);
    }

    // FILTER & UPPER BOUNDED ----------------------------------------------------------------------------------------
    /**
     * Ispisuje zapise čija je cijena stavke manja od 300 — primjer gornje granice (upper bounded wildcard).
     *
     * @param events lista zapisa čiji je tip elementa {@link org.example.entities.Record} ili njegova podklasa
     */
    public static void printCheapEvents(List<? extends org.example.entities.Record> events) {
        events.stream()
                .filter(e -> e.getItemPrice().compareTo(new BigDecimal("300")) < 0)
                .forEach(System.out::println);
    }

    //MOULLTIPLE BOUNDS----------------------------------------------------------------------------------------------
    /**
     * Vraća veći od dva broja — primjer višestrukog ograničenja tipa
     * (tip mora biti i {@link Number} i {@link Comparable}).
     *
     * @param item1 prvi broj
     * @param item2 drugi broj
     * @param <T>   tip broja koji je i {@link Number} i {@link Comparable} sam sa sobom
     * @return veći od dva broja
     */
    public static <T extends Number & Comparable<T>> T comparePrices(T item1, T item2) {
        return item1.compareTo(item2) > 0 ? item1 : item2;
    }

}