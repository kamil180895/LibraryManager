package LibraryGUI;

import Library.Book;
import Library.BookRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditBookView extends View{
    private JLabel titleLabel;
    private JTextField newTitleField;
    private JTextField newSignatureField;
    private JTextField newJimField;
    private JTextField newLibrarianNumberField;
    private JTextField newReleaseYearField;
    private JTextField newMeasureUnitField;
    private JTextField newCommentField;

    private BookRecord editedBook;

    public EditBookView(LibraryGUI libraryGUI)
    {
        super(libraryGUI);
        newTitleField = new JTextField();
        titleLabel = new JLabel();
        newSignatureField = new JTextField();
        newJimField = new JTextField();
        newLibrarianNumberField = new JTextField();
        newReleaseYearField = new JTextField();
        newMeasureUnitField = new JTextField();
        newCommentField = new JTextField();
    }

    private class EditButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(editedBook == null)
            {
                showMessage("Wystąpił nieoczekiwany błąd: nie wybrano książki do edycji", "Nieoczekiwany błąd");
                return;
            }

            int number;
            String line;
            Book book = editedBook.getBook();
            Book newBook = new Book();
            line = newTitleField.getText();
            if(line.equals(""))
            {
                showMessage("Należy wprowadzić tytuł książki", "Nieprawidłowe dane");
                return;
            }
            newBook.setTitle(line);
            line = newSignatureField.getText();
            newBook.setSignature(line);
            line = newJimField.getText();
            newBook.setJimNumber(line);
            line = newReleaseYearField.getText();
            try
            {
                number = Integer.parseInt(line);
                newBook.setReleaseYear(number);
            }
            catch(Exception exc)
            {
                showMessage("Nieprawidłowy rok wydania", "Nieprawidłowe dane");
                return;
            }
            line = newLibrarianNumberField.getText();
            try
            {
                number = Integer.parseInt(line);
                newBook.setLibrarianNumber(number);
            }
            catch(Exception exc)
            {
                showMessage("Nieprawidłowy numer biblioteczny", "Nieprawidłowe dane");
                return;
            }
            line = newMeasureUnitField.getText();
            newBook.setMeasureUnit(line);
            line = newCommentField.getText();
            newBook.setComment(line);

            if(showConfirmation("Czy na pewno chcesz edytować książkę z wprowadzonymi informacjami", "Potwierdzenie") == 1)
                return;

            if(!libraryGUI.getLibrary().editBook(book, newBook))
            {
                showMessage("Wystąpił nieznany błąd", "Nieznany błąd");
                return;
            }

            libraryGUI.changeToMainView();
            resetFields();
            showMessage("Pomyślnie edytowano książkę", "Sukces");
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

    @Override
    public void draw() {
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
        label = new JLabel("Nowy tytuł: ");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        newTitleField.setPreferredSize(new Dimension(450,20));
        panel.add(newTitleField);
        view.add(panel);

        panel = new JPanel();
        label = new JLabel("Nowa sygnatura: ");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        newSignatureField.setPreferredSize(new Dimension(150,20));
        panel.add(newSignatureField);
        view.add(panel);

        panel = new JPanel();
        label = new JLabel("Nowy JIM: ");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        newJimField.setPreferredSize(new Dimension(150,20));
        panel.add(newJimField);
        view.add(panel);

        panel = new JPanel();
        label = new JLabel("Nowy numer biblioteczny: ");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        newLibrarianNumberField.setPreferredSize(new Dimension(150,20));
        panel.add(newLibrarianNumberField);
        view.add(panel);

        panel = new JPanel();
        label = new JLabel("Nowy rok wydania: ");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        newReleaseYearField.setPreferredSize(new Dimension(150,20));
        panel.add(newReleaseYearField);
        view.add(panel);

        panel = new JPanel();
        label = new JLabel("Nowa jednostka miary: ");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        newMeasureUnitField.setPreferredSize(new Dimension(150,20));
        panel.add(newMeasureUnitField);
        view.add(panel);

        panel = new JPanel();
        label = new JLabel("Nowy komentarz: ");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        panel.add(label);
        newCommentField.setPreferredSize(new Dimension(150,20));
        panel.add(newCommentField);
        view.add(panel);

        panel = new JPanel();
        button = new JButton("Edytuj");
        button.addActionListener(new EditButtonListener());
        panel.add(button);
        button = new JButton("Wróć");
        button.addActionListener(new BackButtonListener());
        panel.add(button);
        view.add(panel);

        resetFields();
    }

    public void resetFields()
    {
        newTitleField.setText("");
        newSignatureField.setText("");
        newJimField.setText("");
        newLibrarianNumberField.setText("");
        newReleaseYearField.setText("");
        newMeasureUnitField.setText("");
        newCommentField.setText("");
        editedBook = null;
    }

    public void setEditedBook(BookRecord book)
    {
        this.editedBook = book;
    }

    public void update()
    {
        titleLabel.setText("Wybrano książkę o tytule \"" + editedBook.getBook().getTitle() + "\" do edycji");
        newTitleField.setText(editedBook.getBook().getTitle());
        newSignatureField.setText(editedBook.getBook().getSignature());
        newJimField.setText(editedBook.getBook().getJimNumber());
        newLibrarianNumberField.setText(String.valueOf(editedBook.getBook().getLibrarianNumber()));
        newReleaseYearField.setText(String.valueOf(editedBook.getBook().getReleaseYear()));
        newMeasureUnitField.setText(editedBook.getBook().getMeasureUnit());
        newCommentField.setText(editedBook.getBook().getComment());
    }
}
