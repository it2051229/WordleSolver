import javax.swing.*;
import java.awt.*;

// This pane is a UI where the player inputs a letter and it can be marked, green or orange
public class LetterPane extends JPanel {

    private JTextField letterField = new JTextField();

    // Initialize the components
    public LetterPane(char letter) {
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, letterField);

        letterField.setHorizontalAlignment(JTextField.CENTER);
        letterField.setText(String.valueOf(letter));
        letterField.setEditable(false);
        letterField.setBackground(Color.WHITE);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        add(BorderLayout.SOUTH, buttonsPanel);

        JButton greenButton = new JButton("G");
        buttonsPanel.add(greenButton);
        greenButton.addActionListener(e -> {
            letterField.setBackground(Color.GREEN);
        });

        JButton orangeButton = new JButton("O");
        buttonsPanel.add(orangeButton);
        orangeButton.addActionListener(e -> {
            letterField.setBackground(Color.ORANGE);
        });

        JButton clearButton = new JButton("C");
        buttonsPanel.add(clearButton);
        clearButton.addActionListener(e ->{
            letterField.setBackground(Color.WHITE);
        });
    }

    // Return the letter
    public char getLetter() {
        return letterField.getText().charAt(0);
    }

    // Return the color
    public Color getColor() {
        return letterField.getBackground();
    }
}
