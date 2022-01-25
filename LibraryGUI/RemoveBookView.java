package LibraryGUI;

import Library.Book;
import Library.BookOwnershipRecord;
import Library.BookRecord;
import Library.BorrowRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RemoveBookView extends View{
    private JLabel titleLabel;
    private JTextField documentNumberField;
    private JTextField dateField;
    private JTextField amountField;

    private JRadioButton destroyButton;
    private JRadioButton giveAwayButton;
    private JPanel nextOwnerPanel;
    private JTextField nextOwnerField;
    private JLabel nextOwnerPlaceholderLabel;
    private BookOwnershipRecord.RecordType type;

    private BookRecord removedBook;

    private class RemoveButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(removedBook == null)
            {
                showMessage("Wystąpił nieoczekiwany błąd: nie wybrano książki do usunięcia", "Nieoczekiwany błąd");
                return;
            }
            int number;
            String line;
            Book book = removedBook.getBook();
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
                showMessage("Liczba usuwanych książek nie może być mniejsza od 1", "Nieprawidłowe dane");
                return;
            }
            if(number > removedBook.getAmountAvailable())
            {
                showMessage("Nie ma takiej liczby książek w bibliotece", "Brak książek");
                return;
            }
            BookOwnershipRecord bookOwnershipRecord = new BookOwnershipRecord(book);
            bookOwnershipRecord.setAmountOfBooks(number);

            if(type.equals(BookOwnershipRecord.RecordType.Unknown))
            {
                showMessage("Należy wybrać rodzaj usuwania(zniszczenie/wysłanie)", "Nieprawidłowe dane");
                return;
            }
            bookOwnershipRecord.setRecordType(type);
            line = documentNumberField.getText();
            if(line.equals(""))
            {
                showMessage("Należy wprowadzić numer dokumentu", "Nieprawidłowe dane");
                return;
            }
            bookOwnershipRecord.setDocumentNumber(line);
            line = dateField.getText();
            if(line.equals(""))
            {
                showMessage("Należy wprowadzić datę wypożyczenia", "Nieprawidłowe dane");
                return;
            }
            bookOwnershipRecord.setDate(line);

            if(type.equals(BookOwnershipRecord.RecordType.Given))
            {
                line = nextOwnerField.getText();
                if(line.equals(""))
                {
                    showMessage("Należy wprowadzić do kogo wysłano", "Nieprawidłowe dane");
                    return;
                }
                bookOwnershipRecord.setNextOwner(line);
            }

            if(showConfirmation("Czy na pewno chcesz usunąć książkę z wprowadzonymi informacjami", "Potwierdzenie") == 1)
                return;

            if(!libraryGUI.getLibrary().removeBook(bookOwnershipRecord))
            {
                showMessage("Wystąpił nieznany błąd", "Nieznany błąd");
                return;
            }

            libraryGUI.changeToMainView();
            resetFields();
            showMessage("Pomyślnie usunięto książkę", "Sukces");
            libraryGUI.getLibrary().saveToFile();
        }
    }

    private class DestroyButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            type = BookOwnershipRecord.RecordType.Destroyed;
            nextOwnerPanel.remove(nextOwnerField);
            nextOwnerPanel.add(nextOwnerPlaceholderLabel);
            nextOwnerPanel.revalidate();
            nextOwnerPanel.repaint();
        }
    }

    private class GiveAwayButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            type = BookOwnershipRecord.RecordType.Given;
            nextOwnerPanel.remove(nextOwnerPlaceholderLabel);
            nextOwnerPanel.add(nextOwnerField);
            nextOwnerPanel.revalidate();
            nextOwnerPanel.repaint();
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

    public RemoveBookView(LibraryGUI libraryGUI)
    {
        super(libraryGUI);
        titleLabel = new JLabel("");
        documentNumberField = new JTextField();
        amountField = new JTextField();
        dateField = new JTextField();
        removedBook = null;

        type = BookOwnershipRecord.RecordType.Unknown;
        destroyButton = new JRadioButton("Zniszczono");
        giveAwayButton = new JRadioButton("Wysłano do");
        nextOwnerField = new JTextField();
        nextOwnerField.setPreferredSize(new Dimension(150,20));
        nextOwnerPanel = new JPanel();
        nextOwnerPlaceholderLabel = new JLabel(" ");
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
        label = new JLabel("Numer dokumentu: ");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        documentNumberField.setPreferredSize(new Dimension(150,20));
        panel.add(documentNumberField);
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
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(destroyButton);
        buttonGroup.add(giveAwayButton);
        destroyButton.addActionListener(new DestroyButtonListener());
        giveAwayButton.addActionListener(new GiveAwayButtonListener());
        panel.add(destroyButton);
        panel.add(giveAwayButton);
        view.add(panel);

        nextOwnerPanel.add(nextOwnerPlaceholderLabel);
        view.add(nextOwnerPanel);

        panel = new JPanel();
        button = new JButton("Usuń");
        button.addActionListener(new RemoveButtonListener());
        panel.add(button);
        button = new JButton("Wróć");
        button.addActionListener(new BackButtonListener());
        panel.add(button);
        view.add(panel);

        resetFields();
    }

    public void resetFields()
    {
        documentNumberField.setText("");
        amountField.setText("");
        dateField.setText(libraryGUI.getLibrary().getTodayDate());
        nextOwnerField.setText("");
        removedBook = null;

        destroyButton.setSelected(false);
        giveAwayButton.setSelected(false);
    }

    public void setRemovedBook(BookRecord book)
    {
        this.removedBook = book;
    }

    public void updateLabel()
    {
        titleLabel.setText("Wybrano książkę o tytule \"" + removedBook.getBook().getTitle() + "\" do usunięcia");
    }
}
