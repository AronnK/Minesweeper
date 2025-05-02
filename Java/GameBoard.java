import java.util.Random;

public class GameBoard {
    private boolean[][] mines;
    private boolean[][] revealed;
    private boolean[][] flagged;
    private int minesCount;
    private boolean gameOver;

    public GameBoard() {
        mines = new boolean[GameConstants.GRID_SIZE][GameConstants.GRID_SIZE];
        revealed = new boolean[GameConstants.GRID_SIZE][GameConstants.GRID_SIZE];
        flagged = new boolean[GameConstants.GRID_SIZE][GameConstants.GRID_SIZE];
        gameOver = false;
    }

    public void initializeBoard(String difficulty) {
        // Calculate mines based on difficulty
        if (difficulty.equals("Easy")) {
            minesCount = (int) (GameConstants.GRID_SIZE * GameConstants.GRID_SIZE * GameConstants.EASY_MINE_PERCENTAGE);
        } else if (difficulty.equals("Medium")) {
            minesCount = (int) (GameConstants.GRID_SIZE * GameConstants.GRID_SIZE
                    * GameConstants.MEDIUM_MINE_PERCENTAGE);
        } else {
            minesCount = (int) (GameConstants.GRID_SIZE * GameConstants.GRID_SIZE * GameConstants.HARD_MINE_PERCENTAGE);
        }
        placeMines();
    }

    private void placeMines() {
        Random rand = new Random();
        int placedMines = 0;
        while (placedMines < minesCount) {
            int r = rand.nextInt(GameConstants.GRID_SIZE);
            int c = rand.nextInt(GameConstants.GRID_SIZE);
            if (!mines[r][c]) {
                mines[r][c] = true;
                placedMines++;
            }
        }
    }

    public int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int nr = row + dr, nc = col + dc;
                if (nr >= 0 && nr < GameConstants.GRID_SIZE && nc >= 0 && nc < GameConstants.GRID_SIZE
                        && mines[nr][nc]) {
                    count++;
                }
            }
        }
        return count;
    }

    // Getters and setters
    public boolean isMine(int row, int col) {
        return mines[row][col];
    }

    public boolean isRevealed(int row, int col) {
        return revealed[row][col];
    }

    public boolean isFlagged(int row, int col) {
        return flagged[row][col];
    }

    public void setRevealed(int row, int col) {
        revealed[row][col] = true;
    }

    public void toggleFlag(int row, int col) {
        flagged[row][col] = !flagged[row][col];
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean value) {
        gameOver = value;
    }

    public boolean isGameWon() {
        for (int i = 0; i < GameConstants.GRID_SIZE; i++) {
            for (int j = 0; j < GameConstants.GRID_SIZE; j++) {
                if (!mines[i][j] && !revealed[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}