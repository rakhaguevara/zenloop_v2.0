package util;

import java.util.Iterator;

public class ArrayList<E> implements Iterable<E> {

    private transient Object[] arrayList;
    private static final int DEFAULT_CAPACITY = 10;
    private int size;

    // Ini yang akan disimpan ke XML (isinya hanya elemen valid)
    private java.util.List<E> validElements = new java.util.ArrayList<>();

    public ArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayList(int capacity) {
        if (capacity <= 0)
            capacity = DEFAULT_CAPACITY;
        this.arrayList = new Object[capacity];
        this.size = 0;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(Object obj) {
        return indexOf(obj) >= 0;
    }

    public int indexOf(Object obj) {
        for (int i = 0; i < size; i++) {
            if (obj.equals(arrayList[i])) {
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        arrayList = new Object[DEFAULT_CAPACITY];
        validElements.clear();
        size = 0;
    }

    private boolean isFull() {
        return size == arrayList.length;
    }

    private void resizeArray() {
        int newCapacity = arrayList.length + (arrayList.length >> 1);
        Object[] newArray = new Object[newCapacity];
        System.arraycopy(arrayList, 0, newArray, 0, size);
        arrayList = newArray;
    }

    public void add(E obj) {
        if (isFull())
            resizeArray();
        arrayList[size++] = obj;
        validElements.add(obj);
    }

    public void add(int index, E obj) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();

        if (isFull())
            resizeArray();

        for (int i = size; i > index; i--) {
            arrayList[i] = arrayList[i - 1];
        }
        arrayList[index] = obj;
        size++;
        validElements.add(index, obj);
    }

    public E get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        return (E) arrayList[index];
    }

    public void set(int index, E obj) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        arrayList[index] = obj;
        validElements.set(index, obj);
    }

    public void remove(Object obj) {
        int index = indexOf(obj);
        if (index != -1)
            remove(index);
    }

    public void remove(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();

        for (int i = index; i < size - 1; i++) {
            arrayList[i] = arrayList[i + 1];
        }

        arrayList[--size] = null;
        validElements.remove(index);
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public E next() {
                return (E) arrayList[currentIndex++];
            }
        };
    }

    // Ini dipanggil setelah load dari XML agar array internal dibangun ulang
    public void rebuildArrayAfterLoad() {
        arrayList = new Object[Math.max(DEFAULT_CAPACITY, validElements.size())];
        for (int i = 0; i < validElements.size(); i++) {
            arrayList[i] = validElements.get(i);
        }
        size = validElements.size();
    }

    // Optional: untuk XStream (tidak wajib, tapi bisa bantu debug/konversi manual)
    public java.util.List<E> getValidElements() {
        return validElements;
    }
}
