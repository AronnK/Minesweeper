import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Minesweeper extends JFrame {
    private JButton[][] buttons = new JButton[GameConstants.GRID_SIZE][GameConstants.GRID_SIZE];
    private GameBoard gameBoard;
    private ImageIcon bombIcon;

    // UI components
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel homePanel;
    private JPanel gamePanel;
    private JPanel gridPanel;
    private JPanel headerPanel;
    private JLabel timerLabel;
    private JLabel difficultyLabel;
    private javax.swing.Timer gameTimer;
    private long startTime;
    private String currentDifficulty;

    public Minesweeper() {
        gameBoard = new GameBoard();
        bombIcon = new ImageIcon(GameConstants.BOMB_ICON_PATH);
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Minesweeper");
        setSize(GameConstants.WINDOW_SIZE, GameConstants.WINDOW_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);

        createHomeScreen();
        createGameScreen();

        setVisible(true);
    }

    // Home screen with three difficulty options
    private void createHomeScreen() {
        homePanel = new JPanel();
        homePanel.setLayout(new GridBagLayout());
        homePanel.setBackground(new Color(240, 240, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Add game logo/title
        JLabel titleLabel = new JLabel("MINESWEEPER");
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 32));
        titleLabel.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        homePanel.add(titleLabel, gbc);

        // Style difficulty buttons
        JButton[] difficultyButtons = {
                new JButton("Easy") {
                    {
                        setBackground(new Color(46, 204, 113));
                        setForeground(Color.WHITE);
                        setFont(new Font("Arial", Font.BOLD, 20));
                        setFocusPainted(false);
                        setBorderPainted(false);
                        setPreferredSize(new Dimension(200, 50));
                        addActionListener(e -> startGame("Easy"));
                        addMouseListener(new MouseAdapter() {
                            public void mouseEntered(MouseEvent e) {
                                setBackground(new Color(82, 220, 140));
                            }
                            public void mouseExited(MouseEvent e) {
                                setBackground(new Color(46, 204, 113));
                            }
                        });
                    }
                },
                new JButton("Medium") {
                    {
                        setBackground(new Color(52, 152, 219));
                        setForeground(Color.WHITE);
                        setFont(new Font("Arial", Font.BOLD, 20));
                        setFocusPainted(false);
                        setBorderPainted(false);
                        setPreferredSize(new Dimension(200, 50));
                        addActionListener(e -> startGame("Medium"));
                        addMouseListener(new MouseAdapter() {
                            public void mouseEntered(MouseEvent e) {
                                setBackground(new Color(82, 172, 229));
                            }
                            public void mouseExited(MouseEvent e) {
                                setBackground(new Color(52, 152, 219));
                            }
                        });
                    }
                },
                new JButton("Good Luck Winning") {
                    {
                        setBackground(new Color(231, 76, 60));
                        setForeground(Color.WHITE);
                        setFont(new Font("Arial", Font.BOLD, 20));
                        setFocusPainted(false);
                        setBorderPainted(false);
                        setPreferredSize(new Dimension(200, 50));
                        addActionListener(e -> startGame("Hard"));
                        addMouseListener(new MouseAdapter() {
                            public void mouseEntered(MouseEvent e) {
                                setBackground(new Color(241, 96, 80));
                            }
                            public void mouseExited(MouseEvent e) {
                                setBackground(new Color(231, 76, 60));
                            }
                        });
                    }
                }
        };

        for (int i = 0; i < difficultyButtons.length; i++) {
            gbc.gridy = i + 1;
            gbc.insets = new Insets(10, 0, 10, 0);  // Reduced horizontal padding
            homePanel.add(difficultyButtons[i], gbc);
        }

        mainPanel.add(homePanel, "Home");
    }

    // Game screen with a header (timer on left, difficulty on right) above the grid
    private void createGameScreen() {
        gamePanel = new JPanel(new BorderLayout());

        // Header panel with timer and difficulty level labels
        headerPanel = new JPanel(new BorderLayout());
        timerLabel = new JLabel("Timer: 0");
        difficultyLabel = new JLabel("Difficulty Level: ");
        headerPanel.add(timerLabel, BorderLayout.WEST);
        headerPanel.add(difficultyLabel, BorderLayout.EAST);
        gamePanel.add(headerPanel, BorderLayout.NORTH);

        // Grid panel containing the game tiles
        gridPanel = new JPanel(new GridLayout(GameConstants.GRID_SIZE, GameConstants.GRID_SIZE));
        gamePanel.add(gridPanel, BorderLayout.CENTER);

        mainPanel.add(gamePanel, "Game");
    }

    // Starts a new game with the selected difficulty level
    private void startGame(String difficulty) {
        currentDifficulty = difficulty;
        difficultyLabel.setText("Difficulty Level: " + difficulty);

        gameBoard = new GameBoard();
        gameBoard.initializeBoard(difficulty);

        gridPanel.removeAll();
        initializeGameGrid();
        gridPanel.revalidate();
        gridPanel.repaint();

        startTime = System.currentTimeMillis();
        if (gameTimer != null) {
            gameTimer.stop();
        }
        gameTimer = new Timer(1000, e -> updateTimer());
        gameTimer.start();

        cardLayout.show(mainPanel, "Game");
    }

    private void initializeGameGrid() {
        for (int row = 0; row < GameConstants.GRID_SIZE; row++) {
            for (int col = 0; col < GameConstants.GRID_SIZE; col++) {
                buttons[row][col] = new JButton();
                styleButton(buttons[row][col]);
                buttons[row][col].addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        handleCellClick(e);
                    }
                });
                gridPanel.add(buttons[row][col]);
            }
        }
    }

    private void handleCellClick(MouseEvent e) {
        if (gameBoard.isGameOver())
            return;
        JButton clicked = (JButton) e.getSource();
        int[] pos = findButtonPosition(clicked);
        if (pos == null)
            return;

        if (SwingUtilities.isRightMouseButton(e)) {
            toggleFlag(pos[0], pos[1]);
        } else {
            revealCell(pos[0], pos[1]);
        }
    }

    private void updateTimer() {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        timerLabel.setText("Timer: " + elapsed);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 14));
        button.setBackground(new Color(240, 240, 240));
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        button.setFocusPainted(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setPreferredSize(new Dimension(40, 40));
    }

    private int[] findButtonPosition(JButton button) {
        for (int i = 0; i < GameConstants.GRID_SIZE; i++) {
            for (int j = 0; j < GameConstants.GRID_SIZE; j++) {
                if (buttons[i][j] == button) {
                    return new int[] { i, j };
                }
            }
        }
        return null;
    }

    private void revealAllMines() {
        for (int i = 0; i < GameConstants.GRID_SIZE; i++) {
            for (int j = 0; j < GameConstants.GRID_SIZE; j++) {
                if (gameBoard.isMine(i, j) && !gameBoard.isFlagged(i, j)) {
                    buttons[i][j].setBackground(Color.GREEN);
                    buttons[i][j].setIcon(new ImageIcon(bombIcon.getImage().getScaledInstance(
                            buttons[i][j].getWidth(),
                            buttons[i][j].getHeight(),
                            Image.SCALE_SMOOTH)));
                }
            }
        }
    }

    private void revealCell(int row, int col) {
        if (row < 0 || row >= GameConstants.GRID_SIZE || col < 0 || col >= GameConstants.GRID_SIZE
                || gameBoard.isRevealed(row, col) || gameBoard.isFlagged(row, col))
            return;

        gameBoard.setRevealed(row, col);
        buttons[row][col].setEnabled(false);
        buttons[row][col].setBackground(new Color(200, 200, 200));

        if (gameBoard.isMine(row, col)) {
            buttons[row][col].setIcon(new ImageIcon(bombIcon.getImage().getScaledInstance(
                    buttons[row][col].getWidth(),
                    buttons[row][col].getHeight(),
                    Image.SCALE_SMOOTH)));
            gameOver();
            return;
        }

        int count = gameBoard.countAdjacentMines(row, col);
        if (count > 0) {
            buttons[row][col].setText("<html><font color='" + getHexColor(count) + "'>" + count + "</font></html>");
        } else {
            buttons[row][col].setText("");
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    revealCell(row + dr, col + dc);
                }
            }
        }
        checkGameWin();
    }

    private String getHexColor(int count) {
        return switch (count) {
            case 1 -> "#0000FF"; // Blue
            case 2 -> "#008000"; // Green
            case 3 -> "#FF0000"; // Red
            case 4 -> "#800080"; // Purple
            case 5 -> "#FFA500"; // Orange
            default -> "#000000"; // Black
        };
    }

    private void toggleFlag(int row, int col) {
        if (gameBoard.isRevealed(row, col))
            return;
        gameBoard.toggleFlag(row, col);
        buttons[row][col].setText(gameBoard.isFlagged(row, col) ? "M" : "");
        checkGameWin();
    }

    private boolean isGameWon() {
        for (int i = 0; i < GameConstants.GRID_SIZE; i++) {
            for (int j = 0; j < GameConstants.GRID_SIZE; j++) {
                // Check if non-mine cell is not revealed
                if (!gameBoard.isMine(i, j) && !gameBoard.isRevealed(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void checkGameWin() {
        if (!gameBoard.isGameOver() && isGameWon()) {
            gameBoard.setGameOver(true);
            revealAllMines(); // Optional: Show mines in different color
            if (gameTimer != null) {
                gameTimer.stop();
            }
            long elapsed = (System.currentTimeMillis() - startTime) / 1000;
            int choice = JOptionPane.showConfirmDialog(this,
                    "Game Over! You won!\nTotal Time: " + elapsed + " seconds\nDifficulty Level: " + currentDifficulty
                            + "\nPlay again?",
                    "Game Over", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                System.exit(0);
            }
        }
    }

    private void resetGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        // Return to the home screen for difficulty selection
        cardLayout.show(mainPanel, "Home");
    }

    private void gameOver() {
        gameBoard.setGameOver(true);
        if (gameTimer != null) {
            gameTimer.stop();
        }
        for (int i = 0; i < GameConstants.GRID_SIZE; i++) {
            for (int j = 0; j < GameConstants.GRID_SIZE; j++) {
                if (gameBoard.isMine(i, j)) {
                    buttons[i][j].setBackground(Color.RED);
                    buttons[i][j]
                            .setIcon(new ImageIcon(bombIcon.getImage().getScaledInstance(buttons[i][j].getWidth(),
                                    buttons[i][j].getHeight(), Image.SCALE_SMOOTH)));
                }
            }
        }
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        int choice = JOptionPane.showConfirmDialog(this,
                "Game Over! You hit a mine.\nTotal Time: " + elapsed + " seconds\nDifficulty Level: "
                        + currentDifficulty + "\nPlay again?",
                "Game Over", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new Minesweeper();
        });
    }
}
