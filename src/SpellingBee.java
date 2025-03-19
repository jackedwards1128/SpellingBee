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
 * @author Zach Blick, Jack Edwards
 *
 * Written on March 18, 2025 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    // Declare instance variables
    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // Create all possible arrangements of letters where each letter may only be used AT MOST once
    public void generate() {
        permutate(letters, "");
    }

    // Recursively run through a tree of all possible permutations of letters and add each one to a list
    public void permutate(String storage, String output) {
        // Add the output to a list
        words.add(output);
        // Once all the letters have been used, return (means this is at the bottom of a branch of the tree)
        if(storage.length() == 0) {
            return;
        }

        // Loop through each letter in the storage letters to try all possible combinations of choosing
        // one of them to pick off and add to the output
        for(int i = 0; i < storage.length(); i++) {
            String subOutput = output + storage.charAt(i);
            String subStorage = "";
            // Splice the given letter out of the storage string
            if (i != 0) {
                subStorage = storage.substring(0,i);
            }
            if (i != storage.length() - 1) {
                subStorage = subStorage + storage.substring(i+1);
            }
            // Continue the tree via a recursive call
            permutate(subStorage, subOutput);
        }

    }

    // Alphabetically sort all of the possible permutations
    public void sort() {
        words = mergeSort(words, 0, words.size() - 1);
    }

    public ArrayList<String> mergeSort(ArrayList<String> arr, int start, int end) {
        // Base case: if our subsection of the array is one word long, just return an array with that word
        if (start == end) {
            ArrayList<String> result = new ArrayList<String>();
            result.add(arr.get(start));
            return result;
        }

        // Define the middle of the subarray and call mergeSort on each of the halves based on that middle
        int middle = (start + end) / 2;
        ArrayList<String> leftArr = mergeSort(arr, start, middle);
        ArrayList<String> rightArr = mergeSort(arr, middle + 1, end);

        // Merge the two halves together
        return merge(leftArr, rightArr);
    }

    public ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2) {
        // Set up the array for the merged array
        ArrayList<String> result = new ArrayList<String>();
        int a = 0;
        int b = 0;

        // Combine the two sub arrays into the result array
        while (a < arr1.size() && b < arr2.size()) {
            if (arr1.get(a).compareTo(arr2.get(b)) < 0) {
                result.add(arr1.remove(a));
            } else {
                result.add(arr2.remove(b));
            }
        }

        // Take leftover from whatever array is still untransfered and dump it into result array
        result.addAll(arr1);
        result.addAll(arr2);

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

    // Checks which of the possible permutations of the user-inputted letters are real words
    public void checkWords() {
        // Loop through every word and run binary search to determine whether they are in the dictionary
        for (int i = 0; i < words.size(); i++) {
            if (!binarySearch(0,DICTIONARY_SIZE-1, words.get(i))) {
                // If the permutation is not in the dictionary, remove it from the words list
                words.remove(i--);
            }
        }
    }

    // Searches for an item in a SORTED list by picking the spot in the middle, and determining whether
    // to go left or right or not, and then recursively repeating the process
    public boolean binarySearch(int start, int end, String target) {
        // Base case: if there's only one thing in the array (end is exclusive here), check whether that
        // one thing is the target, and then return true or false accordingly
        if (end - start < 2) {
            if (DICTIONARY[start].equals(target)) {
                return true;
            }
            return false;
        }

        // Pick a spot in the middle and check if it's the target. If it isn't, determine whether to
        // continue the search in the left or right direction, and recursively call binarySearch.
        int middle = (start + end) / 2;
        if (DICTIONARY[middle].equals(target)) {
            return true;
        }
        if (DICTIONARY[middle].compareTo(target) > 0) {
            return binarySearch(start, middle, target);
        }
        return binarySearch(middle + 1, end, target);
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
