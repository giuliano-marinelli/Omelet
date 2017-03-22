package omelet.graphic;

import omelet.semantic.Element;
import java.awt.Rectangle;

/**
 *
 * @author Giuliano
 */
public class RectangleElement {
    
    private Rectangle rectangle;
    private Element element;
    
    public RectangleElement(Rectangle rectangle, Element element) {
        this.rectangle = rectangle;
        this.element = element;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    @Override
    public String toString() {
        String result = "";
        result += element.toString();
        result += "("+rectangle.getX()+","+rectangle.getY()+","+rectangle.getWidth()+","+rectangle.getHeight()+")";
        return result;
    }
    
}
