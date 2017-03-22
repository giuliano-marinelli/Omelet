package omelet.semantic;

import java.util.LinkedList;

/**
 *
 * @author Giuliano
 */
public class Diagram {

    private LinkedList<Entity> entities;
    private LinkedList<Relationship> relationships;
    private LinkedList<Attribute> attributes;
    private LinkedList<Inheritance> inheritances;
    private LinkedList<Connector> connectors;

    public Diagram() {
    }

    public LinkedList<Entity> getEntities() {
        return entities;
    }

    public void setEntities(LinkedList<Entity> entities) {
        this.entities = entities;
    }

    public LinkedList<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(LinkedList<Relationship> relationships) {
        this.relationships = relationships;
    }

    public LinkedList<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(LinkedList<Attribute> attributes) {
        this.attributes = attributes;
    }

    public LinkedList<Inheritance> getInheritances() {
        return inheritances;
    }

    public void setInheritances(LinkedList<Inheritance> inheritances) {
        this.inheritances = inheritances;
    }

    public LinkedList<Connector> getConnectors() {
        return connectors;
    }

    public void setConnectors(LinkedList<Connector> connectors) {
        this.connectors = connectors;
    }

    @Override
    public String toString() {
        String result = "";
        result += "Entities: \n";
        for (Entity entity : entities) {
            result += "    " + entity.toString() + "\n";
        }
        result += "\nRelationships: \n";
        for (Relationship relationship : relationships) {
            result += "    " + relationship.toString() + "\n";
        }
        result += "\nAttributes: \n";
        for (Attribute attribute : attributes) {
            result += "    " + attribute.toString() + "\n";
        }
        result += "\nInheritances: \n";
        for (Inheritance inheritance : inheritances) {
            result += "    " + inheritance.toString() + "\n";
        }
        result += "\nConnectors: \n";
        for (Connector connector : connectors) {
            result += "    " + connector.toString() + "\n";
        }
        return result;
    }

}
