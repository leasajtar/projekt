package org.example.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * Generička kolekcija entiteta (npr. korisnika, rezervacija, stavki) koja
 * ih interno drži u {@link Map}i radi brzog dohvata po ID-u, a prema van
 * nudi filtriranje i sortiranje lambda izrazima.
 *
 * @param <T> tip entiteta koji se pohranjuje
 */
public class EntityCollection<T> {

    private final Map<Integer, T> items = new LinkedHashMap<>();
    private final Function<T, Integer> idExtractor;
    private int autoId = 1;

    public EntityCollection(Function<T, Integer> idExtractor) {
        this.idExtractor = idExtractor;
    }

    /** Dodaje entitet; ako idExtractor ne vrati ID, dodjeljuje interni redni broj. */
    public void add(T item) {
        Integer id = idExtractor.apply(item);
        items.put(id != null ? id : autoId++, item);
    }

    public void addAll(Collection<? extends T> newItems) {
        newItems.forEach(this::add);
    }

    public T get(int id) {
        return items.get(id);
    }

    public boolean remove(int id) {
        return items.remove(id) != null;
    }

    public List<T> filter(Predicate<? super T> predicate) {
        return items.values().stream()
                .filter(predicate)
                .toList();
    }

    public List<T> sortedBy(Comparator<? super T> comparator) {
        return items.values().stream()
                .sorted(comparator)
                .toList();
    }

    public List<T> asList() {
        return new ArrayList<>(items.values());
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}