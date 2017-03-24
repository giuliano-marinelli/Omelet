package omelet.semantic;

import static omelet.Main.formatName;

/**
 *
 * @author Giuliano
 */
public class Relationship implements Element {

    private String name;
    private boolean weak;

    public Relationship(String code) {
        name = "";
        weak = false;
        String[] lines = code.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (!weak && lines[i].equals("_weak")) {
                weak = true;
            } else {
                if (!name.equals("")) {
                    name += " ";
                }
                name += lines[i];
            }
        }
        name = formatName(name);
    }

    public Relationship(String name, boolean isWeak) {
        this.name = name;
        this.weak = isWeak;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isWeak() {
        return weak;
    }

    public void setWeak(boolean weak) {
        this.weak = weak;
    }

    @Override
    public String toString() {
        return name;
    }

}
