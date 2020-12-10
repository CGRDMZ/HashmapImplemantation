import java.util.Locale;

public class PAFHashService<K> implements HashService<K> {
    @Override
    public int hashCode(K key) {
        String s = (String) key;
        int sum = 0;
        int z = 7; // 7 gave less collision compared to bigger prime numbers.
        for (int i = 0; i < s.length(); i++) {
            sum += (((int) s.toLowerCase(Locale.ENGLISH).charAt(i) - 96) * Math.pow(z, s.length() - (i + 1)));
        }
        return Math.abs(sum);
    }

    @Override
    public int hashFunc(int hashCode, int tableSize) {
        return hashCode % tableSize;
    }
}
