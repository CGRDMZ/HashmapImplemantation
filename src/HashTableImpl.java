import java.util.ArrayList;

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
        if (table[hashFunc(key)] != null ) {
            return table[hashFunc(key)];
        }
        return null;
    }

    public void put(K key, V value) {
        System.out.println((size / (double) table_size));
        if (!hasEnoughPlace()) {
            resize();
        }
        int hashIndex = hashFunc(key);


        if (table[hashIndex] != null && !table[hashIndex].getKey().equals(key)) {
//            System.out.println("collision");
            return;
        }

        if (table[hashIndex] == null) {
            size++;
        }
        table[hashIndex] = new HashTableEntry<>(key, value);
    }

    private void resize() {
        ArrayList<HashTableEntry<K,V>> oldTable = new ArrayList<>();
        for (HashTableEntry entry :
                table) {
            if (entry != null) {
                oldTable.add(entry);
            }
        }
        System.out.println(size);

        table_size = table_size * 2;
        size = 0;

        table = new HashTableEntry[table_size];
        System.out.println("resizing...");

        for (HashTableEntry<K,V> entry:
             oldTable) {
            put(entry.getKey(), entry.getValue());
        }

        System.out.println("new size: " + table.length);

    }

    private int hashFunc(K key) {
        return PAF((String) key) % table_size;
    }

    private int PAF(String s) {
        int sum = 0;
        int z = 33;
        for (int i = 0; i < s.length(); i++) {
            sum += ((int) s.toLowerCase().charAt(i) - 96) * Math.pow(z, s.length() - (i + 1));
        }
        return Math.abs(sum);
    }

    private boolean hasEnoughPlace() {
        return (size / (double) table_size) < MAX_LOAD_FACTOR;
    }

}
