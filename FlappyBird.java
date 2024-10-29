import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 1000, HEIGHT = 900;  // Increased window size
    private int birdY = HEIGHT / 2 - 20, birdVelocity = 0, gravity = 1;
    private boolean gameOver = false, started = false;
    private ArrayList<Rectangle> obstacles = new ArrayList<>();
    private Timer timer;
    private Image birdImage;  // Declare an image for the bird
    private static final int BIRD_WIDTH = 170, BIRD_HEIGHT = 60;  // Increased bird size
    private int score = 0;  // Score variable

    public FlappyBird() {
        timer = new Timer(20, this);
        timer.start();
        addKeyListener(this);
        setFocusable(true);
        addObstacle(true);
        addObstacle(true);
        
        // Load the bird image
        birdImage = new ImageIcon("C:\\Users\\ASUS\\Desktop\\Java\\Flappy Niranjan.png").getImage();
    }

    // Add new obstacles to the screen
    private void addObstacle(boolean start) {
        int space = 300;  // Adjusted space between top and bottom obstacles
        int width = 150;  // Increased obstacle width
        int height = 100 + (int)(Math.random() * 400);  // Adjusted obstacle height range
        
        if (start) {
            obstacles.add(new Rectangle(WIDTH + width + obstacles.size() * 300, HEIGHT - height - 150, width, height));
            obstacles.add(new Rectangle(WIDTH + width + (obstacles.size() - 1) * 300, 0, width, HEIGHT - height - space));
        } else {
            obstacles.add(new Rectangle(obstacles.get(obstacles.size() - 1).x + 600, HEIGHT - height - 150, width, height));
            obstacles.add(new Rectangle(obstacles.get(obstacles.size() - 1).x, 0, width, HEIGHT - height - space));
        }
    }

    // Paint method to draw components
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.cyan);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.orange);
        g.fillRect(0, HEIGHT - 150, WIDTH, 150);

        g.setColor(Color.green);
        g.fillRect(0, HEIGHT - 150, WIDTH, 20);

        // Draw the bird image instead of a red square, with the increased size
        g.drawImage(birdImage, 100, birdY, BIRD_WIDTH, BIRD_HEIGHT, this);

        // Draw the obstacles with borders
        for (Rectangle obstacle : obstacles) {
            g.setColor(Color.green);
            g.fillRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
            g.setColor(Color.black);  // Border color
            g.drawRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);  // Draw border around pipe
        }

        // Draw the scoreboard
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Score: " + score, WIDTH - 300, 50);

        if (gameOver) {
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 100));  // Increased font size
            g.drawString("Game Over", 300, HEIGHT / 2 - 50);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Final Score: " + score, 400, HEIGHT / 2 + 50);  // Show final score on Game Over
        }

        if (!started) {
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 100));  // Increased font size
            g.drawString("Flappy Niranjan", 120, HEIGHT / 2 - 200);
        }
    }

    // ActionListener method to handle game logic
    @Override
    public void actionPerformed(ActionEvent e) {
        if (started) {
            birdVelocity += gravity;
            birdY += birdVelocity;

            for (int i = 0; i < obstacles.size(); i++) {
                Rectangle obstacle = obstacles.get(i);
                obstacle.x -= 10;
            }

            if (birdY > HEIGHT - 150 || birdY < 0) {
                gameOver = true;
            }

            // Check for collision with pipes
            for (Rectangle obstacle : obstacles) {
                if (obstacle.intersects(new Rectangle(100, birdY, BIRD_WIDTH, BIRD_HEIGHT))) {  // Adjusted collision detection
                    gameOver = true;
                }
            }

            // Remove passed obstacles and add new ones, increase score
            if (obstacles.get(0).x + obstacles.get(0).width < 0) {
                obstacles.remove(0);
                obstacles.remove(0);
                addObstacle(false);
                score++;  // Increase score when passing pipes
            }
        }

        repaint();
    }

    // Start game when space is pressed
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!started) {
                started = true;
            } else if (!gameOver) {
                birdVelocity = -10;
            } else {
                // Reset game after Game Over
                birdY = HEIGHT / 2 - 20;
                birdVelocity = 0;
                obstacles.clear();
                addObstacle(true);
                addObstacle(true);
                gameOver = false;
                started = false;
                score = 0;  // Reset score
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        FlappyBird game = new FlappyBird();

        frame.add(game);
        frame.setTitle("Flappy Bird");
        frame.setSize(WIDTH, HEIGHT);  // Set the new window size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
