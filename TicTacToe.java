import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Random;
import javax.swing.*;

public class TicTacToe extends JFrame implements ActionListener {
    private JButton[][] buttons = new JButton[3][3];
    private JLabel scoreLabel;
    private int playerWins = 0, computerWins = 0, draws = 0;
    private char[][] board = new char[3][3];
    private boolean playerTurn = true;
    private Random random = new Random();
    private int moveCount = 0;
    private static final String FILE_PATH = "game_score.txt";

    public TicTacToe() {
        // Load the score from the file
        loadScoreFromFile();

        // Set up the game window
        setTitle("Tic-Tac-Toe Game with AI");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600); // Larger window size

        // Scoreboard
        scoreLabel = new JLabel("Player: " + playerWins + " - Computer: " + computerWins + " - Draws: " + draws, JLabel.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(scoreLabel, BorderLayout.NORTH);

        // Create board panel
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 80));
                buttons[i][j].addActionListener(this);
                boardPanel.add(buttons[i][j]);
                board[i][j] = '-'; // Empty spaces
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        // Randomize first turn
        playerTurn = random.nextBoolean();
        if (!playerTurn) {
            computerMove();
        }

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
    
        // Player's move
        if (playerTurn) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (clickedButton == buttons[i][j] && board[i][j] == '-') {
                        buttons[i][j].setText("X");
                        buttons[i][j].setEnabled(false); // Disable button after click
                        board[i][j] = 'X';
                        moveCount++;
                        if (checkWin('X')) {
                            playerWins++;
                            showResult("You Won!");
                        } else if (moveCount == 9) {
                            draws++;
                            showResult("Draw!");
                        } else {
                            playerTurn = false; // Switch to computer's turn
                            computerMoveWithDelay(); // Initiate AI move after player's move
                        }
                        return; // Exit after processing the player's move
                    }
                }
            }
        }
    }

    // Computer's move with 1-2 second delay
    // Computer's move with a Swing Timer to create a delay
    private void computerMoveWithDelay() {
        Timer timer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                computerMove(); // Make the computer's move
                // After the computer's move, check for a win
                if (checkWin('O')) {
                    computerWins++;
                    showResult("AeXOmatic Won!");
                } else if (moveCount == 9) {
                    draws++;
                    showResult("Draw!");
                } else {
                    playerTurn = true; // Switch back to player's turn
                }
            }
        });
        timer.setRepeats(false); // Ensure the timer only runs once
        timer.start(); // Start the timer
    }


    // Computer's move (improved AI)
    private void computerMove() {
        if (moveCount == 9) return; // No moves left

        // Step 1: Try to win if possible
        if (tryToWinOrBlock('O')) return;

        // Step 2: Try to block the player if they're about to win
        if (tryToWinOrBlock('X')) return;

        // Step 3: Choose the best available move
        if (board[1][1] == '-') { // Take center if available
            makeMove(1, 1, 'O');
        } else {
            // Step 4: Choose a corner if available
            int[][] corners = {{0, 0}, {0, 2}, {2, 0}, {2, 2}};
            for (int[] corner : corners) {
                if (board[corner[0]][corner[1]] == '-') {
                    makeMove(corner[0], corner[1], 'O');
                    return;
                }
            }

            // Step 5: Choose any remaining available spot
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '-') {
                        makeMove(i, j, 'O');
                        return;
                    }
                }
            }
        }
    }

    // Try to win or block the player
    private boolean tryToWinOrBlock(char symbol) {
        // Check rows, columns, and diagonals for a potential win or block
        for (int i = 0; i < 3; i++) {
            // Check rows
            if (checkTwoInLine(symbol, i, 0, i, 1, i, 2)) return true;
            // Check columns
            if (checkTwoInLine(symbol, 0, i, 1, i, 2, i)) return true;
        }
        // Check diagonals
        if (checkTwoInLine(symbol, 0, 0, 1, 1, 2, 2)) return true;
        if (checkTwoInLine(symbol, 0, 2, 1, 1, 2, 0)) return true;

        return false;
    }

    // Helper function to check if there are two of the same symbol in a line, and make the third move
    private boolean checkTwoInLine(char symbol, int r1, int c1, int r2, int c2, int r3, int c3) {
        if (board[r1][c1] == symbol && board[r2][c2] == symbol && board[r3][c3] == '-') {
            makeMove(r3, c3, 'O');
            return true;
        } else if (board[r1][c1] == symbol && board[r3][c3] == symbol && board[r2][c2] == '-') {
            makeMove(r2, c2, 'O');
            return true;
        } else if (board[r2][c2] == symbol && board[r3][c3] == symbol && board[r1][c1] == '-') {
            makeMove(r1, c1, 'O');
            return true;
        }
        return false;
    }

    // Helper function to make a move
    private void makeMove(int row, int col, char symbol) {
        buttons[row][col].setText(String.valueOf(symbol));
        buttons[row][col].setEnabled(false); // Disable button after move
        board[row][col] = symbol;
        moveCount++;

        // Check for win or draw
        if (checkWin(symbol)) {
            if (symbol == 'O') {
                computerWins++;
                showResult("eXOmatic Won!");
            }
        } else if (moveCount == 9) {
            draws++;
            showResult("Draw!");
        } else {
            playerTurn = (symbol == 'X');
        }
    }

    // Check if a player has won
    private boolean checkWin(char symbol) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) return true;
            if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol) return true;
        }
        if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) return true;
        if (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol) return true;
        return false;
    }

    // Show result dialog and reset the board after a short pause
    private void showResult(String message) {
        updateScoreboard();
        JOptionPane.showMessageDialog(this, message);
        resetBoard();
    }

    // Reset the board after each round
    private void resetBoard() {
        moveCount = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true); // Enable buttons after reset
                board[i][j] = '-';
            }
        }
        playerTurn = random.nextBoolean(); // Randomize the first turn
        if (!playerTurn) {
            computerMoveWithDelay(); // Computer starts first if it's not the player's turn
        }
        saveScoreToFile();
    }

    // Update the scoreboard
    private void updateScoreboard() {
        scoreLabel.setText("Player: " + playerWins + " - Computer: " + computerWins + " - Draws: " + draws);
    }

    // Load score from file
    private void loadScoreFromFile() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine();
                if (line != null) {
                    String[] scores = line.split(",");
                    playerWins = Integer.parseInt(scores[0].trim());
                    computerWins = Integer.parseInt(scores[1].trim());
                    draws = Integer.parseInt(scores[2].trim());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Save score to file
    private void saveScoreToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(playerWins + "," + computerWins + "," + draws);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Main method to run the game
    public static void main(String[] args) {
        new TicTacToe();
    }
}
