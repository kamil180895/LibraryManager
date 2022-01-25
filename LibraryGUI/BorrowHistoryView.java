package LibraryGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BorrowHistoryView extends ViewWithTable{
    private JPanel tablePanel;
    private String[] columnNames = {"Nazwisko", "Imię", "JIM", "Sygnatura", "Tytuł", "Nr biblioteczny", "Data", "Ilość", "Rodzaj"};


    public BorrowHistoryView(LibraryGUI libraryGUI)
    {
        super(libraryGUI);
        super.columnNames = columnNames;
        tablePanel = new JPanel();
    }

    private class ReturnButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            libraryGUI.changeToMainView();
        }
    }

    @Override
    public void draw() {
        JPanel panel;
        JButton button;

        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));

        panel = new JPanel();
        button = new JButton("Wróć");
        button.addActionListener(new ReturnButtonListener());
        panel.add(button);
        view.add(panel);

        tablePanel = drawDefaultTable();
        view.add(tablePanel);
    }
}
