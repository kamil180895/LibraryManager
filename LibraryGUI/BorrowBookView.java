package LibraryGUI;

import Library.Book;
import Library.BookOwnershipRecord;
import Library.BookRecord;
import Library.BorrowRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BorrowBookView extends View{
    private JLabel titleLabel;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField dateField;
    private JTextField amountField;

    private BookRecord borrowedBook;

    private class AddButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(borrowedBook == null)
            {
                showMessage("Wystąpił nieoczekiwany błąd: nie wybrano książki do wypożyczenia", "Nieoczekiwany błąd");
                return;
            }
            int number;
            String line;
            Book book = borrowedBook.getBook();
            line = amountField.getText();
            try
            {
                number = Integer.parseInt(line);
            }
            catch(Exception exc)
            {
                showMessage("Nieprawidłowa liczba książek", "Nieprawidłowe dane");
                return;
            }
            if(number <= 0)
            {
                showMessage("Liczba wypożyczanych książek nie może być mniejsza od 1", "Nieprawidłowe dane");
                return;
            }
            if(number > borrowedBook.getAmountAvailable())
            {
                showMessage("Nie ma wystarczającej liczby książek w bibliotece na takie wypożyczenie", "Brak książek");
                return;
            }
            BorrowRecord borrowRecord = new BorrowRecord(book, number);
            borrowRecord.setRecordType(BorrowRecord.RecordType.Borrowed);
            line = lastNameField.getText();
            if(line.equals(""))
            {
                showMessage("Należy wprowadzić nazwisko wypożyczającego", "Nieprawidłowe dane");
                return;
            }
            line = line.substring(0,1).toUpperCase() + line.substring(1).toLowerCase();
            borrowRecord.setLastName(line);
            line = firstNameField.getText();
            if(line.equals(""))
            {
                showMessage("Należy wprowadzić imię wypożyczającego", "Nieprawidłowe dane");
                return;
            }
            line = line.substring(0,1).toUpperCase() + line.substring(1).toLowerCase();
            borrowRecord.setFirstName(line);
            line = dateField.getText();
            if(line.equals(""))
            {
                showMessage("Należy wprowadzić datę wypożyczenia", "Nieprawidłowe dane");
                return;
            }
            borrowRecord.setDate(line);

            if(showConfirmation("Czy na pewno chcesz wypożyczyć książkę z wprowadzonymi informacjami", "Potwierdzenie") == 1)
                return;

            if(!libraryGUI.getLibrary().borrowBook(borrowRecord))
            {
                showMessage("Wystąpił nieznany błąd", "Nieznany błąd");
                return;
            }
            libraryGUI.changeToMainView();
            resetFields();
            showMessage("Pomyślnie wypożyczono książkę", "Sukces");
            libraryGUI.getLibrary().saveToFile();
        }
    }

    private class BackButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            libraryGUI.changeToMainView();
            resetFields();
        }
    }

    public BorrowBookView(LibraryGUI libraryGUI)
    {
        super(libraryGUI);
        titleLabel = new JLabel("");
        lastNameField = new JTextField();
        firstNameField = new JTextField();
        amountField = new JTextField();
        dateField = new JTextField();
        borrowedBook = null;
    }

    @Override
    public void draw()
    {
        JPanel panel;
        JLabel label;
        JButton button;

        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));

        panel = new JPanel();
        titleLabel.setText("");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.PLAIN, 14));
        panel.add(titleLabel);
        view.add(panel);

        panel = new JPanel();
        label = new JLabel("Wszystkie pola są wymagane");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        view.add(panel);

        panel = new JPanel();
        label = new JLabel("Nazwisko: ");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        lastNameField.setPreferredSize(new Dimension(150,20));
        panel.add(lastNameField);
        view.add(panel);

        panel = new JPanel();
        label = new JLabel("Imię: ");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        firstNameField.setPreferredSize(new Dimension(150,20));
        panel.add(firstNameField);
        view.add(panel);

        panel = new JPanel();
        label = new JLabel("Ilość: ");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        amountField.setPreferredSize(new Dimension(150,20));
        panel.add(amountField);
        view.add(panel);

        panel = new JPanel();
        label = new JLabel("Data: ");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        dateField.setPreferredSize(new Dimension(150,20));
        panel.add(dateField);
        view.add(panel);

        panel = new JPanel();
        button = new JButton("Wypożycz");
        button.addActionListener(new AddButtonListener());
        panel.add(button);
        button = new JButton("Wróć");
        button.addActionListener(new BackButtonListener());
        panel.add(button);
        view.add(panel);

        resetFields();
    }

    public void resetFields()
    {
        lastNameField.setText("");
        firstNameField.setText("");
        amountField.setText("");
        dateField.setText(libraryGUI.getLibrary().getTodayDate());
        borrowedBook = null;
    }

    public void setBorrowedBook(BookRecord book)
    {
        this.borrowedBook = book;
    }

    public void updateLabel()
    {
        titleLabel.setText("Wybrano książkę o tytule \"" + borrowedBook.getBook().getTitle() + "\"");
    }
}
