package Library;

import java.io.Serializable;

public class CurrentBorrowRecord implements Serializable {
    private Book book;

    private String firstName;
    private String lastName;
    private int amountToReturn;
    private String date;

    private static final long serialVersionUID = 4689966815224395112L;

    public CurrentBorrowRecord(Book book)
    {
        this.book = book;
        firstName = "";
        lastName = "";
        amountToReturn = -1;
        date = "";
    }

    public CurrentBorrowRecord(BorrowRecord borrowRecord)
    {
        book = borrowRecord.getBook();
        firstName = borrowRecord.getFirstName();
        lastName = borrowRecord.getLastName();
        amountToReturn = borrowRecord.getAmount();
        date = borrowRecord.getDate();
    }

    public Book getBook() {
        return book;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAmountToReturn() {
        return amountToReturn;
    }

    public void setAmountToReturn(int amountToReturn) {
        this.amountToReturn = amountToReturn;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
