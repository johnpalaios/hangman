import java.util.*;

class InvalidRangeException extends Exception {}
class UnbalancedException extends Exception {}
public class Methods {

    /**
     * Takes a String Array as a parameter 
     * (which in our app contains the book description of the json data - the value field)
     * This string is supposed to have all the words on different lines.
     * This method creates a HashMap with -
     * Key : The String Object of the String Array, words[i] for example. 
     *         Every word is contained only 1 time (we avoid duplicates),
     *         all the letters of the word are modified to Uppercase and 
     *         only the words with more or equal than 6 letters are putted in the HashMap.
     * Value : The times the value (the word) is appeared in the String[]   
     * @param words
     * @return
     */
    public static HashMap<String, Integer> makeHashMap(String[] words) {
        // δημιουργω ενα 
		// HashMap<Λέξη(μεγαλυτερη ή ίση απο 6 γραμματα), 
		//  	   συχνοτητα εμφανισης της στο λεξικό>
		// Έτσι κάνω και τον έλεγχο για τον αριθμό των γραμμάτων των λέξεων
        HashMap<String, Integer> map = new HashMap<>();
        for(int i = 0; i < words.length; i++) {
            String upperCaseWord = words[i].toUpperCase();
            if(words[i].length() > 5) {
                if(map.containsKey(upperCaseWord)) {
                    int count = map.get(upperCaseWord);
                    map.put(upperCaseWord, ++count);
                }
                else {
                    map.put(upperCaseWord, 1);
                }
            }
        }
        return map;
    }
    
    /**
     * Checks If the given HashMap is valid for a Dictionary.
     * For a dictionary to be valid must have at least 20 words and 
     * 20% of the words must be 9 or more letters.
     * (there are also other criteria for a Dictionary to be valid)
     * @param map
     * @return True if the dictionary is valid ,false if its not
     * @throws InvalidRangeException
     * @throws UnbalancedException
     */
    public static boolean checkIfDictionaryIsValid(HashMap<String,Integer> map) throws InvalidRangeException, UnbalancedException{
        // Ένα λεξικό θα πρέπει να περιλαμβάνει τουλάχιστον 20 υποψήφιες λέξεις. 
        if(map.size() < 20) {
            throw new InvalidRangeException();
        }
        // Τουλάχιστον 20% των λέξεων θα πρέπει να αποτελούνται από 9 ή περισσότερα
        // γράμματα.
        int overOrEqual9 = 0; 
        for (String name : map.keySet()) {
            if(name.length() >= 9) {
                overOrEqual9++;
                // System.out.println(name);
            }
        }
        // System.out.println("this is over or equal " + overOrEqual9);
        // System.out.println(map.size());
        if(overOrEqual9 < 0.2 * map.size()) {
            throw new UnbalancedException();
        }
        return true;
    }

    /**
     * Prints a hashmap with String as a Key and Integer as a Value
     * @param map The map you want to print 
     */
    public static void printHashMap(HashMap<String, Integer> map) {
        for (String name : map.keySet()) {
            String key = name.toString();
            int mapValue = map.get(name);
            System.out.println(key + " " + mapValue);
        }
    }

    /**
     * Prints a hashmap with Character as key and Integer as value
     * (I didn't do method overload with these two methods : printHashMap, printHashMap2
     *  because some weird bugs were appearing)
     * @param map The map you want to print
     */
    public static void printHashMap2(HashMap<Character, Integer> map) {
        for (Character name : map.keySet()) {
            int mapValue = map.get(name);
            System.out.println(name + " " + mapValue);
            
        }
    }

    /**
     * Insert a HashMap with a String as the Key and Integer as a Value
     * and returns a random key(String) from the HashMap
     * @param map The map you want to get the random key from
     * @return The random key(String)
     */
    public static String getRandomMapKeyElement(HashMap<String, Integer> map) {
        try {
        Set<String> keySet = map.keySet();
        List<String> keyList = new ArrayList<>(keySet);

        int size = keyList.size();
        int randIdx = new Random().nextInt(size);

        String randomKey = keyList.get(randIdx);
        // Integer randomValue = map.get(randomKey);

        return randomKey;
        }
        catch (Exception e) {
            System.out.println("You must first load the file");
            return "WRONGWORDTRYAGAIN";
        }
    }

