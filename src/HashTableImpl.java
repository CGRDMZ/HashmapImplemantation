import java.util.ArrayList;
import java.util.Locale;

public class HashTableImpl<K, V> {
    private static double MAX_LOAD_FACTOR;
    private final static double DEFAULT_LOAD_FACTOR = 0.7;
    private static final int INITIAL_TABLE_SIZE = 997;

    private HashTableEntry<K, V>[] table;
    private int tableSize;

    private int collisionCount;

    private long size;

    private HashService<K> hashService;

    public HashTableImpl(double loadFactor) {
        this.table = new HashTableEntry[INITIAL_TABLE_SIZE];
        tableSize = INITIAL_TABLE_SIZE;
        collisionCount = 0;
        MAX_LOAD_FACTOR = loadFactor;
    }

    public HashTableImpl() {
        this(DEFAULT_LOAD_FACTOR);
    }


    public HashTableEntry<K, V> get(K key) {
        int hashIndex = hashFunc(hashCode(key));
        int initialIndex = hashIndex;
        while (table[hashIndex] != null
                && calculateDIB(initialIndex, hashIndex) <= calculateDIB(hashFunc((table[hashIndex].getCode())), hashIndex)) {
            if (table[hashIndex].getKey().equals(key)) {
                return table[hashIndex];
            }
            hashIndex++;
        }
        return null;
    }

    public void print(K key) {
        HashTableEntry entry = get(key);
        if (entry == null) {
            System.out.println("Not Found.");
            return;
        }
        System.out.printf("search: %s \nkey: %s \ncount: %s \nindex: %d\n---------------------------------%n", entry.getKey(), entry.getCode(), entry.getValue(), entry.getIndex());
    }


    public void put(K key, V value) {
//        System.out.println(size());
        if (!hasEnoughPlace()) {
            resize();
        }
        int hashIndex = hashFunc(hashCode(key));

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
        table[hashIndex].setIndex(hashIndex);
    }

    private void collisionHandler(K key, V value) {
        int hash = hashFunc(hashCode(key));

        int initialIndex = hash;

        if (hash >= tableSize) {
            resize();
        }

        collisionCount++;

        while (table[hash] != null && calculateDIB(initialIndex, hash) <= calculateDIB(hashFunc(table[hash].getCode()), hash)) {
            hash++;
            if (hash >= tableSize) {
                resize();
            }
            if (table[hash] != null && table[hash].getKey().equals(key)) {
                table[hash].setValue(value);
                return;
            }
        }

        if (table[hash] == null) {
            size++;
            table[hash] = new HashTableEntry<>(key, value);
            table[hash].setCode(hashCode(key));
            table[hash].setIndex(hash);
        } else {
            HashTableEntry<K, V> temp = table[hash];
            table[hash] = new HashTableEntry<>(key, value);
            table[hash].setCode(hashCode(key));
            table[hash].setIndex(hash);
            collisionHandler(temp.getKey(), temp.getValue());
        }


    }

    public int getCollisionCount() {
        return collisionCount;
    }

    private int calculateDIB(int initialIndex, int hash) {
        return hash - initialIndex;
    }

    private void resize() {
        ArrayList<HashTableEntry<K, V>> oldTable = new ArrayList<>();
        for (HashTableEntry entry :
                table) {
            if (entry != null) {
                oldTable.add(entry);
            }
        }

        tableSize = tableSize * 2;
        size = 0;

        table = new HashTableEntry[tableSize];

        for (HashTableEntry<K, V> entry :
                oldTable) {
            put(entry.getKey(), entry.getValue());
        }

        System.out.println("new size: " + table.length);

    }

    public int size() {
        //uncomment this for exact size, if you think there is a problem with the size variable.
//        int counter = 0;
//        for (int i = 0; i < table.length; i++) {
//            if (table[i] != null) {
//                counter++;
//            }
//        }
        return (int) size;
    }

    public void setHashService(HashService<K> hashService) {
        this.hashService = hashService;
    }

    private int hashFunc(int hashCode) {
        return hashService.hashFunc(hashCode, tableSize);
    }

    private int hashCode(K key) {
        return hashService.hashCode(key);
    }

    private boolean hasEnoughPlace() {
        return (size / (double) tableSize) < MAX_LOAD_FACTOR;
    }

}
