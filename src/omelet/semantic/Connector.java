package omelet.semantic;

import static omelet.Main.formatName;

/**
 *
 * @author Giuliano
 */
public class Connector {

    private Element element1;
    private Element element2;
    private String cardinality1;
    private String cardinality2;
    private String name;
    private boolean total;
    private boolean inheritance;

    public Connector(String code) {
        cardinality1 = "";
        cardinality2 = "";
        name = "";
        total = false;
        String[] lines = code.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (!total && lines[i].equals("lw=4")) {
                total = true;
            } else if (!inheritance
                    && (lines[i].equals("lt=<<-") || lines[i].equals("lt=->>"))) {
                inheritance = true;
            } else if (lines[i].contains("m1=")) {
                if (lines[i].split("=").length > 1) {
                    cardinality1 = lines[i].split("=")[1];
                }
            } else if (lines[i].contains("m2=")) {
                if (lines[i].split("=").length > 1) {
                    cardinality2 = lines[i].split("=")[1];
                }
            } else {
                /*if (!name.equals("")) {
                    name += " ";
                }*/
                name += lines[i];
            }
            name = formatName(name);
        }
    }

    public Connector(String cardinality1, String cardinality2, boolean isTotal, boolean isInheritance) {
        this.cardinality1 = cardinality1;
        this.cardinality2 = cardinality2;
        this.total = isTotal;
        this.inheritance = isInheritance;
    }

    public Connector(String cardinality1, String cardinality2, String name, boolean total, boolean inheritance) {
        this.cardinality1 = cardinality1;
        this.cardinality2 = cardinality2;
        this.name = name;
        this.total = total;
        this.inheritance = inheritance;
    }

    public Connector(Element element1, Element element2, String cardinality1, String cardinality2, boolean isTotal, boolean isInheritance) {
        this.element1 = element1;
        this.element2 = element2;
        this.cardinality1 = cardinality1;
        this.cardinality2 = cardinality2;
        this.total = isTotal;
        this.inheritance = isInheritance;
    }

    public Element getElement1() {
        return element1;
    }

    public void setElement1(Element element1) {
        this.element1 = element1;
    }

    public Element getElement2() {
        return element2;
    }

    public void setElement2(Element element2) {
        this.element2 = element2;
    }

    public String getCardinality1() {
        return cardinality1;
    }

    public void setCardinality1(String cardinality1) {
        this.cardinality1 = cardinality1;
    }

    public String getCardinality2() {
        return cardinality2;
    }

    public void setCardinality2(String cardinality2) {
        this.cardinality2 = cardinality2;
    }

    public boolean isTotal() {
        return total;
    }

    public void setTotal(boolean total) {
        this.total = total;
    }

    public boolean isInheritance() {
        return inheritance;
    }

    public void setInheritance(boolean inheritance) {
        this.inheritance = inheritance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        String result = "";
        if (element1 == null || element2 == null) {
            result = "Connector";
        } else {
            result += element1.toString() + "<" + cardinality1;
            if (total) {
                result += "==";
            } else if (inheritance) {
                result += "<->";
            } else {
                result += "--";
            }
            result += cardinality2 + ">" + element2.toString();
        }
        return result;
    }

    /**
     * Verifica si el connector conecta un elemento recibido por parametro. Si
     * lo conecta, devuelve la referencia al elemento con el que conecta. Caso
     * contrario la referencia sera nula.
     *
     * @param element
     * @return
     */
    public Element connect(Element element) {
        Element otherElement = null;
        if (element1 == element) {
            otherElement = element2;
        } else if (element2 == element) {
            otherElement = element1;
        }
        return otherElement;
    }

    public Entity connectEntity(Element element) {
        Entity otherElement = null;
        if (element1 == element && element2.getClass() == Entity.class) {
            otherElement = (Entity) element2;
        } else if (element2 == element && element1.getClass() == Entity.class) {
            otherElement = (Entity) element1;
        }
        return otherElement;
    }

    public Relationship connectRelationship(Element element) {
        Relationship otherElement = null;
        if (element1 == element && element2.getClass() == Relationship.class) {
            otherElement = (Relationship) element2;
        } else if (element2 == element && element1.getClass() == Relationship.class) {
            otherElement = (Relationship) element1;
        }
        return otherElement;
    }

    public Attribute connectAttribute(Element element) {
        Attribute otherElement = null;
        if (element1 == element && element2.getClass() == Attribute.class) {
            otherElement = (Attribute) element2;
        } else if (element2 == element && element1.getClass() == Attribute.class) {
            otherElement = (Attribute) element1;
        }
        return otherElement;
    }

    public Inheritance connectInheritance(Element element) {
        Inheritance otherElement = null;
        if (element1 == element && element2.getClass() == Inheritance.class) {
            otherElement = (Inheritance) element2;
        } else if (element2 == element && element1.getClass() == Inheritance.class) {
            otherElement = (Inheritance) element1;
        }
        return otherElement;
    }

    public String getCardinality() {
        String cardinality = "";
        if (cardinality1.isEmpty() && !cardinality2.isEmpty()) {
            cardinality = cardinality2;
        } else if (cardinality2.isEmpty() && !cardinality1.isEmpty()) {
            cardinality = cardinality1;
        }
        return cardinality;
    }

    /**
     * Devuelve true si la cardinalidad es muchos.
     *
     * @return
     */
    public boolean isManyCardinality() {
        boolean many = false;
        String cardinality = getCardinality();
        if (cardinality.matches("[a-zA-Z]+") || Integer.parseInt(cardinality) > 1) {
            many = true;
        }
        return many;
    }
}
