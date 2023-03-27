/*
 * LetterSquare - represents the state of a letter-square puzzle,
 * and solves it using recursive backtracking.
 * 
 * starter code: Computer Science S-111 staff
 * 
 * code added by: Clara Henzinger, clh181@g.harvard.edu
 */

import java.util.*;

public class LetterSquare {
    public static final int MOST_WORDS = 10;
    public static final String WORDS_FILE = "word_list.txt";
    public static final Dictionary dictionary = new Dictionary(WORDS_FILE);
    
    // The sides of the puzzle, given as four strings of length 3.
    private String[] sides;
    
    // The individual letters in the puzzle.
    // Each element of this array is a single-character string.
    private String[] letters;
    
    // The words in the solution.
    private String[] words;
    
    /*
     * Constructor for a puzzle with the specified sides, where each
     * side is a string containing the 3 letters from one side of the square.
     */
    public LetterSquare(String[] sides) {
        if (sides == null || sides.length != 4) {
            throw new IllegalArgumentException(
                        "parameter must be an array of 4 strings");
        }
        
        this.sides = sides;
        this.letters = new String[12];
        int letterNum = 0;
        for (int i = 0; i < sides.length; i++) {
            if (sides[i] == null || sides[i].length() != 3) {
                throw new IllegalArgumentException(
                        "invalid side string: " + sides[i]);
            }
            
            for (int j = 0; j < 3; j++) {
                this.letters[letterNum] = this.sides[i].substring(j, j+1);
                letterNum++;
            }
        }
        
        this.words = new String[MOST_WORDS];
        for (int i = 0; i < this.words.length; i++) {
            this.words[i] = "";
        }
    }
    
    /*
     * Returns a string that shows the letters of the puzzle in "square" form.
     * For example, if the sides are {"cat", "dog", "fry", "pie"}, it will
     * return a string that when printed looks like this:
     *  c a t
     * d     f
     * o     r
     * g     y
     *  p i e
     */
    public String toString() {
        String s = "";
        
        // top of the square (i.e., sides[0])
        for (int i = 0; i < 3; i++) {
            s += " " + this.sides[0].charAt(i);
        }
        s += "\n";
        
        // left and right sides (sides[1] and sides[2])
        for (int i = 0; i < 3; i++) {
            s += this.sides[1].charAt(i);
            s += "     " + this.sides[2].charAt(i);
            s += "\n";
        }
        
        // bottom of the square (i.e., sides[3])
        for (int i = 0; i < 3; i++) {
            s += " " + this.sides[3].charAt(i);
        }
        s += "\n";
        
        return s;
    }
    
    /*
     * lastLetter - returns a single-character string containing
     * the last letter in the specified word
     * 
     * assumes that word is a String with at least one character
     */
    private static String lastLetter(String word) {
        return word.substring(word.length() - 1);
    }
    
    /*
     * removeLast - returns the string that is formed by removing
     * the last character of the specified word
     * 
     * assumes that word is a String with at least one character
     */
    private static String removeLast(String word) {
        return word.substring(0, word.length() - 1);
    }
    
    /*
     * addLetter - adds the specified letter as the next letter 
     * in the word at position wordNum in the solution
     * and also updates the solnString accordingly
     */
    private void addLetter(String letter, int wordNum) {
        this.words[wordNum] += letter;
    }
    
    /*
     * removeLetter - removes the specified letter from the end of  
     * the word at position wordNum in the solution
     * and also updates the solnString accordingly
     */
    private void removeLetter(int wordNum) {
        this.words[wordNum] = removeLast(this.words[wordNum]);
    }
    
    /*
     * alreadyUsed - returns true if the specified word is already 
     * one of the words in the solution, and false otherwise.
     */
    private boolean alreadyUsed(String word) {
        for (String w : this.words) {
            if (w.equals(word)) {
                return true;
            }
        }
        return false;
    }
    
    /*
     * onSameSide - returns true if the single-character strings 
     * letter1 and letter2 come from the same side of the puzzle,
     * and false otherwise
     */
    private boolean onSameSide(String letter1, String letter2) {
        for (String side : this.sides) {
            if (side.contains(letter1) && side.contains(letter2)) {
                return true;
            }
        }
        
        return false;
    }
    
    /*
     * allLettersUsed - returns true if all of the letters in the puzzle
     * are currently being used in the solution to the puzzle,
     * and false otherwise
     */
    private boolean allLettersUsed() {
        for (String letter : this.letters) {
            boolean anyWordHasLetter = false;
            
            for (String w : this.words) {
                if (w.contains(letter)) {
                    anyWordHasLetter = true;
                    break;
                }
            }
            
            if (!anyWordHasLetter) {
                return false;
            }
        }
        
        return true;
    }
    
