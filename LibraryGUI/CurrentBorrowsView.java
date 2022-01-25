package LibraryGUI;

import Library.Book;
import Library.BorrowRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CurrentBorrowsView extends ViewWithTable{
    private JPanel tablePanel;
    private String[] columnNames = {"Nazwisko", "Imię", "JIM", "Sygnatura", "Tytuł", "Nr biblioteczny", "Data", "Ilość do zwrotu"};


    public CurrentBorrowsView(LibraryGUI libraryGUI)
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

    private class ReturnBookButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if(selectedRow == -1)
            {
                showMessage("Nie zaznaczono żadnej książki do zwrócenia", "Błąd");
                return;
            }

            Book book = new Book();
            book.setJimNumber((String)table.getValueAt(selectedRow, 2));
            book.setSignature((String)table.getValueAt(selectedRow, 3));
            book.setTitle((String)table.getValueAt(selectedRow, 4));
            book.setLibrarianNumber((int)table.getValueAt(selectedRow, 5));

            boolean correctInput = false;
            int number = 0;
            do {
                String input =(String)JOptionPane.showInputDialog(null, "Ile książek zwrócono?", "Zwracanie książek",
                        JOptionPane.PLAIN_MESSAGE, null, null, table.getValueAt(selectedRow, 7));
                if(input==null)
                {
                    return;
                }

                try
                {
                    number = Integer.parseInt(input);
                }catch (Exception exc)
                {
                    continue;
                }
                if(number > 0 && number <= (int)table.getValueAt(selectedRow, 7))
                {
                    correctInput = true;
                }
            }while(!correctInput);

            BorrowRecord borrowRecord = new BorrowRecord(book, number);
            borrowRecord.setRecordType(BorrowRecord.RecordType.Returned);

            borrowRecord.setLastName((String)table.getValueAt(selectedRow, 0));
            borrowRecord.setFirstName((String)table.getValueAt(selectedRow, 1));
            borrowRecord.setDate(libraryGUI.getLibrary().getTodayDate());

            if(libraryGUI.getLibrary().returnBook(borrowRecord))
            {
                showMessage("Pomyślnie zwrócono książki", "Sukces");
                updateTable(libraryGUI.getLibrary().convertAllCurrentBorrowRecordsToRowData());
                libraryGUI.getLibrary().saveToFile();
            }
            else
            {
                showMessage("Wystąpił nieoczekiwany błąd", "Nieoczekiwany bląd");
            }
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

        panel = new JPanel();
        button = new JButton("Zwróć");
        button.addActionListener(new ReturnBookButtonListener());
        panel.add(button);
        view.add(panel);
    }
}