    /**
     * Sort a HashMap with Character as Value and Integer as Value,By the Integer Value 
     * and stores the sorted values in a Character Array (in descending order)
     * @param hm The hashmap you want to sort
     * @return The sorted values of the hashmap in a character array (in descending order)
     */
    public static Character[] sortByValue(HashMap<Character, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<Character, Integer> > list =
                new LinkedList<>(hm.entrySet());

        // Sort the list
        list.sort(Comparator.comparing(Map.Entry::getValue));

        // I make the stack in order to sort the elements in the
        // Character Array that will be returned from sortByValue()
        Character[] charArray = new Character[list.size()];

        Stack <Character> stackBiggerToSmaller = new Stack<>(); 
        for (Map.Entry<Character, Integer> element : list) {
            stackBiggerToSmaller.push(element.getKey());
        }
        int i = 0;
        while(!stackBiggerToSmaller.empty()) {
            charArray[i++] = stackBiggerToSmaller.pop();
        }
        return charArray;
    
    }

    /**
     * Gets a Character[][] (a 2-dimensional Character Array),
     *      the length of the Character Array
     *      and a Set<Integer> of the already found indexes of the word (in our game)
     * and prints the Two Dimensional Array of the input but except the lines i that 
     * are inside the Set<Integer>
     * For example : If the int i is contained in the Set<Integer> then Character[i] 
     *               doesn't get printed. But if not it gets.
     * @param arr
     * @param length
     * @param foundIndexes
     */
    public static void print2dArray(Character[][] arr, int length, Set<Integer> foundIndexes) {
        for(int i = 0; i < length; i++) {
            if(foundIndexes.contains(i)) {continue;}
            System.out.print(" INDEX : " + i + " // ");
            for(int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Takes an Integer "length" and a HashMap<String, Integer> 
     * and makes a 2 Dimensional Character Array
     * which has "int length" lines and 
     * 24 columns (as many as the letters of the latin alphabet) 
     * and contains for every line (which represents the index)
     * the times a character appeared in this Index(number of line 0..length) 
     * in the words of the HashMap that is given
     * @param length
     * @param activeDictionary
     * @return
     */    
    public static Character[][] letters(int length, HashMap<String, Integer> activeDictionary) {
        
        Character[][] char2dArray = new Character[length][24];

        for(int i =0; i < length; i++) {

            HashMap<Character, Integer> timesOfCharAppearance = new HashMap<>();

            for (String name : activeDictionary.keySet()) {

                Character ourChar = name.charAt(i);
                
                if(timesOfCharAppearance.containsKey(ourChar)) {
                
                    int times = timesOfCharAppearance.get(ourChar);
                    // if the word is contained x times in the HashMap then
                    // the times the Char appeared get increased by x
                    int numberOfTheTimesAWordApppeared = activeDictionary.get(name);
                    timesOfCharAppearance.put(ourChar,times + numberOfTheTimesAWordApppeared);
                
                }
                else {
                    timesOfCharAppearance.put(ourChar, 1);
                }
            
            }

            char2dArray[i] = Methods.sortByValue(timesOfCharAppearance);
        
        }

        return char2dArray;
    }

    /**
     * This method takes a HashMap<String, Integer> 
     * (all the words in the description of the book, in our case)
     * and makes a HashMap<String, Integer> with
     * Key : A String of the HashMap parameter that has
     *       the same length as the randomWord Parameter
     * Value : The times the Key appeared in the HashMap parameter 
     * @param map
     * @param randomWord
     * @return
     */
    public static HashMap<String, Integer> makeFirstDictionary(HashMap<String, Integer> map, String randomWord) {
            
        HashMap<String, Integer> activeDictionary = new HashMap<>();
        Integer randomWordLength = randomWord.length();
        // System.out.println("This is the Random Word : " + randomWord);
        
        for (String name : map.keySet()) {
            
            if(name.length() == randomWordLength) {
                
                // System.out.println("We are putting the word " + name);
                activeDictionary.put(name, map.get(name));
            
            }

        }

        return activeDictionary;
    }

    /**
     * This method gets called when a CORRECT choice of letter is done in our game.
     * It takes as parameters :
     * HashMap<String, Integer>  @param oldActiveDictionary : the old Dictionary we had in the previous round
     * Character @param charFound : the character found
     * Index @param index : in what index the character is in
     * It returns a new HashMap<String, Index> that contain only the words of the 
     * @param oldActiveDictionary who have the  @param charFound in the @param index
     * 
     * @param oldActiveDictionary
     * @param charFound
     * @param index
     * @return
     */
    public static HashMap<String, Integer> renewActiveDictionaryCorrect(HashMap<String, Integer> oldActiveDictionary
                                                                        , Character charFound, int index) {
        
        HashMap<String, Integer> activeDictionary = new HashMap<>();
		
        for (String name : oldActiveDictionary.keySet()) {
			
            if(name.charAt(index) == charFound) {
				
                // System.out.println("We are putting the word " + name);
				activeDictionary.put(name, oldActiveDictionary.get(name));
			
            }

		}

        return activeDictionary;
    }

    /**
     * This method gets called when a FALSE choice of letter is done in our game.
     * It takes as parameters :
     * HashMap<String, Integer>  @param oldActiveDictionary : the old Dictionary we had in the previous round
     * Character @param charFound : the character found
     * Index @param index : in what index the character is in
     * It returns a new HashMap<String, Index> that contain only the words of the 
     * @param oldActiveDictionary who DOES NOT have the  @param charFound in the @param index
     * 
     * @param oldActiveDictionary
     * @param charFound
     * @param index
     * @return
     */
    public static HashMap<String, Integer> renewActiveDictionaryFalse(HashMap<String, Integer> oldActiveDictionary, Character charFound, int index) {
        
        HashMap<String, Integer> activeDictionary = new HashMap<>();
		
        for (String name : oldActiveDictionary.keySet()) {
			
            if(name.charAt(index) != charFound) {
				
                activeDictionary.put(name, oldActiveDictionary.get(name));
			
            }
            else {
                // System.out.println("We are excluding the word " + name);
            }

		}

        return activeDictionary;
    }

    /**
     * This method returns a float which represent the probability 
     * of a @param charFound in one @param index at the @param activeDictionary
     * The float it returns is like this : "XXX.YY"
     * with 2 decimal places
     * @param activeDictionary
     * @param index
     * @param charFound
     * @return
     */
    public static float getCharProbability(HashMap<String, Integer> activeDictionary, int index, Character charFound) {
        int counter = 0;
       
        for (String name : activeDictionary.keySet()) {
			
            if(name.charAt(index) == charFound) {
				counter++;
			}

		}
        
        float probability = (float) counter / activeDictionary.size();
        // System.out.println("counter : " + counter + "activeDictionary.size() : " + activeDictionary.size() + " probability : " + probability);
        
        return probability ;
    }
    
    /**
     * This method returns an Integer depending
     * on @param probability value.
     * If probability is :
     *                      >= 0.6 -> @return 5
     *                      >= 0.4 -> @return 10
     *                      >= 0.25 -> @return 15
     *                      else -> @return 30
     * @param probability
     * @return
     */
    public static int points(float probability) {
        
        int extraPoints;
        if(probability >= 0.6) {
            extraPoints = 5;
        }
        else if(probability >= 0.4) {
            extraPoints = 10;
        }
        else if(probability >= 0.25) {
            extraPoints = 15;
        }
        else {
            extraPoints = 30;
        }
        return extraPoints;
    }
}
