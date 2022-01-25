package Library;


import java.io.Serializable;

public class BookOwnershipRecord implements Serializable {
    private Book book;
    private RecordType recordType;

    private String previousOwner;
    private String nextOwner;
    private String documentNumber;//faktura lub pismo lub protokół zniszczenia
    private String date;//pewnie glowny argument wyszukiwarki
    private int amountOfBooks;

    private static final long serialVersionUID = 4689966315224392110L;

    public BookOwnershipRecord(Book book)
    {
        this.book = book;
        recordType = RecordType.Unknown;
        previousOwner = "";
        nextOwner = "";
        documentNumber = "";
        date = "";
        amountOfBooks = 0;
    }

    public enum RecordType{
        Unknown, Received, Given, Destroyed
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public RecordType getRecordType() {
        return recordType;
    }

    public void setRecordType(RecordType recordType) {
        this.recordType = recordType;
    }

    public String getPreviousOwner() {
        return previousOwner;
    }

    public void setPreviousOwner(String previousOwner) {
        this.previousOwner = previousOwner;
    }

    public String getNextOwner() {
        return nextOwner;
    }

    public void setNextOwner(String nextOwner) {
        this.nextOwner = nextOwner;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmountOfBooks() {
        return amountOfBooks;
    }

    public void setAmountOfBooks(int amountOfBooks) {
        this.amountOfBooks = amountOfBooks;
    }
}
