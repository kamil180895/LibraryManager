package Library;

import java.io.Serializable;

public class BorrowRecord implements Serializable {
    private Book book;
    private RecordType recordType;

    private String firstName;
    private String lastName;
    private int amount;
    private String date;

    private static final long serialVersionUID = 4689966815224395111L;

    public BorrowRecord(Book book, int amount)
    {
        this.book = book;
        recordType = RecordType.Unknown;
        firstName = "";
        lastName = "";
        this.amount = amount;
        date = "";
    }

    public enum RecordType{
        Unknown, Borrowed, Returned
    }

    public RecordType getRecordType() {
        return recordType;
    }

    public void setRecordType(RecordType recordType) {
        this.recordType = recordType;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book)
    {
        this.book = book;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
