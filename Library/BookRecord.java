package Library;

import java.io.Serializable;

public class BookRecord implements Serializable {
    //private int bookRecordID;
    private Book book;
    private int amountOwn;
    private int amountAvailable;


    private static final long serialVersionUID = 4689966815224392110L;

    public BookRecord(Book book)
    {
        this.book = book;
        //bookRecordID = -1;
        amountOwn = 0;
        amountAvailable = 0;
    }

    /*public int getBookRecordID() {
        return bookRecordID;
    }*/

    public Book getBook()
    {
        return book;
    }

    public int getAmountOwn()
    {
        return amountOwn;
    }

    /*public void setBookRecordID(int bookRecordID) {
        this.bookRecordID = bookRecordID;
    }*/

    public void setAmountOwn(int amountOwn)
    {
        this.amountOwn = amountOwn;
    }

    public void setAmountAvailable(int amountAvailable)
    {
        this.amountAvailable = amountAvailable;
    }

    public int getAmountAvailable()
    {
        return amountAvailable;
    }

    public boolean isAnyBookAvailable()
    {
        return amountAvailable > 0;
    }
}
