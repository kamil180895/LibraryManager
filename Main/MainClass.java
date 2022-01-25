package Main;

import Library.Library;
import LibraryGUI.LibraryGUI;

public class MainClass {
    public static void main(String[] args)
    {
        Library library = new Library();
        //library.loadFromExcelFile();
        //library.saveToFile();
        library.loadFromFile();
        LibraryGUI libraryGUI = new LibraryGUI(library);
        libraryGUI.initialize();
    }
}
