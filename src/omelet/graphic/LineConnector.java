package omelet.graphic;

import omelet.semantic.Connector;
import java.awt.geom.Line2D;

/**
 *
 * @author Giuliano
 */
public class LineConnector {

    private Line2D.Double line;
    private Connector connector;

    public LineConnector(Line2D.Double line) {
        this.line = line;
    }

    public LineConnector(Line2D.Double line, Connector connector) {
        this.line = line;
        this.connector = connector;
    }

    public Line2D.Double getLine() {
        return line;
    }

    public void setLine(Line2D.Double line) {
        this.line = line;
    }

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    @Override
    public String toString() {
        String result = "";
        if (connector == null) {
            result += "Connector";
        } else {
            result += connector.toString();
        }
        result += "(" + line.getX1() + "," + line.getY1() + "," + line.getX2() + "," + line.getY2() + ")";
        return result;
    }

}
