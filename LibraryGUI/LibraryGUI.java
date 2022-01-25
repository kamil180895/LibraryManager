package LibraryGUI;

import Library.Library;
import Library.BookRecord;

import javax.swing.*;

public class LibraryGUI {
    private Library library;

    private JFrame frame;
    private MainView mainView;
    private AddBookView addBookView;
    private OwnershipHistoryView ownershipHistoryView;
    private BorrowHistoryView borrowHistoryView;
    private BorrowBookView borrowBookView;
    private CurrentBorrowsView currentBorrowsView;
    private RemoveBookView removeBookView;
    private EditBookView editBookView;

    public LibraryGUI(Library lib)
    {
        library = lib;
        frame = new JFrame();
        mainView = new MainView(this);
        addBookView = new AddBookView(this);
        ownershipHistoryView = new OwnershipHistoryView(this);
        borrowHistoryView = new BorrowHistoryView(this);
        borrowBookView = new BorrowBookView(this);
        currentBorrowsView = new CurrentBorrowsView(this);
        removeBookView = new RemoveBookView(this);
        editBookView = new EditBookView(this);
    }

    private void changeViewTo(View view)
    {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(view.getViewPanel());
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    public void initialize()
    {
        mainView.draw();
        addBookView.draw();
        ownershipHistoryView.draw();
        borrowHistoryView.draw();
        borrowBookView.draw();
        currentBorrowsView.draw();
        removeBookView.draw();
        editBookView.draw();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600,1024);
        frame.getContentPane().add(mainView.getViewPanel());
        frame.setVisible(true);
    }

    public void changeToMainView()
    {
        mainView.updateTable(library.convertAllBooksToRowData());
        changeViewTo(mainView);
    }

    public void changeToAddBookView(BookRecord bookRecord)
    {
        if(bookRecord != null)
            addBookView.setFields(bookRecord);
        changeViewTo(addBookView);
    }

    public void changeToOwnershipHistoryView()
    {
        ownershipHistoryView.updateTable(library.convertAllOwnershipRecordsToRowData());
        changeViewTo(ownershipHistoryView);
    }

    public void changeToBorrowHistoryView()
    {
        borrowHistoryView.updateTable(library.convertAllBorrowRecordsToRowData());
        changeViewTo(borrowHistoryView);
    }

    public void changeToCurrentBorrowsView()
    {
        currentBorrowsView.updateTable(library.convertAllCurrentBorrowRecordsToRowData());
        changeViewTo(currentBorrowsView);
    }

    public void changeToBorrowBookView(BookRecord bookRecord)
    {
        borrowBookView.setBorrowedBook(bookRecord);
        borrowBookView.updateLabel();
        changeViewTo(borrowBookView);
    }

    public void changeToRemoveBookView(BookRecord bookRecord)
    {
        removeBookView.setRemovedBook(bookRecord);
        removeBookView.updateLabel();
        changeViewTo(removeBookView);
    }

    public void changeToEditBookView(BookRecord bookRecord)
    {
        editBookView.setEditedBook(bookRecord);
        editBookView.update();
        changeViewTo(editBookView);
    }

    public Library getLibrary() {
        return library;
    }

    public MainView getMainView() {
        return mainView;
    }

    public AddBookView getAddBookView() {
        return addBookView;
    }
}
