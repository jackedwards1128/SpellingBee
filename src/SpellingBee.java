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

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        permutate(letters, "");
        for(int i = 0; i < words.size(); i++) {
            System.out.println(words.get(i));
        }
    }

    public void permutate(String storage, String output) {
        words.add(output);
        if(storage.length() == 0) {
            return;
        }

        for(int i = 0; i < storage.length(); i++) {
            String subOutput = output + storage.charAt(i);
            String subStorage = "";
            if (i != 0) {
                subStorage = storage.substring(0,i);
            }
            else if (i != storage.length() - 1) {
                subStorage = subStorage + storage.substring(i+1);
            }
            permutate(subStorage, subOutput);
        }

    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE

    }

    public char[] mergeSort(char[] arr, int start, int end) {
        if (start == end) {
            char[] result = new char[1];
            result[0] = arr[start];
            return result;
        }
        int middle = (start + end) / 2;
        char[] leftArr = mergeSort(arr, start, middle);
        char[] rightArr = mergeSort(arr, middle + 1, end);
        return merge(leftArr, rightArr);
    }

    public char[] merge(char[] arr1, char[] arr2) {
        // Set up the array for the merged array
        char[] result = new char[arr1.length + arr2.length];
        int a = 0;
        int b = 0;
        int c = 0;

        // Combine the two sub arrays into the result array
        while (a < arr1.length && b < arr2.length) {
            if (arr1[a] < arr2[b]) {
                result[c] = arr1[a];
                a++;
            } else {
                result[c] = arr2[b];
                b++;
            }
            c++;
        }

        // Take leftover from whatever array is still untransfered and dump it into result array
        while(a < arr1.length) {
            result[c++] = arr1[a++];
        }
        while(b < arr2.length) {
            result[c++] = arr1[b++];
        }

        return result;
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

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
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
