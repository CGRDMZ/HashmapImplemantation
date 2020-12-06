import java.util.ArrayList;
import java.util.Locale;

public class HashTableImpl<K, V> {
    private final static double MAX_LOAD_FACTOR = .70;
    private static final int INITIAL_TABLE_SIZE = 997;

    private HashTableEntry[] table;
    private int table_size;

    private double size;

    public HashTableImpl() {
        this.table = new HashTableEntry[INITIAL_TABLE_SIZE];
        table_size = INITIAL_TABLE_SIZE;
    }

    public HashTableEntry<K, V> get(K key) {
        int hashIndex = hashFunc(key);
        int initialIndex = hashIndex;
        while (table[hashIndex] != null && calculateDIB(initialIndex, hashIndex) <= calculateDIB(hashFunc((K) (table[hashIndex].getKey())), hashIndex)) {
            if (table[hashIndex].getKey().equals(key)) {
                return table[hashIndex];
            }
            hashIndex++;
        }
        return null;
    }

    public void put(K key, V value) {
//        System.out.println(count());
        if (!hasEnoughPlace()) {
            resize();
        }
        int hashIndex = hashFunc(key);

        if (table[hashIndex] != null && table[hashIndex].getKey().equals(key)) {
            table[hashIndex].setValue(value);
            return;
        }

        if (table[hashIndex] != null && !table[hashIndex].getKey().equals(key)) {
//            System.out.println("collision");
            collisionHandler(key, value);
            return;
        }

        if (table[hashIndex] == null) {
            size++;
        }
        table[hashIndex] = new HashTableEntry<>(key, value);
        table[hashIndex].setCode(hashCode(key));
    }

    private void collisionHandler(K key, V value) {
        int hash = hashFunc(key);

        int initialIndex = hash;

        if (hash >= table_size) {
            resize();
        }

        while (table[hash] != null && calculateDIB(initialIndex, hash) <= calculateDIB(hashFunc((K) (table[hash].getKey())), hash)) {
            hash++;
            if (hash >= table_size) {
                resize();
            }
            if (table[hash] != null && table[hash].getKey().equals(key)) {
                table[hash].setValue(value);
                return;
            }
        }

        if (table[hash] == null) {
            table[hash] = new HashTableEntry<>(key, value);
        } else {
            HashTableEntry<K,V> temp = table[hash];
            table[hash] = new HashTableEntry<>(key, value);
            collisionHandler(temp.getKey(), temp.getValue());
        }



    }

    private int calculateDIB(int initialIndex, int hash) {
        return hash - initialIndex;
    }

    private void resize() {
        ArrayList<HashTableEntry<K,V>> oldTable = new ArrayList<>();
        for (HashTableEntry entry :
                table) {
            if (entry != null) {
                oldTable.add(entry);
            }
        }

        table_size = table_size * 2;
        size = 0;

        table = new HashTableEntry[table_size];

        for (HashTableEntry<K,V> entry:
             oldTable) {
            put(entry.getKey(), entry.getValue());
        }

        System.out.println("new size: " + table.length);

    }

    public int count() {
        int counter = 0;
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                System.out.println(table[i].toString() + " index= " + i);
                counter++;
            }
        }
        return counter;
    }

    private int hashFunc(K key) {
        return hashCode(key) % table_size;
    }

    private int hashCode(K key) {
        String s = (String) key;
        int sum = 0;
        int z = 33;
        for (int i = 0; i < s.length(); i++) {
            sum += ((int) s.toLowerCase(Locale.ENGLISH).charAt(i) - 96) * Math.pow(z, s.length() - (i + 1));
        }
        return Math.abs(sum);
    }

    private boolean hasEnoughPlace() {
        return (size / (double) table_size) < MAX_LOAD_FACTOR;
    }

}
