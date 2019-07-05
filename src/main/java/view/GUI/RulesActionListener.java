package view.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


public class RulesActionListener implements ActionListener {

    public RulesActionListener(){}
    @Override
    public void actionPerformed(ActionEvent e) {
        if (Desktop.isDesktopSupported()) {
            try {
                InputStream is = getClass().getResourceAsStream("/rule.pdf");
                String tempFile = "RulesGame";
                Path tempOutput;
                tempOutput = Files.createTempFile(tempFile, ".pdf");
                tempOutput.toFile().deleteOnExit();
                Files.copy(is,tempOutput, StandardCopyOption.REPLACE_EXISTING);
                Desktop.getDesktop().open(tempOutput.toFile());
            } catch (IOException ignored) {}
        }
        else JOptionPane.showMessageDialog(null,"Desktop functionality does not work","RULES",JOptionPane.WARNING_MESSAGE);
    }
}
