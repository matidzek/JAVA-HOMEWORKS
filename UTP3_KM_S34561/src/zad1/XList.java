package zad1;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class  XList<T> implements List<T> {
    private final List<T> xlist;
    @SafeVarargs
    public XList(T... lists) {
        xlist = new ArrayList<T>(Arrays.asList(lists));
    }

    private XList(List<T> list) {
        xlist = list;
    }
    public XList(Collection<T> collection) {
        this.xlist = new ArrayList<>(collection);
    }

    public static <R> XList<R> of(Collection<R> collection) {
        List<R> list = new ArrayList<>(collection);
        return new XList<R>(list);
    }

    @SafeVarargs
    public static <R> XList<R> of(R... r) {
        List<R> list = new ArrayList<>(Arrays.asList(r));
        return new XList<R>(list);
    }

    public static XList<String> charsOf(String str) {
        List<String> lista;
        lista = str.chars()
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.toList());
        return new XList<>(lista);
    }

    public static XList<String> tokensOf(String str) {
        List<String> lista = Arrays.asList(str.split(" "));
        return new XList<>(lista);
    }
    public static XList<String> tokensOf(String str, String delimiter) {
        List<String> lista = Arrays.asList(str.split(delimiter));
        return new XList<>(lista);
    }

    public XList<T> union(Collection<T> list) {
        List<T> lista = new ArrayList<>(this.xlist);
        lista.addAll(list);
        return new XList(lista);
    }
    public XList<T> union(T... list) {
        List<T> lista = new ArrayList<>(this.xlist);
        lista.addAll(new ArrayList<>(Arrays.asList(list)));
        return new XList(lista);
    }

    public XList<T> diff(Collection<T> zewn) {
        List<T> lista = this.xlist.stream().filter(x -> !zewn.contains(x)).collect(Collectors.toList());
        return new XList(lista);
    }
    public XList<T> unique() {
        return new XList<>(
                this.xlist.stream().distinct().collect(Collectors.toList()));
    }
    public <R> XList<XList<R>> combine() {
        return XList.of();
    }

    public <R> XList<R> collect(Function<T, R> function) {
        List<R> resultList = this.xlist.stream().map(function)
                .collect(Collectors.toList());
        return new XList<>(resultList);
    }
    public String join(String separator) {
        return this.xlist.stream()
                .map(Object::toString)
                .collect(Collectors.joining(separator));
    }
    public String join() {
        return join("");
    }

    public List<T> getXlist() {
        return xlist;
    }
    public void forEachWithIndex(BiConsumer<T, Integer> consumer) {
        for (int i = 0; i < this.xlist.size(); i++) {
            consumer.accept(this.xlist.get(i), i);
        }
    }

    @Override
    public int size() {
        return xlist.size();
    }

    @Override
    public boolean isEmpty() {
        return xlist.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return xlist.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return xlist.containsAll(c);
    }

    @Override
    public Iterator<T> iterator() {
        return xlist.iterator();
    }

    @Override
    public Object[] toArray() {
        return xlist.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return xlist.toArray(a);
    }

    @Override
    public boolean add(T t) {
        return xlist.add(t);
    }

    @Override
    public void add(int index, T element) {
        xlist.add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return xlist.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return xlist.addAll(index, c);
    }

    @Override
    public boolean remove(Object o) {
        return xlist.remove(o);
    }

    @Override
    public T remove(int index) {
        return xlist.remove(index);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return xlist.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return xlist.retainAll(c);
    }

    @Override
    public void clear() {
        xlist.clear();
    }

    @Override
    public T get(int index) {
        return xlist.get(index);
    }

    @Override
    public T set(int index, T element) {
        return xlist.set(index, element);
    }

    @Override
    public int indexOf(Object o) {
        return xlist.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return xlist.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return xlist.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return xlist.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return xlist.subList(fromIndex, toIndex);
    }

    @Override
    public String toString() {
        return this.xlist.toString();
    }
}
