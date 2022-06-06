import java.util.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.nio.file.*;
import java.io.*;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.fxml.*;
import javafx.scene.Parent;
import java.text.DecimalFormat;



public class mainSceneController {

    @FXML MenuBar applicationMenuBar;
    @FXML BorderPane borderPane;
    @FXML TextField loadArea;
    @FXML TextField createArea;

    @FXML private ImageView hangImage;
    @FXML private Label wordLabel ;


    //start
    @FXML TextField indexArea;
    @FXML TextField letterArea;

    @FXML Label numOfPoints;
    @FXML Label numOfAvailableWords;
    @FXML Label percOfCorrectLetters;
    @FXML Label foundLetters;
    @FXML TextArea lettersArea;

    private Parent root;
    public static String randomWord;
    public static int randomWordLength;
    public static int points;
    public static int lives;
    public static int numberOfLettersFound;
    public static HashMap<String, Integer> activeDictionary;
    public static HashMap<String, Integer> initialDictionary;
    public static Set<Integer> foundIndexes = new HashSet<Integer>();
    public static String foundText;
    public static float percentageOf6LetterWords;
    public static float percentageOf7To9LetterWords;
    public static float percentageOfOver10LetterWords;
    public static Character[][] char2dArray; 
    public static int tries;

    public static int numOfAvailableWordsDic;


