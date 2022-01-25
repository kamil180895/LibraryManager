package LibraryGUI;

import Library.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddBookView extends View{
    private JTextField titleField;
    private JTextField signatureField;
    private JTextField jimNumberField;
    private JTextField releaseYearField;
    private JTextField librarianField;
    private JTextField measureUnitField;
    private JTextField numberOfAddedBooksField;
    private JTextField previousOwnerField;
    private JTextField documentNumberField;
    private JTextField dateField;
    private JTextField commentField;

    private class AddButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            long bigNumber;
            int number;
            String line;
            Book book = new Book();
            line = titleField.getText();
            if(line.equals(""))
            {
                showMessage("Należy wprowadzić tytuł książki", "Nieprawidłowe dane");
                return;
            }
            book.setTitle(line);
            line = signatureField.getText();
            book.setSignature(line);
            line = jimNumberField.getText();
            book.setJimNumber(line);
            line = releaseYearField.getText();
            try
            {
                number = Integer.parseInt(line);
                book.setReleaseYear(number);
            }
            catch(Exception exc)
            {
                showMessage("Nieprawidłowy rok wydania", "Nieprawidłowe dane");
                return;
            }
            line = librarianField.getText();
            try
            {
                number = Integer.parseInt(line);
                book.setLibrarianNumber(number);
            }
            catch(Exception exc)
            {
                showMessage("Nieprawidłowy numer biblioteczny", "Nieprawidłowe dane");
                return;
            }
            line = measureUnitField.getText();
            book.setMeasureUnit(line);

            BookOwnershipRecord bookOwnershipRecord = new BookOwnershipRecord(book);
            bookOwnershipRecord.setRecordType(BookOwnershipRecord.RecordType.Received);
            line = numberOfAddedBooksField.getText();
            try
            {
                number = Integer.parseInt(line);
                bookOwnershipRecord.setAmountOfBooks(number);
            }
            catch(Exception exc)
            {
                showMessage("Nieprawidłowa liczba książek", "Nieprawidłowe dane");
                return;
            }
            if(number <= 0)
            {
                showMessage("Nieprawidłowa liczba książek", "Nieprawidłowe dane");
                return;
            }
            line = previousOwnerField.getText();
            if(line.equals(""))
            {
                showMessage("Należy wypełnić pole skąd", "Nieprawidłowe dane");
                return;
            }
            bookOwnershipRecord.setPreviousOwner(line);
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
                showMessage("Należy wprowadzić datę", "Nieprawidłowe dane");
                return;
            }
            bookOwnershipRecord.setDate(line);
            line = commentField.getText();
            bookOwnershipRecord.getBook().setComment(line);

            if(showConfirmation("Czy na pewno chcesz dodać książkę z wprowadzonymi informacjami", "Potwierdzenie") == 1)
                return;

            libraryGUI.getLibrary().addBook(bookOwnershipRecord);
            if(book.getLibrarianNumber() >= libraryGUI.getLibrary().getLastFreeLibrarianNumber())
                libraryGUI.getLibrary().setLastFreeLibrarianNumber(book.getLibrarianNumber() + 1);
            libraryGUI.changeToMainView();
            resetFields();
            showMessage("Pomyślnie dodano nową książkę do spisu", "Sukces");
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

    public AddBookView(LibraryGUI libraryGUI)
    {
        super(libraryGUI);
        titleField = new JTextField();
        signatureField = new JTextField();
        jimNumberField = new JTextField();
        releaseYearField = new JTextField();
        librarianField = new JTextField();
        measureUnitField = new JTextField();
        numberOfAddedBooksField = new JTextField();
        previousOwnerField = new JTextField();
        documentNumberField = new JTextField();
        dateField = new JTextField();
        commentField = new JTextField();
    }

    @Override
    public void draw()
    {
        JPanel panel;
        JLabel label;
        JButton button;

        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));

        panel = new JPanel();
        label = new JLabel("Pola oznaczone * są wymagane");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        view.add(panel);

        panel = new JPanel();
        label = new JLabel("*Tytuł: ");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        titleField.setPreferredSize(new Dimension(450,20));
        panel.add(titleField);
        view.add(panel);

        panel = new JPanel();
        label = new JLabel("Sygnatura: ");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        signatureField.setPreferredSize(new Dimension(150,20));
        panel.add(signatureField);
        view.add(panel);

        panel = new JPanel();
        label = new JLabel("Numer JIM: ");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        jimNumberField.setPreferredSize(new Dimension(150,20));
        panel.add(jimNumberField);
        view.add(panel);

        panel = new JPanel();
        label = new JLabel("*Rok wydania: ");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        releaseYearField.setPreferredSize(new Dimension(150,20));
        panel.add(releaseYearField);
        view.add(panel);

        panel = new JPanel();
        label = new JLabel("*Numer biblioteczny: ");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        librarianField.setPreferredSize(new Dimension(150,20));
        panel.add(librarianField);
        view.add(panel);

        panel = new JPanel();
        label = new JLabel("Jednostka miary: ");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        measureUnitField.setPreferredSize(new Dimension(150,20));
        panel.add(measureUnitField);
        view.add(panel);

        panel = new JPanel();
        label = new JLabel("*Ilość dodawanych książek: ");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        numberOfAddedBooksField.setPreferredSize(new Dimension(150,20));
        panel.add(numberOfAddedBooksField);
        view.add(panel);

        panel = new JPanel();
        label = new JLabel("*Skąd: ");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        previousOwnerField.setPreferredSize(new Dimension(150,20));
        panel.add(previousOwnerField);
        view.add(panel);

        panel = new JPanel();
        label = new JLabel("*Numer dokumentu: ");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        documentNumberField.setPreferredSize(new Dimension(150,20));
        panel.add(documentNumberField);
        view.add(panel);

        panel = new JPanel();
        label = new JLabel("*Data: ");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        dateField.setPreferredSize(new Dimension(150,20));
        panel.add(dateField);
        view.add(panel);

        panel = new JPanel();
        label = new JLabel("Uwagi: ");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        commentField.setPreferredSize(new Dimension(450,20));
        panel.add(commentField);
        view.add(panel);

        panel = new JPanel();
        button = new JButton("Dodaj");
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
        titleField.setText("");
        signatureField.setText("");
        jimNumberField.setText("");
        releaseYearField.setText("");
        librarianField.setText(String.valueOf(libraryGUI.getLibrary().getLastFreeLibrarianNumber()));
        measureUnitField.setText("");
        numberOfAddedBooksField.setText("");
        previousOwnerField.setText("");
        documentNumberField.setText("");
        dateField.setText(libraryGUI.getLibrary().getTodayDate());
        commentField.setText("");
    }

    public void setFields(BookRecord bookRecord)
    {
        titleField.setText(bookRecord.getBook().getTitle());
        signatureField.setText(bookRecord.getBook().getSignature());
        jimNumberField.setText(bookRecord.getBook().getJimNumber());
        librarianField.setText(String.valueOf(bookRecord.getBook().getLibrarianNumber()));
        measureUnitField.setText(bookRecord.getBook().getMeasureUnit());
        commentField.setText(bookRecord.getBook().getComment());
    }
}
