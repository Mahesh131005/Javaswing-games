import javax.swing.*;  
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
public class PinBall1 {
    // Window dimensions
    private final int WIDTH = 600;
    private final int HEIGHT = 400;

    // Game parameters
    private Color ballColor = Color.RED;
    private int ballDiameter = 20;
    private int numberOfBars = 5;
    private int barWidth = 100;
    private int barHeight = 20;
    private int barSpeed = 2;

    // Main frame and panels
    private  JFrame frame;
    private JPanel setupPanel, gamePanel;
    private Timer timer;

    // Ball parameters
    private int ballX, ballY;
    private boolean ballDropped = false;
    private boolean gameOver = false;
    private boolean playerWon = false;

    // Random and lists for bars
    private Random random = new Random();
    private ArrayList<Rectangle> bars = new ArrayList<>();
    private ArrayList<Integer> barSpeeds = new ArrayList<>();
    // Main entry point
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> new PinBall1().createSetupScreen());
    }

    // Create the setup screen
    private void createSetupScreen() {
        frame = new JFrame("Pinball Game Setup");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,400);
        setupPanel = new CustomizationScreen(frame);
        frame.add(setupPanel);
        frame.pack();
        frame.setVisible(true);
    }

    // Create the game screen
    private void createGameScreen() {
        frame.getContentPane().removeAll();
        frame.repaint();
        gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.pack();
        frame.setVisible(true);
        startGame();
    }

    // Start the game
    private void startGame() {
        ballX = WIDTH / 2;
        ballY = 0;
        ballDropped = false;
        gameOver = false;
        playerWon = false;

        // Initialize bars with random positions and speeds
        bars.clear();
        barSpeeds.clear();
        for (int i = 0; i < numberOfBars; i++) {
            bars.add(new Rectangle(random.nextInt(WIDTH - barWidth), 300 + i * 30, barWidth, barHeight));
            barSpeeds.add(barSpeed * (random.nextBoolean() ? 1 : -1)); // Random speed direction
        }

        // Timer with bar and ball movement logic
        timer = new Timer(20, e -> {
            moveBars();
            moveBall();
            gamePanel.repaint();
        });
        timer.start();
    }

    // Method to move the bars
    private void moveBars() {
        for (int i = 0; i < bars.size(); i++) {
            Rectangle bar = bars.get(i);
            bar.x += barSpeeds.get(i);
            if (bar.x <= 0 || bar.x + bar.width >= WIDTH) {
                barSpeeds.set(i, -barSpeeds.get(i)); // Reverse direction at edges
            }
        }
    }

    // Method to move the ball
    private void moveBall() {
        if (ballDropped) {
            ballY += 5;
            if (ballY > HEIGHT) {
                playerWon = true;
                gameOver = true;
                timer.stop();
            }
            for (Rectangle bar : bars) {
                if (bar.intersects(new Rectangle(ballX, ballY, ballDiameter, ballDiameter))) {
                    gameOver = true;
                    timer.stop();
                    break;
                }
            }
        }
    }

    // Inner class for the game panel
    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (gameOver) {
                g.setColor(playerWon ? Color.GREEN : Color.RED);
                g.setFont(new Font("Times", Font.BOLD, 30));
                g.drawString(playerWon ? "You Win!" : "Game Over!", getWidth() / 2 - 70, getHeight() / 2);
                return;
            }

            // Draw the ball if dropped
            if (ballDropped) {
                g.setColor(ballColor);
                g.fillOval(ballX, ballY, ballDiameter, ballDiameter);
            }

            // Draw the bars
            g.setColor(Color.BLUE);
            for (Rectangle bar : bars) {
                g.fillRect(bar.x, bar.y, bar.width, bar.height);
            }
        }

        public GamePanel() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (!ballDropped && !gameOver) {
                        ballX = e.getX();
                        ballY = 0;
                        ballDropped = true;
                    }
                }
            });
        }
    }

    // Customization screen panel
     class CustomizationScreen extends JPanel {
        private JFrame frame;
        private JTextField ballSizeField, barWidthField, barHeightField, barSpeedField, numberOfBarsField;
        private JComboBox<String> colorComboBox;

        public CustomizationScreen(JFrame frame) {
            this.frame = frame;
            setLayout(new GridLayout(6, 2, 10, 10));

            add(new JLabel("Ball Size:"));
            ballSizeField = new JTextField("20");
            add(ballSizeField);

            add(new JLabel("Ball Color:"));
            String[] colors = {"Red", "Green", "Blue"};
            colorComboBox = new JComboBox<>(colors);
            add(colorComboBox);

            add(new JLabel("Number of Bars:"));
            numberOfBarsField = new JTextField("5");
            add(numberOfBarsField);

            add(new JLabel("Bar Width:"));
            barWidthField = new JTextField("100");
            add(barWidthField);

            add(new JLabel("Bar Height:"));
            barHeightField = new JTextField("20");
            add(barHeightField);

            add(new JLabel("Bar Speed:"));
            barSpeedField = new JTextField("2");
            add(barSpeedField);

            JButton startButton = new JButton("Start Game");
            startButton.addActionListener(e -> startGame());
            add(startButton);
        }

        private void startGame() {
            try {
                int ballSize = Integer.parseInt(ballSizeField.getText());
                int numberOfBars = Integer.parseInt(numberOfBarsField.getText());
                int barWidth = Integer.parseInt(barWidthField.getText());
                int barHeight = Integer.parseInt(barHeightField.getText());
                int barSpeed = Integer.parseInt(barSpeedField.getText());
                Color ballColor;

                // Set ball color based on selected option
                String selectedColor = (String) colorComboBox.getSelectedItem();
                switch (selectedColor) {
                    case "Green" -> ballColor = Color.GREEN;
                    case "Blue" -> ballColor = Color.BLUE;
                    default -> ballColor = Color.RED;
                }

                // Use the provided values to customize game parameters
                PinBall1.this.ballDiameter = ballSize;
                PinBall1.this.numberOfBars = numberOfBars;
                PinBall1.this.barWidth = barWidth;
                PinBall1.this.barHeight = barHeight;
                PinBall1.this.barSpeed = barSpeed;
                PinBall1.this.ballColor = ballColor;

                // Start the game screen
                PinBall1.this.createGameScreen();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