    public void startHandlerCall(ActionEvent event) throws IOException {
        if(activeDictionary == null) {
            Alert a = new Alert(AlertType.INFORMATION);
            a.setTitle("Load a Dictionary First");
            a.setContentText("Load a Dictionary Before Starting a Game!");
            a.show();
            return;
        }
        randomWord = Methods.getRandomMapKeyElement(activeDictionary);
        randomWordLength = randomWord.length(); 
        numOfAvailableWordsDic = activeDictionary.size() - 1;
        initialDictionary = activeDictionary;
        activeDictionary = Methods.makeFirstDictionary(activeDictionary, randomWord);       
        lives = 6;
        points = 90; 
        numberOfLettersFound = 0;  
        tries = 0; 
        root = FXMLLoader.load(getClass().getResource("fxml-files/start.fxml"));
        Stage stage = (Stage) applicationMenuBar.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void show() {
        renewRecommendedLetters(); // maybe after the loading of the fxml
        numOfAvailableWords.setText(String.valueOf(numOfAvailableWordsDic));
        String underscoresLength = "";
        // System.out.println(foundIndexes);
        for(int i = 0; i < randomWordLength; i++) {
            if(foundIndexes.contains(i)) {
                underscoresLength += randomWord.charAt(i) + " ";
            }
            else underscoresLength += "_ ";
        }
        DecimalFormat df = new DecimalFormat("0.00");
        foundLetters.setText(String.valueOf(underscoresLength));
        float percentage = (float) (foundIndexes.size() * 100.00) / randomWordLength ;
        percOfCorrectLetters.setText(df.format(percentage) + "%");
    }

    public void submitLetter(ActionEvent event) {
        String strIndex = indexArea.getText();
        String strLetter = letterArea.getText();
        int index = Integer.parseInt(strIndex.replaceAll("[\\D]", ""));
        char letter = strLetter.charAt(0);
        if(index >= randomWordLength || index < 0 ) {
            Alert a = new Alert(AlertType.INFORMATION);
            a.setTitle("Wrong Input");
            a.setContentText("Index out of Bounds! Index must be between 0 and " + String.valueOf(randomWordLength - 1));
            a.show();
            return;
        }
        if(foundIndexes.contains(index)) {
            Alert a = new Alert(AlertType.INFORMATION);
            a.setTitle("Wrong Input");
            a.setContentText("You already found this index : " + index);
            a.show();
            return;
        }
        
        int letterIndex = -1;
        for(int i = 0; i < char2dArray[index].length; i++) {
            if(char2dArray[index][i] == letter) {
                letterIndex = i;
            }
        }
       
        if(letterIndex == -1) {
            Alert a = new Alert(AlertType.INFORMATION);
            a.setTitle("Wrong Input");
            a.setContentText("This Letter doesn't exist!");
            a.show();
            return;
        }
        else {
            submitHandler(index, letterIndex);
        }
    }

    public void submitHandler(int index, int letter) {

        tries++;
        if(char2dArray[index][letter] == randomWord.charAt(index)) {
            foundIndexes.add(index);
            if(foundIndexes.size() == randomWordLength) {
                gameWon();
                return;
            }
            float probability = Methods.getCharProbability(activeDictionary, index, randomWord.charAt(index));
            activeDictionary = Methods.renewActiveDictionaryCorrect(activeDictionary, randomWord.charAt(index), index);
            points += Methods.points(probability);
            System.out.println("You got " + Methods.points(probability) + " points");
            show();
        }
        else {
            lives--;
            String imgPath = "images/" + lives + ".png";
            Image image = new Image(imgPath);
            hangImage.setImage(image);
            points -= 15;
            activeDictionary = Methods.renewActiveDictionaryFalse(activeDictionary, char2dArray[index][letter], index);
            System.out.println("WRONG!!You have another " + lives + " chances");
            if(lives == 0) {
                gameLost();
                return;
            }
            show();
        }
        numOfPoints.setText(String.valueOf(points));
        renewRecommendedLetters();
        // System.out.println();
    }
    
    public void roundsHandler(ActionEvent e) {
        try {
            Path path = Paths.get("src/medialab/previous-games.txt");
            String content = Files.readString(path);
            String[] lines = content.split("\\r?\\n");
            String output = "";
            // System.out.println(lines.length);
            if(lines.length >= 5) {
                for(int i = lines.length - 5; i < lines.length; i++) {
                    output += lines[i] + "\n";
                }
            }
            else {
                for(int i = 0; i < lines.length; i++) {
                    output += lines[i] + "\n";
                }
            }
            Alert a = new Alert(AlertType.INFORMATION);
            a.setTitle("5 Previous Games");
            a.setContentText(output);
            a.show();
        } catch (Exception exc) {
            System.out.println(exc);
        }
    }

    public void solutionHandler(ActionEvent e) {
        if(activeDictionary == null) {
            Alert a = new Alert(AlertType.INFORMATION);
            a.setTitle("Load a Dictionary First");
            a.setContentText("Load a Dictionary Before Click Details > Solution!");
            a.show();
            return;
        }
        gameLost();
        try {
            startHandlerCall(e);
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }
    
    public void renewRecommendedLetters() {
        // show the new Recommended Letters after the submission of the letter
        lettersArea.clear();
        char2dArray = Methods.letters(randomWordLength,activeDictionary);
        // Methods.print2dArray(char2dArray, randomWordLength, foundIndexes);
        String recommendedLetters = "";
        for(int i = 0; i < randomWordLength; i++) {
            if(foundIndexes.contains(i)) continue;
            recommendedLetters += "Index " + i + " : ";
            for(int j = 0; j < char2dArray[i].length; j++) {
                recommendedLetters += char2dArray[i][j] + " ";
            }
            recommendedLetters += "\n";
        }
        lettersArea.appendText(recommendedLetters);
        recommendedLetters = "";
    }
    
    public void gameLost() {
        try {
            String thisGame = randomWord + " " + String.valueOf(tries) + " PC\n";
            File previousGames = new File("src/medialab/previous-games.txt");
            FileWriter fr = new FileWriter(previousGames, true);
            fr.write(thisGame);
            fr.close();
            Alert a = new Alert(AlertType.INFORMATION);
            a.setTitle("You LOST");
            a.setContentText("You LOST!!! The secret word was : " + randomWord);
            a.show();
            return;
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
    
    public void gameWon() {
        try {
            System.out.println("YOU WON!");
            String thisGame = randomWord + " " + String.valueOf(tries) + " User\n";
            File previousGames = new File("src/medialab/previous-games.txt");
            FileWriter fr = new FileWriter(previousGames, true);
            fr.write(thisGame);
            fr.close();
            Alert a = new Alert(AlertType.INFORMATION);
            a.setTitle("You Won");
            a.setContentText("You WON!!! with " + points + " points");
            a.show();
            return;
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
    
    public void createHandler(String dictionaryID) {
        try
		{
            String inline = "";

			URL url = new URL("https://openlibrary.org/works/" + dictionaryID + ".json");
			//Parse URL into HttpURLConnection in order to open the connection in order to get the JSON data
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			//Set the request to GET or POST as per the requirements
			conn.setRequestMethod("GET");
			//Use the connect method to create the connection bridge
			conn.connect();
			//Get the response status of the Rest API
			int responsecode = conn.getResponseCode();
			// System.out.println("Response code is: " +responsecode);
			
			//Iterating condition to if response code is not 200 then throw a runtime exception
			//else continue the actual process of getting the JSON data
			if(responsecode != 200)
				throw new RuntimeException("HttpResponseCode: " +responsecode);
			else
			{
				Scanner sc = new Scanner(url.openStream());
				while(sc.hasNext())
				{
					inline+=sc.nextLine();
				}
				sc.close();
			}
			Object obj = JSONValue.parse(inline);
			JSONObject jsonObj = (JSONObject)obj;
			JSONObject descrObj = (JSONObject)jsonObj.get("description");
            // String value = jsonObj.get("description").toString();
			String value = descrObj.get("value").toString();
			// System.out.println("this is the value : " + value);
			String[] words = value.split("\\s+");
			for (int i = 0; i < words.length; i++) {
				words[i] = words[i].replaceAll("[^\\w]", "");
			}
			
			HashMap<String, Integer> map = Methods.makeHashMap(words);

			boolean s = Methods.checkIfDictionaryIsValid(map);
			// System.out.println(s);

            List<String> lines = Arrays.asList(words);
			Path file = Paths.get("src/medialab/hangman_"+ dictionaryID +".txt");
			Files.write(file, lines);
            File file_storage = new File("src/medialab/hangman.txt");
            FileWriter fr = new FileWriter(file_storage, true);
            fr.write(dictionaryID);
            fr.close();
            conn.disconnect();
        } 
        catch(Exception e)
		{
			e.printStackTrace();
		}

    }

    public void loadHandler(String dictionaryID) {
        // prepei na parw to keimeno toy arxeioy kai na to kanw ikanopoiitiko string
        try {
            Path path = Paths.get("src/medialab/hangman_" + dictionaryID + ".txt");
            String content = Files.readString(path);
            String[] lines = content.split("\\r?\\n");
            int i = 0;
            String[] str = new String[lines.length];
            for (String line : lines) {
                str[i++] = line; 
            }
            activeDictionary = Methods.makeHashMap(str);
            System.out.println("We loaded a dictionary with this dictionary ID : " + dictionaryID);
        }
        catch(FileNotFoundException e ) {
            System.out.println("This Dictionary ID doesn't exist. Please Create it!");
        }
        catch(IOException e) {
            System.out.println("We have an IO Exception");
        }
    }

    public void dicHandlerCall(ActionEvent e) throws IOException {
        int numberOf6LetterWords = 0;
        int numberOf7To9LetterWords = 0;
        int numberOfOver10LetterWords = 0;

        if(initialDictionary == null) {
            Alert a = new Alert(AlertType.INFORMATION);
            a.setTitle("Dictionary Details");
            a.setContentText("Please load a Dictionary first!");
            a.show();
            return;
        }

        for (String name : initialDictionary.keySet()) {
			if(name.length() == 6) numberOf6LetterWords++;
            else if(name.length() >= 7 && name.length() <= 9) numberOf7To9LetterWords++;
            else if(name.length() >= 10) numberOfOver10LetterWords++;
		}
        int dicLength = initialDictionary.size();
        percentageOf6LetterWords = (float) (numberOf6LetterWords * 100) / dicLength;
        percentageOf7To9LetterWords = (float) (numberOf7To9LetterWords * 100)/ dicLength;
        percentageOfOver10LetterWords = (float) (numberOfOver10LetterWords * 100) / dicLength;

        DecimalFormat df = new DecimalFormat("0.00");

        Alert a = new Alert(AlertType.INFORMATION);
        a.setTitle("Dictionary Details");
        String outputText = "The Percentage of Words with Over 6 letters is " + df.format(percentageOf6LetterWords) + "\n";
        outputText += "The Percentage of Words with Over or Equal 6 letters and Less or Equal than 9 is " + df.format(percentageOf7To9LetterWords) + "\n";
        outputText += "The Percentage of Words with Over or Equal 10 is " + df.format(percentageOfOver10LetterWords) + "\n";
        a.setContentText(outputText);
        a.show();
    }
    
    public void loadHandlerCall(ActionEvent event) throws IOException {
        // System.out.println("loaded");
        root = FXMLLoader.load(getClass().getResource("fxml-files/load.fxml"));
        Stage stage = (Stage) applicationMenuBar.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();    
    }
    
    public void createHandlerCall(ActionEvent event) throws IOException {
        // System.out.println("created");
        root = FXMLLoader.load(getClass().getResource("fxml-files/create.fxml"));
        Stage stage = (Stage) applicationMenuBar.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();    
    }

    public void submitDictionaryID(ActionEvent event) {
        String dictionaryID = loadArea.getText();
        loadHandler(dictionaryID);
    }

    public void createDictionaryID(ActionEvent event) {
        String dictionaryID = createArea.getText();
        // System.out.println(dictionaryID);
        createHandler(dictionaryID);
    }  
    
    public void exitHandler(ActionEvent event) {
        System.out.println("Program was Exited by the User");
        System.exit(0);
    }  
    
}   
