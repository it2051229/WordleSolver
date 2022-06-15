import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

// A word consist of sequence of letter panes where a user can input letters
public class WordPane extends JPanel {

    private final int NUM_LETTERS = 5;

    private LetterPane[] letterPanes = new LetterPane[NUM_LETTERS];

    // Initialize a word pane
    public WordPane(String word) {
        // Check that the word is 5 letter in length
        word = word.toUpperCase();

        if(word.length() != NUM_LETTERS)
            throw new IllegalArgumentException("Invalid word.");

        // Check that the word only contains alphabets
        for(int i = 0; i < word.length(); i++)
            if(!Character.isAlphabetic(word.charAt(i)))
                throw new IllegalArgumentException("Invalid word.");

        setLayout(new GridLayout(1, NUM_LETTERS, 1, 1));

        for(int i = 0; i < letterPanes.length; i++) {
            LetterPane letterPane = new LetterPane(word.charAt(i));
            add(letterPane);
            letterPanes[i] = letterPane;
        }
    }

    // Check if the word satisfies the word pane clues
    public boolean satisfiesWord(String inputWord) {
        for(int i = 0; i < inputWord.length(); i++) {
            LetterPane letterPane = letterPanes[i];
            char inputLetter = inputWord.charAt(i);

            if(letterPane.getColor() == Color.GREEN) {
                // A green letter pane must match exactly the input letter
                if(letterPane.getLetter() != inputLetter)
                    return false;
            }

            if(letterPane.getColor() == Color.ORANGE) {
                // An orange color means the letter on it is part of an answer but in the wrong place
                if(letterPane.getLetter() == inputLetter)
                    return false;
            }
        }

        return true;
    }

    // Return all letters that are orange/green
    public Set<Character> getValidLetters() {
        Set<Character> validLetters = new HashSet<>();

        for(LetterPane letterPane : letterPanes)
            if(letterPane.getColor() == Color.GREEN || letterPane.getColor() == Color.ORANGE)
                validLetters.add(letterPane.getLetter());

        return validLetters;
    }

    // Return all invalid letters
    public Set<Character> getInvalidLetters() {
        Set<Character> invalidLetters = new HashSet<>();

        for(LetterPane letterPane : letterPanes)
            if(letterPane.getColor() == Color.WHITE)
                invalidLetters.add(letterPane.getLetter());

        return invalidLetters;
    }
}
