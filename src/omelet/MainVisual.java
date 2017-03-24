package omelet;

import java.util.Iterator;
import omelet.table.*;
import omelet.semantic.*;
import java.util.LinkedList;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

/**
 *
 * @author Giuliano
 */
public class MainVisual extends Main {

    public static Database generateDatabase(Diagram diagram, LinkedList<ButtonGroup> optionMultivalued, LinkedList<ButtonGroup> optionOneToOne) {
        //System.out.print("Enter name of database: ");
        String nombreDB = "a";//TecladoIn.readLine();
        LinkedList<Table> tables = new LinkedList<>();
        Database database = new Database(nombreDB, tables);
        for (Entity entity : diagram.getEntities()) {
            doEntity(entity, database, diagram);
        }
        for (Table table : tables) {
            doInheritances(table, database, diagram);
        }
        for (Relationship relationship : diagram.getRelationships()) {
            doWeakRelationship(relationship, database, diagram);
        }
        Iterator<ButtonGroup> iteratorMultivalued = optionMultivalued.iterator();
        for (Attribute attribute : diagram.getAttributes()) {
            iteratorMultivalued.hasNext();
            doMultivalued(attribute, database, diagram, iteratorMultivalued.next());
        }
        Iterator<ButtonGroup> iteratorOneToOne = optionOneToOne.iterator();
        for (Relationship relationship : diagram.getRelationships()) {
            iteratorOneToOne.hasNext();
            doRelationship(relationship, database, diagram, iteratorOneToOne.next());
        }
        return database;
    }

    public static void doMultivalued(Attribute attribute, Database database, Diagram diagram, ButtonGroup optionMultivalued) {
        if (attribute.isMultivalued()) {
            Table multivaluedTable = null;
            Column multivaluedKey = null;
            boolean exists = false;
            for (Table table : database.getTables()) {
                if (table.getName().equals(attribute.getName())) {
                    exists = true;
                    multivaluedTable = table;
                    for (Column column : multivaluedTable.getKeys()) {
                        multivaluedKey = column;
                    }
                }
            }
            if (!exists) {
                LinkedList<Column> columns = new LinkedList<>();
                multivaluedTable = new Table(attribute.getName(), columns, null);
                database.getTables().add(multivaluedTable);
                multivaluedKey = new Column("id_" + attribute.getName(), "int", true, null);
                columns.add(multivaluedKey);
                columns.add(new Column(attribute.getMultivaluedValue(), attribute.getDataType(), false, null));
            }
            Entity entity;
            for (Connector connector : diagram.getConnectors()) {
                if ((entity = connector.connectEntity(attribute)) != null) {
                    int decision = optionMultivalued.getSelection().getMnemonic();
                    Table entityTable;
                    String multivaluedTableKeyName;
                    switch (decision) {
                        case 1:
                            entityTable = findTableWithEntity(entity, database);
                            multivaluedTableKeyName = structureName(multivaluedKey, multivaluedTable, null, connector);
                            entityTable.getColumns().add(
                                    new Column(multivaluedTableKeyName, multivaluedKey.getDataType(), false, true, multivaluedTable, multivaluedKey, multivaluedKey.getSemanticRef()));
                            break;
                        case 2:
                            entityTable = findTableWithEntity(entity, database);
                            LinkedList<Column> relationshipColumns = new LinkedList<>();
                            Table relationshipTable = new Table(entity.getName() + "_" + attribute.getName(), relationshipColumns, null);
                            database.getTables().add(relationshipTable);
                            multivaluedTableKeyName = structureName(multivaluedKey, multivaluedTable, null, connector);
                            relationshipTable.getColumns().add(
                                    new Column(multivaluedTableKeyName, multivaluedKey.getDataType(), true, true, multivaluedTable, multivaluedKey, multivaluedKey.getSemanticRef()));
                            String entityTableKeyName;
                            for (Column columnKey : entityTable.getKeys()) {
                                entityTableKeyName = structureName(columnKey, entityTable, null, connector);
                                relationshipTable.getColumns().add(
                                        new Column(entityTableKeyName, columnKey.getDataType(), true, true, entityTable, columnKey, columnKey.getSemanticRef()));
                            }
                            break;
                        default:
                            System.err.println("You must enter 1 or 2 (integer numbers). Pleas re-enter the number: ");
                            break;
                    }
                }
            }
        }
    }

