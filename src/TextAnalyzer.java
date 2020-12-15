import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

public class TextAnalyzer {
    private static final String SEARCH_FILE_PATH = "./search.txt";
    private static final String STORY_TEXT_PATH = "./story.txt";

    private HashTableImpl<String, Integer> words;

    private Scanner text;
    private Scanner keyValues;
    private Scanner userInput;

    private long indexingTime;
    private long averageSearchTime;
    private long minSearchTime;
    private long maxSearchTime;


    public TextAnalyzer() {
        setConfig();
        indexWords();
        searchFromFile();
        printMetadata();

        String input;
        do {
            System.out.println("search the word: (\"-1\" for exit.)");
            input = userInput.next();
            if (!input.equals("-1")) {
                words.print(input);
            }
        } while (!input.equals("-1"));

    }



    private void setConfig() {
        try {
            text = new Scanner(new File(STORY_TEXT_PATH));
            text.useLocale(Locale.ENGLISH);
            keyValues = new Scanner(new File(SEARCH_FILE_PATH));
            keyValues.useLocale(Locale.ENGLISH);
            userInput = new Scanner(System.in);
            userInput.useLocale(Locale.ENGLISH);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        // set the load factor.
        float loadFactor;
        do {
            System.out.println("select the load factor (a number between 0 and 1. eg: 0.5):");
            loadFactor = userInput.nextFloat();
        } while (loadFactor <= 0.0 || loadFactor > 1.0);

        words = new HashTableImpl<>(loadFactor);


        // set the hash service.
        String input;
        do {
            System.out.println("select the hash service (PAF or YHF):");
            input = userInput.next().toLowerCase();
        } while (!input.equals("paf") && !input.equals("yhf"));

        if (input.equals("paf")) {
            words.setHashService(new PAFHashService<>());
        } else if (input.equals("yhf")) {
            words.setHashService(new YHFHashService<>());
        }
    }

    private void indexWords() {
        minSearchTime = Long.MAX_VALUE;
        maxSearchTime = Long.MIN_VALUE;

        long startTime = System.nanoTime();
        while (text.hasNext()) {
            String s = text.next().toLowerCase();
            HashTableEntry word = words.get(s);
            if (word == null) {
                words.put(s, 1);
            } else {
                words.put(s, (int) word.getValue() + 1);
            }
        }
        indexingTime = System.nanoTime() - startTime;
        text.close();
    }

    private void searchFromFile() {
        long totalTime = 0;
        while (keyValues.hasNext()) {
            String s = keyValues.next().toLowerCase();
            long startTime = System.nanoTime();
            words.get(s);
            long searchTime = System.nanoTime() - startTime;
            totalTime += searchTime;
            if (searchTime < minSearchTime) {
                minSearchTime = searchTime;
            }
            if (searchTime > maxSearchTime) {
                maxSearchTime = searchTime;
            }
        }

        keyValues.close();

        averageSearchTime = totalTime / getWordCountInAFile(SEARCH_FILE_PATH);


    }

    public void printMetadata() {
        System.out.printf("-------Metadata--------\n" +
                        "Collision Count: %d\n" +
                        "indexing time: %f secs\n" +
                        "Average Search Time: %d nano seconds\n" +
                        "Min. Search Time: %d nano seconds\n" +
                        "Max. Search Time: %d nano seconds\n" +
                        "-----------------------\n",
                words.getCollisionCount(), indexingTime / 1000000000f, averageSearchTime,
                minSearchTime, maxSearchTime);
        System.out.println("There are \"" + words.size() + "\" unique words.");
    }

    public static int getWordCountInAFile(String path) {
        int counter = 0;
        Scanner sc = null;
        try {
            sc = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (sc.hasNext()) {
            sc.next();
            counter++;
        }
        sc.close();
        return counter;
    }
}
