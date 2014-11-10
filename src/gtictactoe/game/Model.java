package gtictactoe.game;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private final byte[][] board;

    private static Model model = null;
    private int moves;
    public static int DEPTH = 3;

    private Model() {
        moves = 0;
        board = new byte[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = (byte) 0;
            }
        }
    }

    public void reset() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = (byte) 0;
            }
        }
        moves = 0;
    }

    public static Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public int[] calculateAIMove() {
        return alphaBeta(DEPTH, Controller.getInstance().getMachinePlayer(), Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1);
    }

    // Machine Maximize - Human Minimize
    private int[] alphaBeta(int depth, byte player, int alpha, int beta) {
        List<int[]> availableMoves = findMoves();

        int bestI = -1;
        int bestJ = -1;
        int score = 0;

        if (availableMoves.isEmpty() || depth == 0) {
            score = findScore();
            return new int[]{bestI, bestJ, score};
        } else {

            for (int[] move : availableMoves) {
                board[move[0]][move[1]] = player;

                if (player == Controller.getInstance().getMachinePlayer()) { // Maximize
                    score = alphaBeta(depth - 1, Controller.getInstance().getHumanPlayer(), alpha, beta)[2];
                    if (score > alpha) {
                        alpha = score;
                        bestI = move[0];
                        bestJ = move[1];
                    }
                } else { // minimize
                    score = alphaBeta(depth - 1, Controller.getInstance().getMachinePlayer(), alpha, beta)[2];
                    if (score < beta) {
                        beta = score;
                        bestI = move[0];
                        bestJ = move[1];
                    }
                }

                board[move[0]][move[1]] = 0;
                if (alpha >= beta) {
                    break;
                }
            }
            return new int[]{bestI, bestJ, (player == Controller.getInstance().getMachinePlayer()) ? alpha : beta};
        }
    }

    private int findScore() {
        int score = 0;
        score += findScore(0, 0, 0, 1, 0, 2);
        score += findScore(1, 0, 1, 1, 1, 2);
        score += findScore(2, 0, 2, 1, 2, 2);
        score += findScore(0, 0, 1, 0, 2, 0);
        score += findScore(0, 1, 1, 1, 2, 1);
        score += findScore(0, 2, 1, 2, 2, 2);
        score += findScore(0, 0, 1, 1, 2, 2);
        score += findScore(0, 2, 1, 1, 2, 0);

        return score;
    }

    private int findScore(int r0, int c0, int r1, int c1, int r2, int c2) {
        int score = 0;

        byte pH = Controller.getInstance().getHumanPlayer();
        byte pM = Controller.getInstance().getMachinePlayer();
        byte b0 = board[r0][c0];
        byte b1 = board[r1][c1];
        byte b2 = board[r2][c2];

        if (b0 == 0 && b1 == 0 && b2 == 0) {
            score = 0;
        } else if ((b0 == 0 && b1 == 0 && b2 == pH) || (b0 == 0 && b1 == pH && b2 == 0) || (b0 == pH && b1 == 0 && b2 == 0)) {
            score = -50;
        } else if ((b0 == 0 && b1 == 0 && b2 == pM) || (b0 == 0 && b1 == pM && b2 == 0) || (b0 == pM && b1 == 0 && b2 == 0)) {
            score = 50;
        } else if (b0 == 0 && b1 == pH && b2 == pM || (b0 == 0 && b1 == pM && b2 == pH)) {
            score = 0;
        } else if (b0 == 0 && b1 == pH && b2 == pH) {
            score = -100;
        } else if (b0 == 0 && b1 == pM && b2 == pM) {
            score = 100;
        } else if (b1 == 0 && b0 == pH && b2 == pM || (b1 == 0 && b0 == pM && b2 == pH)) {
            score = 0;
        } else if (b1 == 0 && b0 == pH && b2 == pH) {
            score = -100;
        } else if (b1 == 0 && b0 == pM && b2 == pM) {
            score = 100;
        } else if (b2 == 0 && b1 == pH && b0 == pM || (b2 == 0 && b1 == pM && b0 == pH)) {
            score = 0;
        } else if (b2 == 0 && b1 == pH && b0 == pH) {
            score = -100;
        } else if (b2 == 0 && b1 == pM && b0 == pM) {
            score = 100;
        } else if ((b0 == pM && b1 == pM && b2 == pH) || (b0 == pM && b1 == pH && b2 == pM)) {
            score = 0;
        } else if ((b0 == pM && b1 == pH && b2 == pH) || (b0 == pH && b1 == pM && b2 == pH)) {
            score = 0;
        } else if ((b0 == pH && b1 == pM && b2 == pM) || (b0 == pH && b1 == pH && b2 == pM)) {
            score = 0;
        } else if (b0 == pH && b1 == pH && b2 == pH) {
            score = -1000;
        } else if (b0 == pM && b1 == pM && b2 == pM) {
            score = 1000;
        }

        return score;
    }

    List<int[]> findMoves() {
        List<int[]> moves = new ArrayList<>();

        if (checkGameStatus(Controller.getInstance().getHumanPlayer()) >= 0 || checkGameStatus(Controller.getInstance().getMachinePlayer()) >= 0) {
            return moves;
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == (byte) 0) {
                    moves.add(new int[]{i, j});
                }
            }
        }

        return moves;
    }

    public int updateMove(int i, int j, byte player) {
        moves++;
        board[i][j] = player;
        return checkGameStatus(player);
    }

    private void printBoard() {
        System.out.println("Game Status");
        for (int i = 0; i < 3; i++) {
            System.out.print("\t");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private int checkGameStatus(byte player) {
        
        if (moves >= 9) {
            return 0;
        }
        
        if (board[0][0] == player) {
            if (board[0][1] == player && board[0][2] == player) {
                return 1;
            } else if (board[1][0] == player && board[2][0] == player) {
                return 1;
            } else if (board[1][1] == player && board[2][2] == player) {
                return 1;
            }
        }

        if (board[0][1] == player) {
            if (board[1][1] == player && board[2][1] == player) {
                return 1;
            }
        }

        if (board[0][2] == player) {
            if (board[1][2] == player && board[2][2] == player) {
                return 1;
            }

            if (board[1][1] == player && board[2][0] == player) {
                return 1;
            }
        }
        
        

        return -1;
    }
}
