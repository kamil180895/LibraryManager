package LibraryGUI;

import javax.swing.*;

public abstract class View {
    protected JPanel view;
    protected LibraryGUI libraryGUI;

    public View(LibraryGUI libraryGUI)
    {
        view = new JPanel();
        this.libraryGUI = libraryGUI;
    }

    public abstract void draw();

    public JPanel getViewPanel()
    {
        return view;
    }

    public static void showMessage(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage,  titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    public static int showConfirmation(String infoMessage, String titleBar)
    {
        String[] options = {"Tak", "Nie"};
        return JOptionPane.showOptionDialog(null, infoMessage,  titleBar, JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE,null, options, options[0]);
    }
}
