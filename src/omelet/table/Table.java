package omelet.table;

import omelet.semantic.Element;
import java.util.LinkedList;

/**
 *
 * @author Giuliano
 */
public class Table {

    private String name;
    private LinkedList<Column> columns;
    private Element semanticRef;

    public Table(String name, LinkedList<Column> columns, Element semanticRef) {
        this.name = name;
        this.columns = columns;
        this.semanticRef = semanticRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<Column> getColumns() {
        return columns;
    }

    public void setColumns(LinkedList<Column> columns) {
        this.columns = columns;
    }

    public Element getSemanticRef() {
        return semanticRef;
    }

    public void setSemanticRef(Element semanticRef) {
        this.semanticRef = semanticRef;
    }
    
    public LinkedList<Column> getKeys() {
        LinkedList<Column> keyColumns = new LinkedList<>();
        for (Column column : columns) {
            if (column.isPrimaryKey()) {
                keyColumns.add(column);
            }
        }
        return keyColumns;
    }

}
