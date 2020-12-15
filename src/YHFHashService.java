public class YHFHashService<K> implements HashService<K> {
    @Override
    public int hashCode(K key) {
        String s = (String) key;
        int p = 19;
        int hashCode = 0;
        byte[] byteArray = s.getBytes();
        for (int i = 0; i < byteArray.length; i++) {
            hashCode += byteArray[i] * byteArray[byteArray.length - 1 - i] * p;
        }
        return Math.abs(hashCode);
    }

    @Override
    public int hashFunc(int hashCode, int tableSize) {
        return hashCode % (tableSize - 1);
    }
}
