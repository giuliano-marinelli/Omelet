package omelet.table;

import java.util.LinkedList;

/**
 *
 * @author Giuliano Marinelli
 */
public class Database {

    private String name;
    private LinkedList<Table> tables;

    public Database(String name, LinkedList<Table> tables) {
        this.name = name;
        this.tables = tables;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<Table> getTables() {
        return tables;
    }

    public void setTables(LinkedList<Table> tables) {
        this.tables = tables;
    }

    @Override
    public String toString() {
        String result = "";
        result += "Tables: \n";
        for (Table table : tables) {
            result += " " + table.getName() + "\n";
            for (Column column : table.getColumns()) {
                if (column.isPrimaryKey() && column.isForeignKey()) {
                    result += "\033[33m    -" + column.getName() + "\033[30m\n";
                } else if (column.isPrimaryKey()) {
                    result += "\033[32m    -" + column.getName() + "\033[30m\n";
                } else if (column.isForeignKey()) {
                    result += "\033[31m    -" + column.getName() + "\033[30m\n";
                } else {
                    result += "    -" + column.getName() + "\n";
                }
            }
        }
        return result;
    }

    public String toStringNoColor() {
        String result = "";
        result += "Tables: \n";
        for (Table table : tables) {
            result += " " + table.getName() + "\n";
            for (Column column : table.getColumns()) {
                if (column.isPrimaryKey() && column.isForeignKey()) {
                    result += "    -" + column.getName() + " FK PK";
                } else if (column.isPrimaryKey()) {
                    result += "    -" + column.getName() + " PK";
                } else if (column.isForeignKey()) {
                    result += "    -" + column.getName() + " FK";
                } else {
                    result += "    -" + column.getName();
                }
                result += "\n";
            }
        }
        return result;
    }

}
