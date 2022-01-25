package Library;

import java.io.Serializable;
import java.util.Objects;

public class Book implements Serializable {
    private String title;
    private String signature;//moze nie byc przypisana
    private String jimNumber;//moze nie byc przypisany
    private int librarianNumber;
    private int releaseYear;
    private String measureUnit;
    private String comment;

    private static final long serialVersionUID = 4689966415224392109L;

    public Book()
    {
        title = "";
        signature = "";
        jimNumber = "";
        releaseYear = -1;
        librarianNumber = -1;
        measureUnit = "";
        comment = "";
    }

    public String getTitle() {
        return title;
    }

    public String getSignature() {
        return signature;
    }

    public String getJimNumber() {
        return jimNumber;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public int getLibrarianNumber() {
        return librarianNumber;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setJimNumber(String jimNumber) {
        this.jimNumber = jimNumber;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setLibrarianNumber(int librarianNumber) {
        this.librarianNumber = librarianNumber;
    }

    public void setMeasureUnit(String measureUnit)
    {
        this.measureUnit = measureUnit;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {// byc moze lepsza bedzie bardziej liberalna implementacja
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return jimNumber.equals(book.jimNumber) &&
                librarianNumber == book.librarianNumber &&
                title.equals(book.title) &&
                signature.equals(book.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, signature, jimNumber, librarianNumber);
    }
}
