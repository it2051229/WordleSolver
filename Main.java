import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;

// The main user interface where user puts in clues and calculates all possible words to the problem
public class Main extends JFrame {

    private ArrayList<WordPane> wordPanes = new ArrayList<>();
    private JList<String> wordsList = new JList<>(new DefaultListModel<>());

    // Initialize the user interface
    public Main() {
        setTitle("Wordle Solver");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        wordsList.setPreferredSize(new Dimension(150, 0));

        JToolBar toolBar = new JToolBar();
        add(BorderLayout.PAGE_START, toolBar);

        // The center panel is where the row of words are added
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Attempts"));
        add(BorderLayout.CENTER, centerPanel);

        // To the left are the word suggestions
        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.setBorder(BorderFactory.createTitledBorder("Word Suggestions"));
        eastPanel.add(BorderLayout.CENTER, new JScrollPane(wordsList));
        add(BorderLayout.EAST, eastPanel);

        // We need an update button to update the word suggestions when we get clues
        JButton updateWordsListButton = new JButton("Update");
        eastPanel.add(BorderLayout.SOUTH, updateWordsListButton);

        // To the top is a button to enter a word and clear the words
        JButton enterWordButton = new JButton("Enter a 5 Letter Word");
        toolBar.add(enterWordButton);

        JButton clearWordsButton = new JButton("Clear Words");
        toolBar.add(clearWordsButton);

        pack();
        setLocationRelativeTo(null);

        // Load the dictionary words and put it on display
        Set<String> dictionaryWords = loadDictionaryWords();
        updateSuggestedWordsList(dictionaryWords);

        // Allow the user to enter a word
        enterWordButton.addActionListener(e -> {
            String word = JOptionPane.showInputDialog(this, "Enter word");

            try {
                WordPane wordPane = new WordPane(word);
                wordPanes.add(wordPane);
                centerPanel.add(wordPane);

                pack();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        // Make the update word list button word to filter all possible words based on clues
        updateWordsListButton.addActionListener(e -> {
            Set<String> suggestedWordsList = new HashSet<>();

            // For each dictionary word, check that it satisfies the word panes
            for (String word : dictionaryWords) {
                boolean wordPassed = true;

                for (WordPane wordPane : wordPanes) {
                    if (!wordPane.satisfiesWord(word)) {
                        wordPassed = false;
                        break;
                    }
                }

                if (wordPassed)
                    suggestedWordsList.add(word);
            }

            // Collect all valid and invalid letters
            Set<Character> validLetters = new HashSet<>();
            Set<Character> invalidLetters = new HashSet<>();

            for (WordPane wordPane : wordPanes) {
                validLetters.addAll(wordPane.getValidLetters());
                invalidLetters.addAll(wordPane.getInvalidLetters());
            }

            // For valid letters that are found in invalid letters, it becomes valid
            invalidLetters.removeAll(validLetters);

            // For each dictionary word, they should have the valid letters and shouldn't have the invalid letters
            Set<String> failedWords = new HashSet<>();

            for (String word : suggestedWordsList) {
                // Check that word has the valid letters
                for (Character validLetter : validLetters) {
                    if (word.indexOf(validLetter) == -1) {
                        failedWords.add(word);
                        break;
                    }
                }

                // Check that word doesn't have the invalid letters
                for(Character invalidLetter : invalidLetters) {
                    if(word.indexOf(invalidLetter) >= 0) {
                        failedWords.add(word);
                        break;
                    }
                }
            }

            // Remove the failed words
            suggestedWordsList.removeAll(failedWords);

            // Display the Result
            updateSuggestedWordsList(suggestedWordsList);
        });

        // Make the clear word panes work by removing the word panes
        clearWordsButton.addActionListener(e -> {
            for (WordPane wordPane : wordPanes)
                centerPanel.remove(wordPane);

            wordPanes.clear();
            updateSuggestedWordsList(dictionaryWords);
            centerPanel.updateUI();
            pack();
        });
    }

    // Display the suggested words to the list
    private void updateSuggestedWordsList(Set<String> suggestedWordsList) {
        DefaultListModel<String> listModel = (DefaultListModel<String>) wordsList.getModel();
        listModel.clear();
        listModel.addAll(suggestedWordsList);
    }

    // Load all 5-letter words from the dictionary
    private Set<String> loadDictionaryWords() {
        try {
            Set<String> dictionaryWords = new HashSet<>();
            Scanner inFile = new Scanner(new File("dictionary.txt"));

            while (inFile.hasNextLine()) {
                String word = inFile.nextLine().toUpperCase();

                if (word.length() == 5)
                    dictionaryWords.add(word);
            }

            inFile.close();

            return dictionaryWords;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load dictionary.txt file.");
            System.exit(0);
        }

        return null;
    }

    // Entry point of the program and start the program
    public static void main(String[] args) {
        new Main().setVisible(true);
    }
}
