package LibraryGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public abstract class ViewWithTable extends View{
    protected JTable table;
    protected String[] columnNames;

    public ViewWithTable(LibraryGUI libraryGUI) {
        super(libraryGUI);
        table = new JTable();
    }

    public void updateTable(Object[][] newValues)
    {
        ((DefaultTableModel)table.getModel()).setDataVector(newValues, columnNames);
        ((DefaultTableModel)table.getModel()).fireTableDataChanged();
    }

    public JPanel drawDefaultTable()
    {
        JPanel tablePanel = new JPanel(new BorderLayout());
        table.setDefaultEditor(Object.class, null);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane tableScroll = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        tableScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tableScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tablePanel.add(tableScroll);

        return tablePanel;
    }
}
