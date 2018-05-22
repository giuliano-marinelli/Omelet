package omelet.semantic;

/**
 *
 * @author Giuliano Marinelli
 */
public class Inheritance implements Element {

    private int type;

    public Inheritance(String code) {
        type = 0;
        String[] lines = code.split("\n");
        switch (lines[lines.length - 1]) {
            case "_d":
                type = 1;
                break;
            case "_o":
                type = 2;
                break;
            case "_u":
                type = 3;
                break;
            default:
                type = 0;
                break;
        }
    }

    public Inheritance(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        String result;
        switch (type) {
            case 0:
                result = "i";
                break;
            case 1:
                result = "d";
                break;
            case 2:
                result = "o";
                break;
            case 3:
                result = "U";
                break;
            default:
                result = "i";
                break;
        }
        return result;
    }

}