    /*
     * printSolution - prints the words in the soln array from 
     * position 0 up to and including position wordNum
     */
    private void printSolution(int wordNum) {
        for (int i = 0; i <= wordNum; i++) {
            System.out.println(this.words[i]);
        }
    } 
    
    /* 
     * isValid - returns true if the specified letter (a one-character string)
     * is a valid choice for the letter in position charNum of the word at 
     * position wordNum in the soln, and false otherwise.
     * 
     * Since this is a private helper method, we assume that only 
     * appropriate values will be passed in. 
     * In particular, we assume that letter is one of the letters of the puzzle.
     */
    private boolean isValid(String letter, int wordNum, int charNum) {  
        String lastLetter;
        String word = this.words[wordNum];
        String newWord = word + letter;

        if(wordNum == 0){
            if(charNum == 0){
                return true;
            } else {
                boolean notOnSameSide = !onSameSide(letter, word.substring(charNum - 1, charNum));
                //System.out.println("1. notOnSameSide: " + notOnSameSide);
                //System.out.println("2. dictionary: " + dictionary.hasString(newWord));
                if(!dictionary.hasString(newWord)){
                    return false;
                } else {
                    return notOnSameSide;
                }
            }
        
        } else {
            if(charNum == 0){
                String previousWord = this.words[wordNum - 1];
                lastLetter = lastLetter(previousWord);
                if(!letter.equals(lastLetter)){
                    return false;
                } else {
                    return true;
                }
            } else {
                boolean valid = !(onSameSide(letter, word.substring(charNum - 1, charNum))) && (dictionary.hasString(newWord));
                //System.out.println("3. valid: " + valid);
                boolean notOnSameSide = !onSameSide(letter, word.substring(charNum - 1, charNum));
                //System.out.println("4. notOnSameSide: " + notOnSameSide);
                //System.out.println("5. dictionary: " + dictionary.hasString(newWord));

                if(alreadyUsed(newWord)){
                    return false;
                } else {
                    return valid;
                }
    
            }
            
        }
    }
    
    /*
     * solveRB - the key recursive backtracking method.
     * Handles the process of adding one letter to the word at position
     * wordNum as part of a solution with at most maxWords words.
     * Returns true if a solution has been found, and false otherwise.
     * 
     * Since this is a private helper method, we assume that only 
     * appropriate values will be passed in.
     */
    private boolean solveRB(int wordNum, int charNum, int maxWords) {
        String word = this.words[wordNum];

        if(wordNum >= maxWords){
            return false;
        } 
        if (allLettersUsed() && dictionary.hasFullWord(word) && (word.length() >= 3)){
            printSolution(wordNum);
            return true;
        } else {
            for(int i = 0; i < this.letters.length; i++){
                String letter = this.letters[i];
                
                if (isValid(letter, wordNum, charNum)){
                    addLetter(letter, wordNum);

                    if(solveRB(wordNum, charNum + 1, maxWords)){
                        return true;
                    }

                    String newWord = this.words[wordNum];

                    if((newWord.length()>= 3) && dictionary.hasFullWord(newWord)){
                        if(solveRB(wordNum + 1, 0, maxWords)){
                            return true;
                        }
                    }
                    removeLetter(wordNum);
                }

            } 
        }

        return false;

    }
    
    /*
     * solve - the method that the client calls to solve the puzzle.
     * Serves as a wrapper method for solveRB(), which it repeatedly calls 
     * with a gradually increasing limit for the number of words in the solution.
     */  
    public void solve() {
        int maxWords = 1;
        
        while (maxWords <= MOST_WORDS) {
            System.out.println("Looking for a solution of length " 
                               + maxWords + "...");
            if (this.solveRB(0, 0, maxWords)) {
                return;
            }
            maxWords++;
        }
        
        System.out.println("No solution found using up to " 
                           + MOST_WORDS + " words.");
    }
    
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        
        String[] testSides = {"tae", "nih", "omk", "lys"};
        LetterSquare test = new LetterSquare(testSides);
        test.words[0] = "productive";
        test.words[1] = "l";

        // to test cases involving the second or third word,
        // assign strings to other positions in test.words

        // should get true
        System.out.println(test.isValid("l", 0, 10));

        /* should get false
        System.out.println(test.isValid("a", 0, 4));

        // other tests go here

        // should return false
        System.out.println(test.isValid("l", 1, 2));

        // should return true
        System.out.println(test.isValid("s", 1, 2));

        // exit the program after testing
        //System.exit(0);
         get the sides of the square from the user */
        String[] sides = new String[4];
        String[] prompts = {"top side: ", "left side: ", 
            "right side: ", "bottom side: "};
        for (int i = 0; i < 4; i++) {
            System.out.print(prompts[i]);
            sides[i] = console.nextLine();
        } 
        
        LetterSquare puzzle = new LetterSquare(sides);
        System.out.println("Here is the puzzle:");
        System.out.println(puzzle);
        puzzle.solve(); 

        console.close();
    }
}
