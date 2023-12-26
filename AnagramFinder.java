import java.io.*;
import java.util.Iterator;

/**
 * Class that implements a variety of methods to find the anagrams of a word in a dictionary
 * file with different data structures
 * @author Paikea Barricklow; cpb2162
 * @version 1.0.0 December 18, 2023
 */
public class AnagramFinder {
    /**
     *
     * Main method that drives the AnagramFinder process. It determines if the user
     * uses the method correctly, checks if the file exists, and calls to relevant methods
     * to: initialize the data structure, traverse through the dictionary, and find the anagrams
     * @param args 3 arguments: word, dictionary file, and data Structure.
     */

    public static void main(String[] args){
        //parsing of command line args
        if(args.length != 3){
            System.err.println("Usage: java AnagramFinder <word> <dictionary file> <bst|avl|hash>");
            System.exit(1); //exit with status 1 for error
        }

        String word = args[0].toLowerCase();
        String dictionaryFile = args[1];
        String dataStructure = args[2];

        //confirm dictionary file
        File file = new File(dictionaryFile);
        if(!file.exists() || !file.isFile()){
            System.err.println("Error: Cannot open file '" + args[1] + "' for input.");
            System.exit(1); //exit with status 1 for error
        }

        //confirm data structure type
        if(!isValidDataStructure(dataStructure)){
            System.err.println("Error: Invalid data structure '" + args[2] + "' received.");
            System.exit(1); //exit with status 1 for error
        }

        //Initialization of relevant map
        MyMap<String, MyList<String>> map = initializeDataStructure(dataStructure);

        //traverse dictionary
        traverseDictionary(map, dictionaryFile);

        //get anagrams
        getAnagrams(map, word);
    }

    /**
     * Returns if the data structure inputted by the user is valid (avl/bst/hash)
     * @param dataStructure The data structure type as a string.
     * @return true if the structure is valid, otherwise false.
     */
    private static boolean isValidDataStructure(String dataStructure){
        return dataStructure.equals("bst") || dataStructure.equals("avl") || dataStructure.equals("hash");
    }

    /**
     * Initializes and returns a map based on the designated data structure.
     * The method supports bst, avl, and hash data structures.
     * @param dataStructure The type of the data structure to initialize.
     * @return An instance of MyMap as either BSTMap, AVLTreeMap, or MyHashMap.
     */
    private static MyMap<String, MyList<String>> initializeDataStructure(String dataStructure){
        switch(dataStructure) {
            case "bst":
                return new BSTMap<>();
            case "avl":
                return new AVLTreeMap<>();
            case "hash":
                return new MyHashMap<>();
            default:
                throw new IllegalArgumentException("Invalid data structure.");
        }
    }

