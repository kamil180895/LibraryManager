package Library;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Library {
    private ArrayList<BookRecord> bookRecords;
    private ArrayList<BookOwnershipRecord> ownershipHistory;
    private ArrayList<BorrowRecord> borrowHistory;
    private ArrayList<CurrentBorrowRecord> currentBorrows;
    private int lastFreeLibrarianNumber;

    public Library() {

        bookRecords = new ArrayList<>();
        ownershipHistory = new ArrayList<>();
        borrowHistory = new ArrayList<>();
        currentBorrows = new ArrayList<>();
        lastFreeLibrarianNumber = 0;
    }

    public ArrayList<BookRecord> getBookRecords() {
        return bookRecords;
    }

    public ArrayList<BookOwnershipRecord> getOwnershipHistory() {
        return ownershipHistory;
    }

    public ArrayList<BorrowRecord> getBorrowHistory() {
        return borrowHistory;
    }

    public ArrayList<CurrentBorrowRecord> getCurrentBorrows() {
        return currentBorrows;
    }

    public String getTodayDate()
    {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    public int getLastFreeLibrarianNumber() {
        return lastFreeLibrarianNumber;
    }

    public void setLastFreeLibrarianNumber(int lastFreeLibrarianNumber) {
        this.lastFreeLibrarianNumber = lastFreeLibrarianNumber;
    }

    private BookRecord findBookRecord(Book book) {
        for (BookRecord bookRecord : bookRecords) {
            if (bookRecord.getBook().equals(book)) {
                return bookRecord;
            }
        }
        return null;
    }

    private CurrentBorrowRecord findCurrentBorrowRecord(Book book) {
        for (CurrentBorrowRecord bookRecord : currentBorrows) {
            if (bookRecord.getBook().equals(book)) {
                return bookRecord;
            }
        }
        return null;
    }

    public boolean addBook(BookOwnershipRecord record) {
        if (!record.getRecordType().equals(BookOwnershipRecord.RecordType.Received)) {
            return false;
        }
        if(record.getAmountOfBooks() <= 0)
        {
            return false;
        }
        ownershipHistory.add(record);
        BookRecord bookRecord = findBookRecord(record.getBook());
        if (bookRecord != null) {
            bookRecord.setAmountOwn(bookRecord.getAmountOwn() + record.getAmountOfBooks());
            bookRecord.setAmountAvailable(bookRecord.getAmountAvailable() + record.getAmountOfBooks());
            return true;
        }

        bookRecord = new BookRecord(record.getBook());
        bookRecord.setAmountOwn(record.getAmountOfBooks());
        bookRecord.setAmountAvailable(record.getAmountOfBooks());
        bookRecords.add(bookRecord);
        return true;
    }

    public boolean editBook(Book oldBook, Book newBook)
    {
        Book bookInDatabase = null;
        if(oldBook == null)
            return false;
        for(BookRecord bookRecord : bookRecords)
        {
            if(bookRecord.getBook().equals(oldBook))
            {
                bookInDatabase = bookRecord.getBook();
                break;
            }
        }
        if(bookInDatabase == null)
            return false;

        bookInDatabase.setTitle(newBook.getTitle());
        bookInDatabase.setSignature(newBook.getSignature());
        bookInDatabase.setJimNumber(newBook.getJimNumber());
        bookInDatabase.setLibrarianNumber(newBook.getLibrarianNumber());
        bookInDatabase.setReleaseYear(newBook.getReleaseYear());
        bookInDatabase.setMeasureUnit(newBook.getMeasureUnit());
        bookInDatabase.setComment(newBook.getComment());
        return true;
    }

    public boolean removeBook(BookOwnershipRecord record) {
        if (!record.getRecordType().equals(BookOwnershipRecord.RecordType.Given) && !record.getRecordType().equals(BookOwnershipRecord.RecordType.Destroyed)) {
            return false;
        }
        BookRecord bookRecord = findBookRecord(record.getBook());
        if (bookRecord == null) {
            return false;
        }
        if (bookRecord.getAmountAvailable() < record.getAmountOfBooks()) {
            return false;
        }
        bookRecord.setAmountOwn(bookRecord.getAmountOwn() - record.getAmountOfBooks());
        bookRecord.setAmountAvailable(bookRecord.getAmountAvailable() - record.getAmountOfBooks());
        if(bookRecord.getAmountOwn() == 0)
            bookRecords.remove(bookRecord);
        ownershipHistory.add(record);
        return true;
    }

    public boolean borrowBook(BorrowRecord record) {
        if (!record.getRecordType().equals(BorrowRecord.RecordType.Borrowed)) {
            return false;
        }
        BookRecord bookRecord = findBookRecord(record.getBook());
        if (bookRecord == null) {
            return false;
        }
        record.setBook(bookRecord.getBook());
        if (bookRecord.getAmountAvailable() < record.getAmount() || record.getAmount() <= 0) {
            return false;
        }
        borrowHistory.add(record);
        currentBorrows.add(new CurrentBorrowRecord(record));
        bookRecord.setAmountAvailable(bookRecord.getAmountAvailable() - record.getAmount());
        return true;
    }

    public boolean returnBook(BorrowRecord record) {
        if (!record.getRecordType().equals(BorrowRecord.RecordType.Returned)) {
            return false;
        }
        CurrentBorrowRecord currentBorrowRecord = findCurrentBorrowRecord(record.getBook());
        if (currentBorrowRecord == null) {
            return false;
        }
        if (currentBorrowRecord.getAmountToReturn() < record.getAmount() || record.getAmount() <= 0) {
            return false;
        }
        BookRecord bookRecord = findBookRecord(record.getBook());
        if(bookRecord == null)
        {
            return false;
        }
        borrowHistory.add(record);
        bookRecord.setAmountAvailable(bookRecord.getAmountAvailable() + record.getAmount());
        if (currentBorrowRecord.getAmountToReturn() > record.getAmount()) {
            currentBorrowRecord.setAmountToReturn(currentBorrowRecord.getAmountToReturn() - record.getAmount());
            return true;
        }

        currentBorrows.remove(currentBorrowRecord);
        return true;
    }



    public Object[][] convertAllBooksToRowData()
    {
        ArrayList<BookRecord> records = getBookRecords();
        Object[][] result = new Object[records.size()][9];
        for(int i=0; i<records.size(); ++i)
        {
            BookRecord bookRecord = records.get(i);
            result[i] = convertBookToRowData(bookRecord);
        }
        return result;
    }

    private Object[] convertBookToRowData(BookRecord bookRecord)
    {
        Object[] result = new Object[9];

        result[0] = bookRecord.getBook().getJimNumber();
        result[1] = bookRecord.getBook().getSignature();
        result[2] = bookRecord.getBook().getTitle();
        result[3] = bookRecord.getBook().getLibrarianNumber();
        result[4] = bookRecord.getBook().getReleaseYear();
        result[5] = bookRecord.getBook().getMeasureUnit();
        result[6] = bookRecord.getAmountOwn();
        result[7] = bookRecord.getAmountAvailable();
        result[8] = bookRecord.getBook().getComment();
        return result;
    }

    public Object[][] convertAllOwnershipRecordsToRowData()
    {
        ArrayList<BookOwnershipRecord> records = getOwnershipHistory();
        Object[][] result = new Object[records.size()][9];
        for(int i=0; i<records.size(); ++i)
        {
            BookOwnershipRecord ownershipRecord = records.get(i);

            result[i][0] = ownershipRecord.getBook().getJimNumber();
            result[i][1] = ownershipRecord.getBook().getSignature();
            result[i][2] = ownershipRecord.getBook().getTitle();
            result[i][3] = ownershipRecord.getBook().getLibrarianNumber();
            if(ownershipRecord.getRecordType().equals(BookOwnershipRecord.RecordType.Received))
            {
                result[i][4] = "Przychód";
                result[i][5] = ownershipRecord.getPreviousOwner();
            }
            else if(ownershipRecord.getRecordType().equals(BookOwnershipRecord.RecordType.Given))
            {
                result[i][4] = "Rozchód";
                result[i][5] = ownershipRecord.getNextOwner();
            }
            else if(ownershipRecord.getRecordType().equals(BookOwnershipRecord.RecordType.Destroyed))
            {
                result[i][4] = "Zniszczono";
                result[i][5] = " - ";
            }
            result[i][6] = ownershipRecord.getDocumentNumber();
            result[i][7] = ownershipRecord.getDate();
            result[i][8] = ownershipRecord.getAmountOfBooks();
        }
        return result;
    }

    public Object[][] convertAllBorrowRecordsToRowData()
    {
        Object[][] result = new Object[borrowHistory.size()][9];

        for(int i=0; i<borrowHistory.size(); ++i)
        {
            BorrowRecord borrowRecord = borrowHistory.get(i);
            result[i][0] = borrowRecord.getLastName();
            result[i][1] = borrowRecord.getFirstName();
            result[i][2] = borrowRecord.getBook().getJimNumber();
            result[i][3] = borrowRecord.getBook().getSignature();
            result[i][4] = borrowRecord.getBook().getTitle();
            result[i][5] = borrowRecord.getBook().getLibrarianNumber();
            result[i][6] = borrowRecord.getDate();
            result[i][7] = borrowRecord.getAmount();
            if(borrowRecord.getRecordType().equals(BorrowRecord.RecordType.Borrowed))
                result[i][8] = "Wypożyczono";
            else if(borrowRecord.getRecordType().equals(BorrowRecord.RecordType.Returned))
                result[i][8] = "Zwrócono";
        }
        return result;
    }

    public Object[][] convertAllCurrentBorrowRecordsToRowData()
    {
        Object[][] result = new Object[currentBorrows.size()][8];

        for(int i=0; i<currentBorrows.size(); ++i)
        {
            CurrentBorrowRecord currentBorrowRecord = currentBorrows.get(i);
            result[i][0] = currentBorrowRecord.getLastName();
            result[i][1] = currentBorrowRecord.getFirstName();
            result[i][2] = currentBorrowRecord.getBook().getJimNumber();
            result[i][3] = currentBorrowRecord.getBook().getSignature();
            result[i][4] = currentBorrowRecord.getBook().getTitle();
            result[i][5] = currentBorrowRecord.getBook().getLibrarianNumber();
            result[i][6] = currentBorrowRecord.getDate();
            result[i][7] = currentBorrowRecord.getAmountToReturn();
        }
        return result;
    }

    private int compareString(String string1, String string2)
    {
        int result = 0;
        string1 = string1.toLowerCase();
        string2 = string2.toLowerCase();
        string1 = string1.replaceAll("\\s+","");
        string2 = string2.replaceAll("\\s+","");
        for(int i=0; i<Math.min(string1.length(), string2.length());++i)
        {
            if(string1.charAt(i) == string2.charAt(i))
                ++result;
        }

        return result;
    }

    public Object[][] findBookByTitle(String title)
    {
        ArrayList<SortPair<BookRecord>> firstResults = new ArrayList<>();
        for(int i=0; i<bookRecords.size(); ++i)
        {
            firstResults.add(new SortPair<>(bookRecords.get(i), compareString(bookRecords.get(i).getBook().getTitle(), title)));
        }

        Collections.sort(firstResults);

        int finalResultSize = 0;
        while(finalResultSize<firstResults.size() && firstResults.get(finalResultSize++).sortValue >= title.length()/2);

        Object[][] finalResult = new Object[finalResultSize][];

        for(int i=0; i<finalResultSize; ++i)
        {
            finalResult[i] = convertBookToRowData(firstResults.get(i).value);
        }

        return finalResult;
    }

    public Object[][] findBookBySignature(String signature)
    {
        ArrayList<SortPair<BookRecord>> firstResults = new ArrayList<>();
        for(int i=0; i<bookRecords.size(); ++i)
        {
            firstResults.add(new SortPair<>(bookRecords.get(i), compareString(bookRecords.get(i).getBook().getSignature(), signature)));
        }

        Collections.sort(firstResults);

        int finalResultSize = 0;
        while(finalResultSize<firstResults.size() && firstResults.get(finalResultSize++).sortValue >= signature.length()/2);

        Object[][] finalResult = new Object[finalResultSize][];

        for(int i=0; i<finalResultSize; ++i)
        {
            finalResult[i] = convertBookToRowData(firstResults.get(i).value);
        }

        return finalResult;
    }

    public Object[][] findBookByLibrarianNumber(String librarianNumber)
    {
        ArrayList<SortPair<BookRecord>> firstResults = new ArrayList<>();
        for(int i=0; i<bookRecords.size(); ++i)
        {
            firstResults.add(new SortPair<>(bookRecords.get(i), compareString(String.valueOf(bookRecords.get(i).getBook().getLibrarianNumber()), librarianNumber)));
        }

        Collections.sort(firstResults);

        int finalResultSize = 0;
        while(finalResultSize<firstResults.size() && firstResults.get(finalResultSize++).sortValue >= librarianNumber.length()/2);

        Object[][] finalResult = new Object[finalResultSize][];

        for(int i=0; i<finalResultSize; ++i)
        {
            finalResult[i] = convertBookToRowData(firstResults.get(i).value);
        }

        return finalResult;
    }

    public Object[][] findBookByJIM(String jim)
    {
        ArrayList<SortPair<BookRecord>> firstResults = new ArrayList<>();
        for(int i=0; i<bookRecords.size(); ++i)
        {
            firstResults.add(new SortPair<>(bookRecords.get(i), compareString(bookRecords.get(i).getBook().getJimNumber(), jim)));
        }

        Collections.sort(firstResults);

        int finalResultSize = 0;
        while(finalResultSize<firstResults.size() && firstResults.get(finalResultSize++).sortValue >= jim.length()/2);

        Object[][] finalResult = new Object[finalResultSize][];

        for(int i=0; i<finalResultSize; ++i)
        {
            finalResult[i] = convertBookToRowData(firstResults.get(i).value);
        }

        return finalResult;
    }

    public void saveToFile()
    {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("library.data")))
        {
            output.writeObject(lastFreeLibrarianNumber);
            output.writeObject(bookRecords);
            output.writeObject(ownershipHistory);
            output.writeObject(borrowHistory);
            output.writeObject(currentBorrows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile()
    {
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream("library.data"))) {
            Object object;
            object = input.readObject();
            lastFreeLibrarianNumber = (int)object;
            object = input.readObject();
            bookRecords = (ArrayList<BookRecord>)object;
            object = input.readObject();
            ownershipHistory = (ArrayList<BookOwnershipRecord>)object;
            object = input.readObject();
            borrowHistory = (ArrayList<BorrowRecord>)object;
            object = input.readObject();
            currentBorrows = (ArrayList<CurrentBorrowRecord>)object;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadFromExcelFile()
    {
        try
        {
            File file = new File("wydawnictwa.xlsx");   //creating a new file instance
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file
            //creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object <-- ustalic sheetnumber
            Iterator<Row> itr = sheet.iterator();    //iterating over excel file
            itr.next();
            while (itr.hasNext())
            {
                Cell cell;
                Row row = itr.next();
                Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column
                Book book = new Book();
                BookOwnershipRecord bookOwnershipRecord = new BookOwnershipRecord(book);
                bookOwnershipRecord.setRecordType(BookOwnershipRecord.RecordType.Received);
                bookOwnershipRecord.setDocumentNumber("Stan początkowy");
                bookOwnershipRecord.setDate(getTodayDate());

                book.setSignature(cellIterator.next().getStringCellValue());
                cell = cellIterator.next();//problem z typami
                if(cell.getCellTypeEnum().equals(CellType.NUMERIC))
                {
                    book.setJimNumber(String.valueOf(cell.getNumericCellValue()));
                }
                else {
                    book.setJimNumber(cell.getStringCellValue());
                }
                book.setTitle(cellIterator.next().getStringCellValue());

                cell = cellIterator.next();//problem z typami
                if(cell.getCellTypeEnum().equals(CellType.NUMERIC))
                {
                    book.setLibrarianNumber((int)cell.getNumericCellValue());
                }
                else {
                    book.setLibrarianNumber(Integer.parseInt(cell.getStringCellValue()));
                }

                cell = cellIterator.next();//problem z typami
                if(cell.getCellTypeEnum().equals(CellType.NUMERIC))
                {
                    bookOwnershipRecord.setPreviousOwner(String.valueOf(cell.getNumericCellValue()));
                }
                else
                {
                    bookOwnershipRecord.setPreviousOwner(cell.getStringCellValue());
                }
                bookOwnershipRecord.setAmountOfBooks((int)cellIterator.next().getNumericCellValue());
                book.setMeasureUnit(cellIterator.next().getStringCellValue());
                book.setReleaseYear((int)cellIterator.next().getNumericCellValue());

                if(cellIterator.hasNext())
                {
                    book.setComment(cellIterator.next().getStringCellValue());
                }

                addBook(bookOwnershipRecord);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean exportToExcel()
    {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Baza_wydawnictw");
        for(int i=0; i<bookRecords.size(); ++i)
        {
            int columnCout = 0;
            XSSFRow row = sheet.createRow(i);
            row.createCell(columnCout++).setCellValue(bookRecords.get(i).getBook().getJimNumber());
            row.createCell(columnCout++).setCellValue(bookRecords.get(i).getBook().getSignature());
            row.createCell(columnCout++).setCellValue(bookRecords.get(i).getBook().getTitle());
            row.createCell(columnCout++).setCellValue(bookRecords.get(i).getBook().getLibrarianNumber());
            row.createCell(columnCout++).setCellValue(bookRecords.get(i).getBook().getReleaseYear());
            row.createCell(columnCout++).setCellValue(bookRecords.get(i).getBook().getMeasureUnit());
            row.createCell(columnCout++).setCellValue(bookRecords.get(i).getAmountOwn());
            row.createCell(columnCout++).setCellValue(bookRecords.get(i).getAmountAvailable());
            row.createCell(columnCout++).setCellValue(bookRecords.get(i).getBook().getComment());
        }

        sheet = workbook.createSheet("Historia_posiadania");
        for(int i=0; i<ownershipHistory.size(); ++i)
        {
            int columnCout = 0;
            XSSFRow row = sheet.createRow(i);
            row.createCell(columnCout++).setCellValue(ownershipHistory.get(i).getBook().getJimNumber());
            row.createCell(columnCout++).setCellValue(ownershipHistory.get(i).getBook().getSignature());
            row.createCell(columnCout++).setCellValue(ownershipHistory.get(i).getBook().getTitle());
            row.createCell(columnCout++).setCellValue(ownershipHistory.get(i).getBook().getLibrarianNumber());
            if(ownershipHistory.get(i).getRecordType().equals(BookOwnershipRecord.RecordType.Received))
            {
                row.createCell(columnCout++).setCellValue("Przychód");
                row.createCell(columnCout++).setCellValue(ownershipHistory.get(i).getPreviousOwner());

            }
            else if(ownershipHistory.get(i).getRecordType().equals(BookOwnershipRecord.RecordType.Given))
            {
                row.createCell(columnCout++).setCellValue("Rozchód");
                row.createCell(columnCout++).setCellValue(ownershipHistory.get(i).getNextOwner());
            }
            else if(ownershipHistory.get(i).getRecordType().equals(BookOwnershipRecord.RecordType.Destroyed))
            {
                row.createCell(columnCout++).setCellValue("Zniszczono");
                row.createCell(columnCout++).setCellValue("");
            }
            row.createCell(columnCout++).setCellValue(ownershipHistory.get(i).getDocumentNumber());
            row.createCell(columnCout++).setCellValue(ownershipHistory.get(i).getDate());
            row.createCell(columnCout++).setCellValue(ownershipHistory.get(i).getAmountOfBooks());
        }

        sheet = workbook.createSheet("Historia_wypożyczeń");
        for(int i=0; i<borrowHistory.size(); ++i)
        {
            int columnCout = 0;
            XSSFRow row = sheet.createRow(i);
            row.createCell(columnCout++).setCellValue(borrowHistory.get(i).getLastName());
            row.createCell(columnCout++).setCellValue(borrowHistory.get(i).getFirstName());
            row.createCell(columnCout++).setCellValue(borrowHistory.get(i).getBook().getJimNumber());
            row.createCell(columnCout++).setCellValue(borrowHistory.get(i).getBook().getSignature());
            row.createCell(columnCout++).setCellValue(borrowHistory.get(i).getBook().getTitle());
            row.createCell(columnCout++).setCellValue(borrowHistory.get(i).getBook().getLibrarianNumber());
            row.createCell(columnCout++).setCellValue(borrowHistory.get(i).getDate());
            row.createCell(columnCout++).setCellValue(borrowHistory.get(i).getAmount());
            if(borrowHistory.get(i).getRecordType().equals(BorrowRecord.RecordType.Returned))
                row.createCell(columnCout++).setCellValue("Zwrócono");
            else if(borrowHistory.get(i).getRecordType().equals(BorrowRecord.RecordType.Borrowed))
                row.createCell(columnCout++).setCellValue("Wypożyczono");
        }

        sheet = workbook.createSheet("Aktualne_wypożyczenia");
        for(int i=0; i<currentBorrows.size(); ++i)
        {
            int columnCout = 0;
            XSSFRow row = sheet.createRow(i);
            row.createCell(columnCout++).setCellValue(currentBorrows.get(i).getLastName());
            row.createCell(columnCout++).setCellValue(currentBorrows.get(i).getFirstName());
            row.createCell(columnCout++).setCellValue(currentBorrows.get(i).getBook().getJimNumber());
            row.createCell(columnCout++).setCellValue(currentBorrows.get(i).getBook().getSignature());
            row.createCell(columnCout++).setCellValue(currentBorrows.get(i).getBook().getTitle());
            row.createCell(columnCout++).setCellValue(currentBorrows.get(i).getBook().getLibrarianNumber());
            row.createCell(columnCout++).setCellValue(currentBorrows.get(i).getDate());
            row.createCell(columnCout++).setCellValue(currentBorrows.get(i).getAmountToReturn());
        }

        try (FileOutputStream outputStream = new FileOutputStream("Baza_danych.xlsx")) {
            workbook.write(outputStream);
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
