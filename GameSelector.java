import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class GameSelector {

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Choose Your Game");
        frame.setSize(600, 400); // Larger window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 1, 10, 10)); // Layout with gaps

        // Path to the images
        String flappyBirdImagePath = "C:\\Users\\ASUS\\Desktop\\Java\\FlappyBird.jpg";
        String snakeBoardImagePath = "C:\\Users\\ASUS\\Desktop\\Java\\SnakeBoard.png";
        String ticTacToeImagePath = "C:\\Users\\ASUS\\Desktop\\Java\\TicTacToe.png";

        // Creating buttons with icons
        JButton flappyBirdButton = createGameButton("Flappy Bird", flappyBirdImagePath);
        JButton snakeBoardButton = createGameButton("Snake Board", snakeBoardImagePath);
        JButton ticTacToeButton = createGameButton("Tic Tac Toe", ticTacToeImagePath);

        // Adding ActionListener to buttons
        flappyBirdButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Open a command prompt and run both commands in sequence using cmd /c
                    Runtime.getRuntime().exec("cmd /c javac FlappyBird.java && java FlappyBird");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        

        snakeBoardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Run Main.java (assuming this is the Snake Board main class)
                    Runtime.getRuntime().exec("cmd /c javac Main.java && java Main");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        ticTacToeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Run TicTacToe.java (assuming this is the Tic Tac Toe main class)
                    Runtime.getRuntime().exec("cmd /c javac TicTacToe.java && java TicTacToe");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Adding buttons to the frame
        frame.add(flappyBirdButton);
        frame.add(snakeBoardButton);
        frame.add(ticTacToeButton);

        // Display the frame
        frame.setVisible(true);
    }

    // Helper method to create buttons with icons
    public static JButton createGameButton(String gameName, String iconPath) {
        JButton button = new JButton(gameName);
        try {
            // Load and scale the image icon
            ImageIcon icon = new ImageIcon(iconPath);
            Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("Icon for " + gameName + " not found: " + e.getMessage());
        }
        button.setHorizontalTextPosition(SwingConstants.RIGHT); // Place text to the right of the icon
        return button;
    }
}