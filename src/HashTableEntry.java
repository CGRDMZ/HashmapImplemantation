public class HashTableEntry<K, V> {
    private K key;
    private V value;
    private int code;

    public HashTableEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "key: " + key + " value: " + value;
    }
}
