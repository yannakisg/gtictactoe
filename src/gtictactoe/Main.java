package gtictactoe;

import gtictactoe.gui.MainFrame;

public class Main {

    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame.getInstance().setVisible(true);
            }
        });
    }

}
