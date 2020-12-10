public interface HashService<K> {
    int hashCode(K key);
    int hashFunc(int hashCode, int tableSize);
}
