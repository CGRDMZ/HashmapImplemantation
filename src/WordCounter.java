import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

public class WordCounter {
    HashTableImpl<String, Integer> words;

    public WordCounter() {
        words = new HashTableImpl();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("./story.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNext()) {
            String word = scanner.next().toLowerCase(Locale.ENGLISH);
            if (words.get(word) == null) {
                words.put(word, 1);
            } else {
                words.put(word, words.get(word).getValue() + 1);
            }
        }
        System.out.println(words.count());



    }
}
