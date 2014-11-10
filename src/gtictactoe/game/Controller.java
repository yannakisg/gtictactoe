package gtictactoe.game;

import gtictactoe.gui.MainFrame;

public class Controller {

    private static Controller controller = null;

    private Controller() {
    }

    public void onClick(int i, int j, byte player, boolean humanVsHuman) {

        int isWinner = Model.getInstance().updateMove(i, j, player);
        if (isWinner == 1) {
            MainFrame.getInstance().winnerFound();
        } else if (isWinner == -1) {
            if (humanVsHuman) {
                MainFrame.getInstance().updatePlayer();
            } else {
                MainFrame.getInstance().updatePlayer();
                if (player == MainFrame.X()) {
                    int[] move = Model.getInstance().calculateAIMove();
                    if (move[0] == -1 || move[1] == -1) {
                        MainFrame.getInstance().drawResult();
                    } else {
                        MainFrame.getInstance().emitButtonEvent(move[0], move[1]);
                    }
                }
            }
        } else {
            MainFrame.getInstance().drawResult();
        }
    }

    public byte getMachinePlayer() {
        return MainFrame.O();
    }

    public byte getHumanPlayer() {
        return MainFrame.X();
    }

    public void reset() {
        Model.getInstance().reset();
    }

    public static Controller getInstance() {
        if (controller == null) {
            controller = new Controller();
        }
        return controller;
    }
}
