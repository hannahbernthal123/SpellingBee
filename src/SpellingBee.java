import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    public void generate() {
        makeWords("", letters);
    }

    public void makeWords(String word, String letters) {
        if (letters.isEmpty()) {
            words.add(word);
        }

        for (int i  = 0; i < letters.length(); i++) {
            makeWords(word + letters.charAt(i), letters.substring(0, i) + letters.substring(i + 1));
            words.add(word + letters.charAt(i));
        }
    }

    public void sort() {
        mergeSort(words, 0, words.size());
    }

    public ArrayList<String> mergeSort(ArrayList<String> arr, int low, int high) {
        if (high -  low == 0) {
            ArrayList<String> sorted = new ArrayList<>();
            sorted.add(arr.get(0));
            return sorted;
        }
        int mid = (high + low) / 2;
        ArrayList<String> arr1 = mergeSort(arr, low, mid);
        ArrayList<String> arr2 = mergeSort(arr, mid + 1, high);
        return merge(arr1, arr2);
    }

    public ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2) {
        ArrayList<String> merged = new ArrayList<>();
        int index = 0;
        int index2 = 0;
        while (index < arr1.size() && index2 < arr2.size()) {
            if (arr1.get(index).compareTo(arr2.get(index2)) < 0) {
                merged.add(arr1.get(index++));
            }
            else {
                merged.add(arr2.get(index2++));
            }
        }
        while (index < arr1.size()) {
            merged.add(arr1.get(index++));
        }
        while (index2 < arr2.size()) {
            merged.add(arr2.get(index2++));
        }
        return merged;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    public boolean checkDictionary (String word, int first, int last) {
        int mid = (first + last) / 2;
        if (DICTIONARY[mid].equals(word)) {
            return true;
        }
        if (first == last) {
            return DICTIONARY[first].equals(word);
        }
        if (DICTIONARY[mid].compareTo(word) < 0) {
            return checkDictionary(word, first, mid - 1);
        }
        // This line is (DICTIONARY[mid].compareTo(word) > 0)
        else {
            return checkDictionary(word, mid + 1, last);
        }
    }

    public void checkWords() {
        for (String word : words) {
            checkDictionary(word, 0, DICTIONARY_SIZE - 1);
        }
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }

        s.close();
    }
}
