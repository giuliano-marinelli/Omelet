package omelet;

import omelet.table.Column;
import omelet.table.Table;
import omelet.table.Database;
import omelet.semantic.Diagram;
import omelet.semantic.Relationship;
import omelet.semantic.Inheritance;
import omelet.semantic.Entity;
import omelet.semantic.Attribute;
import omelet.semantic.Connector;
import omelet.graphic.LineConnector;
import omelet.graphic.RectangleElement;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import tecladoIn.TecladoIn;

/**
 *
 * @author Giuliano
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Diagram diagram = generateDiagram("src/res/test.uxf");
        //System.out.println(diagram.toString());
        Database database = generateDatabase(diagram);
        //System.out.println(database.toString());
        String sql = generateSQL(database);
        //System.out.println(sql);
        try {
            PrintWriter writer = new PrintWriter("src/res/" + database.getName() + ".sql", "UTF-8");
            writer.println(sql);
            writer.close();
        } catch (IOException e) {
            System.err.println("A problem in the creation of SQL file.");
        }
    }

    /**
     * Genera un objeto sematico diagrama que contiene todas las entidades,
     * relaciones, atributos, herencias y conectores que posea el archivo.
     *
     * @param file
     * @return
     */
    public static Diagram generateDiagram(String file) {
        Diagram diagram = new Diagram();

        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(file);

        try {
            //Listas de elementos semanticos
            LinkedList<Entity> entities = new LinkedList<>();
            LinkedList<Relationship> relationships = new LinkedList<>();
            LinkedList<Attribute> attributes = new LinkedList<>();
            LinkedList<Inheritance> inheritances = new LinkedList<>();
            LinkedList<Connector> connectors = new LinkedList<>();

            //Listas de elementos graficos
            LinkedList<RectangleElement> rectangles = new LinkedList<>();
            LinkedList<LineConnector> lines = new LinkedList<>();

            //Abrir el documento
            Document document = (Document) builder.build(xmlFile);
            Element rootNode = document.getRootElement();
            List list = rootNode.getChildren("element");

            //Parametros de cada <element> del documento
            String id = "";
            String type = "";
            String customCode = "";
            int idType;
            int x;
            int y;
            int w;
            int h;
            String panelAttributes;
            String additAttributes;

            //Recorrer cada <element>
            for (int i = 0; i < list.size(); i++) {
                Element node = (Element) list.get(i);

                Element coordenadas = node.getChild("coordinates");
                x = Integer.parseInt(coordenadas.getChildText("x"));
                y = Integer.parseInt(coordenadas.getChildText("y"));
                w = Integer.parseInt(coordenadas.getChildText("w"));
                h = Integer.parseInt(coordenadas.getChildText("h"));
                panelAttributes = node.getChildText("panel_attributes");
                additAttributes = node.getChildText("additional_attributes");

                if (node.getChild("custom_code") != null) {
                    customCode = node.getChild("custom_code").getText();
                    idType = getCustomCodeType(customCode);
                } else {
                    customCode = "";
                    idType = 0;
                }

                if (node.getChild("type") != null) {
                    type = node.getChild("type").getText();
                } else {
                    type = "";
                }

                if (node.getChild("id") != null) {
                    id = node.getChild("id").getText();
                } else {
                    id = "";
                }

                //DATOS DE UN <ELEMENT>
                /*System.out.println("    Datos:");
                System.out.println(
                        node.getName() + " " + i + ": \n"
                        + " DATOS: \n"
                        + "   type: " + type + "\n"
                        + "   id: " + id + "\n"
                        + "   x: " + x + "\n"
                        + "   y: " + y + "\n"
                        + "   w: " + w + "\n"
                        + "   h: " + h + "\n"
                        + "   panel attributes: " + panelAttributes.replace("\n", " ") + "\n"
                        + "   additional attributes: " + additAttributes
                );*/
                if (!type.equals("")) { //Si es un elemento Custom (entidad, relacion, atributo, herencia)
                    //Creo el rectangulo grafico para el elemento
                    Rectangle rectangle = new Rectangle(x, y, w, h);
                    //Creo el elemento semantico y finalmente el objeto con ambos
                    switch (idType) {
                        case 1:
                            entities.add(new Entity(panelAttributes));
                            rectangles.add(new RectangleElement(rectangle, entities.getLast()));
                            break;
                        case 2:
                            relationships.add(new Relationship(panelAttributes));
                            rectangles.add(new RectangleElement(rectangle, relationships.getLast()));
                            break;
                        case 3:
                            attributes.add(new Attribute(panelAttributes));
                            rectangles.add(new RectangleElement(rectangle, attributes.getLast()));
                            break;
                        case 4:
                            inheritances.add(new Inheritance(panelAttributes));
                            rectangles.add(new RectangleElement(rectangle, inheritances.getLast()));
                            break;
                    }

                } else { //Si es un elemento Relation (conector)
                    //El atributo adicional indica los puntos que tiene la linea
                    //relativos a la posicion absoluta (x,y), vienen de pares.
                    String[] points = additAttributes.split(";");
                    //Obtengo el primer punto que indicaria un extremo de la linea
                    Point p1 = new Point(
                            x + (int) Double.parseDouble(points[0]),
                            y + (int) Double.parseDouble(points[1]));
                    //Obtengo el ultimo punto que indicaria el otro extremo de la linea
                    Point p2 = new Point(
                            x + (int) Double.parseDouble(points[points.length - 2]),
                            y + (int) Double.parseDouble(points[points.length - 1]));
                    //Creo una linea directa entre estos dos puntos
                    //Mas adelante solo utilizo los puntos para intersectar y no
                    //la linea completa.
                    Line2D.Double line = new Line2D.Double(p1, p2);
                    //Creo el conector semantico
                    connectors.add(new Connector(panelAttributes));
                    lines.add(new LineConnector(line, connectors.getLast()));
                }
            }
            /*
            for (RectangleElement rectangle : rectangles) {
                System.out.println(rectangle.toString());
            }
             */
            for (LineConnector line : lines) {
                //System.out.println(line.toString());
                for (RectangleElement rectangle : rectangles) {
                    if (rectangle.getRectangle().contains(line.getLine().getP1())) {
                        //System.out.println("    intersect with " + rectangle.toString());
                        line.getConnector().setElement1(rectangle.getElement());
                    }
                    if (rectangle.getRectangle().contains(line.getLine().getP2())) {
                        //System.out.println("    intersect with " + rectangle.toString());
                        line.getConnector().setElement2(rectangle.getElement());
                    }
                }
            }

            diagram.setEntities(entities);
            diagram.setRelationships(relationships);
            diagram.setAttributes(attributes);
            diagram.setInheritances(inheritances);
            diagram.setConnectors(connectors);

        } catch (IOException | JDOMException io) {
            System.out.println(io.getMessage());
        }

        return diagram;
    }

    /**
     * Devuelve un id que represeta un tipo de elemento en el diagrama: Entity =
     * 1 Relationship = 2 Attribute = 3 Inheritance = 4
     *
     * @param customCode
     * @return
     */
    public static int getCustomCodeType(String customCode) {
        int idType = 0;
        switch (customCode.split("!")[0]) {
            case "//Entity":
                idType = 1;
                break;
            case "//Relationship":
                idType = 2;
                break;
            case "//Attribute":
                idType = 3;
                break;
            case "//Inheritance":
                idType = 4;
                break;
        }
        return idType;
    }

    public static Database generateDatabase(Diagram diagram) {
        System.out.print("Enter name of database: ");
        String nombreDB = TecladoIn.readLine();
        LinkedList<Table> tables = new LinkedList<>();
        Database database = new Database(nombreDB, tables);
        for (Entity entity : diagram.getEntities()) {
            doEntity(entity, database, diagram);
        }
        for (Table table : tables) {
            doInheritances(table, database, diagram);
        }
        for (Attribute attribute : diagram.getAttributes()) {
            doMultivalued(attribute, database, diagram);
        }
        for (Relationship relationship : diagram.getRelationships()) {
            doRelationship(relationship, database, diagram);
        }
        return database;
    }

    public static void doEntity(Entity entity, Database database, Diagram diagram) {
        LinkedList<Table> tables = database.getTables();
        LinkedList<Column> columns = new LinkedList<>();
        tables.add(new Table(entity.getName(), columns, entity));
        Attribute attribute;
        for (Connector connectorEntity : diagram.getConnectors()) {
            if ((attribute = connectorEntity.connectAttribute(entity)) != null) {
                if (!attribute.isDerived() && !attribute.isMultivalued()) {
                    doAttribute(attribute, null, diagram, columns);
                }
            }
        }
    }

    public static void doAttribute(Attribute attribute, Attribute previousAttribute, Diagram diagram, LinkedList<Column> columns) {
        Attribute attributeCompound;
        boolean compound = false;
        for (Connector connectorAttribute : diagram.getConnectors()) {
            if ((attributeCompound = connectorAttribute.connectAttribute(attribute)) != null) {
                if (attributeCompound != previousAttribute) {
                    compound = true;
                    doAttribute(attributeCompound, attribute, diagram, columns);
                }
            }
        }
        if (!compound) {
            columns.add(new Column(attribute.getName(), attribute.getDataType(), attribute.isKey() || attribute.isWeakKey(), attribute));
        }
    }

    public static void doInheritances(Table table, Database database, Diagram diagram) {
        LinkedList<Column> keys = table.getKeys();
        if (keys.size() > 0) {
            Entity entity = (Entity) table.getSemanticRef();
            Inheritance inheritance;
            for (Connector connectorEntity : diagram.getConnectors()) {
                if ((inheritance = connectorEntity.connectInheritance(entity)) != null) {
                    if (!connectorEntity.isInheritance()) {
                        Entity subEntity;
                        for (Connector connectorInheritance : diagram.getConnectors()) {
                            if ((subEntity = connectorInheritance.connectEntity(inheritance)) != null) {
                                if (connectorInheritance.isInheritance()) {
                                    Table subTable = findTableWithEntity(subEntity, database);
                                    LinkedList<Column> subTableKeys = subTable.getKeys();
                                    if (inheritance.getType() != 3) {
                                        if (subTableKeys.isEmpty()) {
                                            for (Column columnKey : keys) {
                                                String realName = columnKey.getName();
                                                String[] formedName = realName.split("\\.");
                                                if (formedName.length > 1) {
                                                    realName = formedName[formedName.length - 1];
                                                }
                                                Column newKey = new Column(table.getName() + "." + realName, columnKey.getDataType(), true, true, table, columnKey, columnKey.getSemanticRef());
                                                subTable.getColumns().add(newKey);
                                            }
                                            doInheritances(subTable, database, diagram);
                                        }
                                    } else if (!subTableKeys.isEmpty()) {
                                        for (Column columnKey : subTableKeys) {
                                            String realName = columnKey.getName();
                                            String[] formedName = realName.split("\\.");
                                            if (formedName.length > 1) {
                                                realName = formedName[formedName.length - 1];
                                            }
                                            Column newForeignKey = new Column(subTable.getName() + "." + realName, columnKey.getDataType(), false, true, subTable, columnKey, columnKey.getSemanticRef());
                                            table.getColumns().add(newForeignKey);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void doMultivalued(Attribute attribute, Database database, Diagram diagram) {
        if (attribute.isMultivalued()) {
            boolean exists = false;
            for (Table table : database.getTables()) {
                if (table.getName().equals(attribute.getName())) {
                    exists = true;
                }
            }
            if (!exists) {
                LinkedList<Column> columns = new LinkedList<>();
                Table table = new Table(attribute.getName(), columns, attribute);
                database.getTables().add(table);
                Column multivaluedKey = new Column("id_" + attribute.getName(), "int", true, null);
                columns.add(multivaluedKey);
                columns.add(new Column(attribute.getMultivaluedValue(), attribute.getDataType(), false, null));

                Entity entity;
                for (Connector connector : diagram.getConnectors()) {
                    if ((entity = connector.connectEntity(attribute)) != null) {
                        System.out.println(
                                "For the multivalued " + attribute.getName()
                                + " in the relation with " + entity.getName()
                                + " wants a relation (1)1:M or a relation "
                                + "(2)N:M: ");
                        int decision = 0;
                        do {
                            decision = TecladoIn.readInt();
                            Table entityTable;
                            String multivaluedTableKeyName;
                            switch (decision) {
                                case 1:
                                    entityTable = findTableWithEntity(entity, database);
                                    multivaluedTableKeyName = structureName(multivaluedKey, table, null, connector);
                                    entityTable.getColumns().add(
                                            new Column(multivaluedTableKeyName, multivaluedKey.getDataType(), false, true, table, multivaluedKey, multivaluedKey.getSemanticRef()));
                                    break;
                                case 2:
                                    entityTable = findTableWithEntity(entity, database);
                                    LinkedList<Column> relationshipColumns = new LinkedList<>();
                                    Table relationshipTable = new Table(entity.getName() + "_" + attribute.getName(), relationshipColumns, null);
                                    database.getTables().add(relationshipTable);
                                    multivaluedTableKeyName = structureName(multivaluedKey, table, null, connector);
                                    relationshipTable.getColumns().add(
                                            new Column(multivaluedTableKeyName, multivaluedKey.getDataType(), true, true, table, multivaluedKey, multivaluedKey.getSemanticRef()));
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
                        } while (decision != 1 && decision != 2);
                    }
                }
            }
        }
    }

    public static void doRelationship(Relationship relationship, Database database, Diagram diagram) {
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
            doRelationshipOneMany(relationship, relationshipConnectors, database);
        } else if (amountManyCardinalities == 0) {
            doRelationshipOneOne(relationship, relationshipConnectors, database);
        }
    }

    public static void doRelationshipManyMany(Relationship relationship,
            LinkedList<Column> columns, LinkedList<Table> tables,
            LinkedList<Connector> relationshipConnectors, Database database) {
        Table table = new Table(relationship.getName(), columns, relationship);
        tables.add(table);
        Entity entity;
        for (Connector connector : relationshipConnectors) {
            entity = connector.connectEntity(relationship);
            Table entityTable = findTableWithEntity(entity, database);
            for (Column column : entityTable.getKeys()) {
                String finalName = structureName(column, entityTable, relationship, connector);
                if (connector.isManyCardinality()) {
                    table.getColumns().add(
                            new Column(finalName, column.getDataType(), true, true, entityTable, column, column.getSemanticRef()));
                } else {
                    table.getColumns().add(
                            new Column(finalName, column.getDataType(), false, true, entityTable, column, column.getSemanticRef()));
                }
            }
        }
    }

    public static void doRelationshipOneMany(Relationship relationship,
            LinkedList<Connector> relationshipConnectors, Database database) {
        Entity entityMany;
        Table tableMany;
        for (Connector connectorManyTable : relationshipConnectors) {
            if (connectorManyTable.isManyCardinality()) {
                entityMany = connectorManyTable.connectEntity(relationship);
                tableMany = findTableWithEntity(entityMany, database);
                Entity entityOne;
                Table tableOne;
                for (Connector connectorOneTable : relationshipConnectors) {
                    if (!connectorOneTable.isManyCardinality()) {
                        entityOne = connectorOneTable.connectEntity(relationship);
                        tableOne = findTableWithEntity(entityOne, database);
                        for (Column column : tableOne.getKeys()) {
                            String finalName = structureName(column, tableOne, relationship, connectorManyTable);
                            if (!relationship.isWeak()) {
                                tableMany.getColumns().add(new Column(finalName, column.getDataType(), false, true, tableOne, column, column.getSemanticRef()));
                            } else {
                                tableMany.getColumns().add(new Column(finalName, column.getDataType(), true, true, tableOne, column, column.getSemanticRef()));
                            }
                        }
                    }
                }
            }
        }
    }

    public static void doRelationshipOneOne(Relationship relationship,
            LinkedList<Connector> relationshipConnectors, Database database) {
        LinkedList<Table> relationshipTables = new LinkedList<>();
        Entity entity;
        for (Connector connector : relationshipConnectors) {
            entity = connector.connectEntity(relationship);
            relationshipTables.add(findTableWithEntity(entity, database));
        }
        System.out.println("In wich entity of the relation 1:1 "
                + relationship.getName() + " beetwen the entities "
                + "(1)" + relationshipTables.getFirst().getName()
                + " and "
                + "(2)" + relationshipTables.getLast().getName()
                + " wants to put the foreign key: ");
        int decision = 0;
        do {
            decision = TecladoIn.readInt();
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
        } while (decision != 1 && decision != 2);
    }

    public static Table findTableWithEntity(Entity entity, Database database) {
        Table searchedTable = null;
        for (Table table : database.getTables()) {
            if (table.getSemanticRef() == entity) {
                searchedTable = table;
            }
        }
        return searchedTable;
    }

    public static String structureName(Column column, Table table, Relationship relationship, Connector connector) {
        String realName = column.getName();
        String[] formedName = realName.split("\\.");
        if (formedName.length > 1) {
            realName = formedName[formedName.length - 1];
        }
        String finalName = "";
        if (relationship != null) {
            finalName = relationship.getName() + ".";
        }
        if (!connector.getName().isEmpty()) {
            finalName += connector.getName() + ".";
        }
        finalName += table.getName() + "." + realName;
        return finalName;
    }

    public static String formatName(String text) {
        char[] chars = text.toCharArray();
        int amountChars = 0;
        for (int i = 1; i < chars.length; i++) {
            if (Character.isUpperCase(chars[i])) {
                text = text.substring(0, i + amountChars) + "_" + text.substring(i + amountChars, text.length());
                amountChars++;
            }
        }
        text = text.replace(' ', '_');
        text = text.replace('ñ', 'n');
        text = text.toLowerCase();
        return text;
    }

    public static String generateSQL(Database database) {
        String sql = "";
        sql += "CREATE DATABASE " + database.getName() + ";";
        sql += "\n\n";
        for (Table table : database.getTables()) {
            sql += "CREATE TABLE IF NOT EXISTS " + table.getName() + " (";
            sql += "\n";
            for (Column column : table.getColumns()) {
                sql += "    ";
                sql += column.getName() + " ";
                if (column.getDataType() != null) {
                    sql += column.getDataType().toUpperCase();
                } else {
                    sql += "INT";
                }
                if (column.isForeignKey()) {
                    sql += " FOREIGN KEY REFERENCES " + column.getReferenceTable().getName() + "(" + column.getReferenceColumn().getName() + ")";
                }
                //if (table.getColumns().getLast() != column) {
                sql += ", ";
                sql += "\n";
                //}
            }
            sql += "    ";
            sql += "PRIMARY KEY (";
            for (Column column : table.getKeys()) {
                sql += column.getName();
                if (table.getKeys().getLast() != column) {
                    sql += ", ";
                }
            }
            sql += ")";
            sql += "\n";
            sql += ");";
            sql += "\n\n";
        }
        return sql;
    }

}