    public static void doRelationship(Relationship relationship, Database database, Diagram diagram, ButtonGroup optionOneToOne) {
        LinkedList<Table> tables = database.getTables();
        LinkedList<Column> columns = new LinkedList<>();
        Attribute attribute;
        Entity entity;
        LinkedList<Connector> relationshipConnectors = new LinkedList<>();
        boolean haveAttributes = false;
        int amountConnections = 0;
        int amountManyCardinalities = 0;
        for (Connector connectorRelationship : diagram.getConnectors()) {
            if ((attribute = connectorRelationship.connectAttribute(relationship)) != null) {
                haveAttributes = true;
                if (!attribute.isDerived() && !attribute.isMultivalued()) {
                    boolean compound = false;
                    Attribute attributeCompound;
                    for (Connector connectorAttribute : diagram.getConnectors()) {
                        if ((attributeCompound = connectorAttribute.connectAttribute(attribute)) != null) {
                            compound = true;
                            columns.add(new Column(attributeCompound.getName(), attributeCompound.getDataType(), attributeCompound.isKey() || attributeCompound.isWeakKey(), attributeCompound));
                        }
                    }
                    if (!compound) {
                        columns.add(new Column(attribute.getName(), attribute.getDataType(), attribute.isKey() || attribute.isWeakKey(), attribute));
                    }
                }
            }
            if (connectorRelationship.connectEntity(relationship) != null) {
                amountConnections++;
                if (connectorRelationship.getCardinality().isEmpty()) {
                    System.err.println("Una cardinalidad no esta definida, o "
                            + "fue definida en ambos lados de un conector");
                } else {
                    relationshipConnectors.add(connectorRelationship);
                    if (connectorRelationship.isManyCardinality()) {
                        amountManyCardinalities++;
                    }
                }
            }
        }
        if (haveAttributes || amountConnections > 2 || amountManyCardinalities > 1) {
            doRelationshipManyMany(relationship, columns, tables, relationshipConnectors, database);
        } else if (amountManyCardinalities == 1) {
            doRelationshipOneMany(relationship, relationshipConnectors, database, diagram);
        } else if (amountManyCardinalities == 0) {
            doRelationshipOneOne(relationship, relationshipConnectors, database, optionOneToOne);
        }
    }

    public static void doRelationshipOneOne(Relationship relationship,
            LinkedList<Connector> relationshipConnectors, Database database, ButtonGroup optionOneToOne) {
        LinkedList<Table> relationshipTables = new LinkedList<>();
        Entity entity;
        for (Connector connector : relationshipConnectors) {
            entity = connector.connectEntity(relationship);
            relationshipTables.add(findTableWithEntity(entity, database));
        }
        int decision = optionOneToOne.getSelection().getMnemonic();
        switch (decision) {
            case 1:
                for (Column column : relationshipTables.getLast().getKeys()) {
                    String finalName = structureName(column, relationshipTables.getLast(), relationship, relationshipConnectors.getLast());
                    relationshipTables.getFirst().getColumns().add(
                            new Column(finalName, column.getDataType(), false, true, relationshipTables.getLast(), column, column.getSemanticRef()));
                }
                break;
            case 2:
                for (Column column : relationshipTables.getFirst().getKeys()) {
                    String finalName = structureName(column, relationshipTables.getFirst(), relationship, relationshipConnectors.getFirst());
                    relationshipTables.getLast().getColumns().add(
                            new Column(finalName, column.getDataType(), false, true, relationshipTables.getFirst(), column, column.getSemanticRef()));
                }
                break;
            default:
                System.err.println("You must enter 1 or 2 (integer numbers). Pleas re-enter the number: ");
                break;
        }
    }

}
