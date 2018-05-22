package omelet.table;

import omelet.semantic.Attribute;

/**
 *
 * @author Giuliano Marinelli
 */
public class Column {

    private String name;
    private String dataType;
    private boolean autoincrement;
    private boolean notNull;
    private boolean unique;
    private boolean primaryKey;
    private boolean foreignKey;
    private Table referenceTable;
    private Column referenceColumn;
    private Attribute semanticRef;

    public Column(String name, String dataType, boolean isPrimaryKey, Attribute semanticRef) {
        this.name = name;
        this.dataType = dataType;
        this.primaryKey = isPrimaryKey;
        this.semanticRef = semanticRef;
    }

    public Column(String name, String dataType, boolean primaryKey, boolean foreignKey, Table referenceTable, Column referenceColumn, Attribute semanticRef) {
        this.name = name;
        this.dataType = dataType;
        this.primaryKey = primaryKey;
        this.foreignKey = foreignKey;
        this.referenceTable = referenceTable;
        this.referenceColumn = referenceColumn;
        this.semanticRef = semanticRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isAutoincrement() {
        return autoincrement;
    }

    public void setAutoincrement(boolean autoincrement) {
        this.autoincrement = autoincrement;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(boolean foreignKey) {
        this.foreignKey = foreignKey;
    }

    public Table getReferenceTable() {
        return referenceTable;
    }

    public void setReferenceTable(Table referenceTable) {
        this.referenceTable = referenceTable;
    }

    public Column getReferenceColumn() {
        return referenceColumn;
    }

    public void setReferenceColumn(Column referenceColumn) {
        this.referenceColumn = referenceColumn;
    }

    public Attribute getSemanticRef() {
        return semanticRef;
    }

    public void setSemanticRef(Attribute semanticRef) {
        this.semanticRef = semanticRef;
    }

    public boolean equals(Column otherColumn) {
        boolean result = false;
        if (name.equals(otherColumn.getName())) {
            result = true;
        }
        return result;
    }

}
