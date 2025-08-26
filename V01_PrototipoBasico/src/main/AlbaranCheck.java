package main;

import vista.*;
import javax.swing.SwingUtilities;


/**
 *
 * @author Leonardo Méndez
 */
public class AlbaranCheck {

    public static void main(String[] args) {
        
       
        SwingUtilities.invokeLater(() -> {
            JFrameMenu frame = new JFrameMenu();
            frame.setVisible(true);
        });
    }
}
