package omelet.semantic;

import static omelet.Main.formatName;

/**
 *
 * @author Giuliano
 */
public class Attribute implements Element {

    private String name;
    private String dataType;
    private String multivaluedValue;
    private boolean key;
    private boolean derived;
    private boolean weakKey;
    private boolean multivalued;

    public Attribute(String code) {
        name = "";
        key = false;
        derived = false;
        weakKey = false;
        multivalued = false;
        String[] lines = code.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (!key && lines[i].equals("_key")) {
                key = true;
            } else if (!derived && lines[i].equals("_derived")) {
                derived = true;
            } else if (!weakKey && lines[i].equals("_weakkey")) {
                weakKey = true;
            } else if (!multivalued && lines[i].equals("_multivalued")) {
                multivalued = true;
            } else if (lines[i].charAt(0) == '(') {
                dataType = lines[i].substring(1, lines[i].length());
            } else if (lines[i].charAt(0) == '*') {
                multivaluedValue = lines[i].substring(1, lines[i].length());
            } else {
                if (!name.equals("")) {
                    name += " ";
                }
                name += lines[i];
            }
        }
        name = formatName(name);
    }

    public Attribute(String name, String dataType, String multivaluedValue, boolean key, boolean derived, boolean weakKey, boolean multivalued) {
        this.name = name;
        this.dataType = dataType;
        this.multivaluedValue = multivaluedValue;
        this.key = key;
        this.derived = derived;
        this.weakKey = weakKey;
        this.multivalued = multivalued;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    public boolean isDerived() {
        return derived;
    }

    public void setDerived(boolean derived) {
        this.derived = derived;
    }

    public boolean isWeakKey() {
        return weakKey;
    }

    public void setWeakKey(boolean weakKey) {
        this.weakKey = weakKey;
    }

    public boolean isMultivalued() {
        return multivalued;
    }

    public void setMultivalued(boolean multivalued) {
        this.multivalued = multivalued;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getMultivaluedValue() {
        return multivaluedValue;
    }

    public void setMultivaluedValue(String multivaluedValue) {
        this.multivaluedValue = multivaluedValue;
    }

    @Override
    public String toString() {
        String result = "";
        result += name;
        if (dataType != null) {
            result += ": " + dataType;
        }
        return result;
    }

}