    /**
     * Traverses through dictionary by reading the words on each line and stores
     * it in a map. Converts each word into lowercase, sorts it, and uses that
     * as a key. Each original word is then added to the list of anagrams
     * mapped to this key.
     * @param map The map where the anagrams are stored.
     * @param dictionaryFile The path to the dictionary file.
     */
    private static void traverseDictionary(MyMap<String, MyList<String>> map, String dictionaryFile){
        try (BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile))){
            String currentWord;
            while ((currentWord = reader.readLine()) != null){ //while line has text
                String sortedWord = insertionSort(currentWord.toLowerCase()); //sorts word's characters
                MyList<String> anagrams = map.get(sortedWord); //looks up key
                if(anagrams == null) { //if not already used
                    anagrams = new MyLinkedList<>();
                    map.put(sortedWord, anagrams); //make it a key
                }
                anagrams.add(currentWord); //maps current word to the key

            }
        } catch (IOException e) {
            System.err.println("Error: An I/O error occurred reading '" + dictionaryFile + "'.");
            System.exit(1); //exit with status 1 for error
        }

    }

    /**
     * Sorts each word into a lowercase collection of the characters
     * in lexicographical order using insertion sort.
     * @param word Word to sort.
     * @return String containing the sorted character of the inputted word.
     */
    private static String insertionSort(String word) {
        char[] characters = word.toLowerCase().toCharArray(); //converts characters to array for easier sorting
        int n = characters.length;

        for (int i = 1; i < n; ++i) { //iterate through each element in array
            char current = characters[i];
            int k;
            for (k = i - 1; k >= 0 && characters[k] > current; k--) { //if the item on the left of k is larger
                characters[k + 1] = characters[k]; //moves value of k one spot to the right
            }
            characters[k + 1] = current; //once k reaches correct position, set value on right to current
        }

        return new String(characters);
    }

    /**
     * If there are no unique anagrams, prints "No Anagrams Found." Otherwise, removes
     * word if it exists in list, and uses insertion sort to sort the anagrams.
     * Displays the anagrams.
     * @param map The map containing the sorted words and their anagrams.
     * @param word The word to find anagrams for.
     */
    private static void getAnagrams(MyMap<String, MyList<String>> map, String word){
        String sortedWord = insertionSort(word.toLowerCase()); //sorts characters of word
        MyList<String> anagrams = map.get(sortedWord); //finds all mapped anagrams to the key of the sorted word

        if(anagrams == null || anagrams.isEmpty() || (anagrams.size() == 1 && anagrams.get(0).equalsIgnoreCase(word))){
            System.out.println("No anagrams found.");
        } else {
            removeItem(anagrams, word); //remove word if it exists in list
            insertionSort(anagrams); //insertion sorting the anagrams
            Iterator<String> anagramsIterator = anagrams.iterator();
            while (anagramsIterator.hasNext()) {
                String anagram = anagramsIterator.next(); //displays the anagrams
                System.out.println(anagram);
            }
        }
    }

    /**
     * Insertion sort of the anagrams in alphabetical order putting capitalized
     * letters first.
     * @param list The list of Strings to be put in order.
     */
    private static void insertionSort(MyList<String> list){
        for(int i = 0; i < list.size(); i++) { //iterates through list
            String current = list.get(i);
            int j = i - 1;
            while(j >= 0 && capitalizedOrder(list.get(j), current)){ //while j is not the first element and is not in the correct place
                list.set(j+1, list.get(j)); //moves value at j to the right
                j = j-1;
            }
            list.set(j+1, current); //once j reaches correct position, set value on right to current
        }
    }

    /**
     * Method for insertion sort that compares two strings' capitalization.
     * Set a boolean value for the first character of both strings. Checks
     * if both have the same capitalization, and if so returns a normal
     * alphabetical comparison. Otherwise, returns !aCapitalized,
     * meaning if A is capitalized returns false and vice versa.
     * @param a The first string to compare.
     * @param b The second string to compare.
     * @return True if the first string comes after the second string in the list.
     */
    private static boolean capitalizedOrder(String a, String b) {
        boolean aCapitalized = Character.isUpperCase(a.charAt(0));
        boolean bCapitalized = Character.isUpperCase(b.charAt(0));

        if(aCapitalized == bCapitalized) { //check if both have same capitalization
            return a.compareToIgnoreCase(b) > 0; //just sorts in alphabetical
        }
        return !aCapitalized; //returns true if first string comes after second string
    }

    /**
     * Adds all items to a temporary list except for the one to be removed.
     * Original list is then cleared and all the items of the tempList is
     * added to the original, removing the itemToRemove
     * @param list The list from which to remove the item.
     * @param itemToRemove The item to remove from the list.
     */
    private static void removeItem(MyList<String> list, String itemToRemove){
        MyList<String> tempList = new MyLinkedList<>(); //creates temp list
        for(int i = 0; i < list.size(); i++) {
            String currentItem = list.get(i);
            if(!currentItem.equalsIgnoreCase(itemToRemove)) {
                tempList.add(currentItem); //creates a temp list without the item to remove
            }
        }
        list.clear(); //clears list
        Iterator<String> tempListIterator = tempList.iterator();
        while (tempListIterator.hasNext()) {
            String item = tempListIterator.next();
            list.add(item); //copies temp list that has does not contain the item to remove
        }
    }
}
