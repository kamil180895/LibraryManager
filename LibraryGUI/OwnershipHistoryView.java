package LibraryGUI;

import Library.BookOwnershipRecord;
import Library.BookRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class OwnershipHistoryView extends ViewWithTable{
    private JPanel tablePanel;
    private String[] columnNames = {"JIM", "Sygnatura", "Tytuł", "Nr biblioteczny", "Rodzaj", "Skąd/Dokąd", "Numer dokumentu", "Data", "Ilość"};


    public OwnershipHistoryView(LibraryGUI libraryGUI)
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
