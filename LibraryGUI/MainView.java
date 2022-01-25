package LibraryGUI;

import Library.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainView extends ViewWithTable{
    private JPanel tablePanel;
    private String[] columnNames = {"JIM", "Sygnatura", "Tytuł", "Nr biblioteczny", "Rok wydania", "j.m.", "Ilość w bibliotece", "Ilość dostępna", "Uwagi"};
    private JTextField searchField;

    public MainView(LibraryGUI libraryGUI)
    {
        super(libraryGUI);
        super.columnNames = columnNames;
        tablePanel = new JPanel();
        searchField = new JTextField();
    }

    private class AddButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            libraryGUI.changeToAddBookView(getSelectedBook());
        }
    }
    private class OwnershipHistoryButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            libraryGUI.changeToOwnershipHistoryView();
        }
    }
    private class BorrowHistoryButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            libraryGUI.changeToBorrowHistoryView();
        }
    }
    private class CurrentBorrowsButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            libraryGUI.changeToCurrentBorrowsView();
        }
    }

    private class BorrowButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            BookRecord selectedBook = getSelectedBook();
            if(selectedBook == null)
            {
                showMessage("Nie zaznaczono żadnej książki do wypożyczenia", "Błąd");
                return;
            }
            libraryGUI.changeToBorrowBookView(selectedBook);
        }
    }

    private class DeleteButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            BookRecord selectedBook = getSelectedBook();
            if(selectedBook == null)
            {
                showMessage("Nie zaznaczono żadnej książki do usunięcia", "Błąd");
                return;
            }
            libraryGUI.changeToRemoveBookView(selectedBook);
        }
    }

    private class EditButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            BookRecord selectedBook = getSelectedBook();
            if(selectedBook == null)
            {
                showMessage("Nie zaznaczono żadnej książki do edycji", "Błąd");
                return;
            }
            libraryGUI.changeToEditBookView(selectedBook);
        }
    }

    private class ExportButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(libraryGUI.getLibrary().exportToExcel())
            {
                showMessage("Pomyślnie wyeksportowano bazę danych do pliku w folderze z programem", "Sukces");
            }
            else
            {
                showMessage("Wystąpił nieoczekiwany błąd", "Niepowodzenie");
            }
        }
    }

    private class SearchTitleButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            String str = searchField.getText();
            updateTable(libraryGUI.getLibrary().findBookByTitle(str));
        }
    }

    private class SearchJIMButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            String str = searchField.getText();
            updateTable(libraryGUI.getLibrary().findBookByJIM(str));
        }
    }

    private class SearchSignatureButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            String str = searchField.getText();
            updateTable(libraryGUI.getLibrary().findBookBySignature(str));
        }
    }

    private class SearchLibrarianNumberButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            String str = searchField.getText();
            updateTable(libraryGUI.getLibrary().findBookByLibrarianNumber(str));
        }
    }

    private BookRecord getSelectedBook()
    {
        int selectedRow = table.getSelectedRow();
        if(selectedRow == -1)
        {
            return null;
        }
        Book book = new Book();

        book.setJimNumber((String)table.getValueAt(selectedRow, 0));
        book.setSignature((String)table.getValueAt(selectedRow, 1));
        book.setTitle((String)table.getValueAt(selectedRow, 2));
        book.setLibrarianNumber((int)table.getValueAt(selectedRow, 3));
        book.setReleaseYear((int)table.getValueAt(selectedRow, 4));
        book.setMeasureUnit((String)table.getValueAt(selectedRow, 5));
        book.setComment((String)table.getValueAt(selectedRow, 8));

        BookRecord bookRecord = new BookRecord(book);
        bookRecord.setAmountOwn((int)table.getValueAt(selectedRow, 6));
        bookRecord.setAmountAvailable((int)table.getValueAt(selectedRow, 7));
        return bookRecord;
    }

    @Override
    public void draw()
    {
        JPanel panel;
        JButton button;

        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));

        panel = new JPanel();
        //panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        button = new JButton("Historia wypożyczeń");
        button.addActionListener(new BorrowHistoryButtonListener());
        panel.add(button);
        button = new JButton("Aktualne wypożyczenia");
        button.addActionListener(new CurrentBorrowsButtonListener());
        panel.add(button);
        button = new JButton("Historia posiadania");
        button.addActionListener(new OwnershipHistoryButtonListener());
        panel.add(button);
        view.add(panel);

        panel = new JPanel();
        searchField.setPreferredSize(new Dimension(150,20));
        panel.add(searchField);
        button = new JButton("Wyszukaj tytuł");
        button.addActionListener(new SearchTitleButtonListener());
        panel.add(button);
        button = new JButton("Wyszukaj JIM");
        button.addActionListener(new SearchJIMButtonListener());
        panel.add(button);
        button = new JButton("Wyszukaj sygnaturę");
        button.addActionListener(new SearchSignatureButtonListener());
        panel.add(button);
        button = new JButton("Wyszukaj nr biblioteczny");
        button.addActionListener(new SearchLibrarianNumberButtonListener());
        panel.add(button);
        view.add(panel);

        tablePanel = drawDefaultTable();
        view.add(tablePanel);
        updateTable(libraryGUI.getLibrary().convertAllBooksToRowData());

        panel = new JPanel();
        button = new JButton("Dodaj");
        button.addActionListener(new AddButtonListener());
        panel.add(button);
        button = new JButton("Wypożycz");
        button.addActionListener(new BorrowButtonListener());
        panel.add(button);
        button = new JButton("Usuń");
        button.addActionListener(new DeleteButtonListener());
        panel.add(button);
        button = new JButton("Edytuj");
        button.addActionListener(new EditButtonListener());
        panel.add(button);
        button = new JButton("Eksportuj");
        button.addActionListener(new ExportButtonListener());
        panel.add(button);
        view.add(panel);
    }

    public void testFill()
    {
        int n = 3000;
        Object[][] fill = new Object[n][8];
        for(int i=0; i<n; ++i)
        {
            for(int j=0; j<8; ++j)
            {
                fill[i][j] = i;
            }
        }

        updateTable(fill);
    }

}
