/*
 * ==========================================================================================
 * NestedShape.java : Creates a Nested Shape that contains Shapes and extens Rectangle Shape.
 * YOUR UPI: uabd315
 * ==========================================================================================
 */
import java.util.*;
import java.awt.*;

public class NestedShape extends RectangleShape {
    private ArrayList<Shape> innerShapes = new ArrayList<Shape>();
    private int count = Integer.parseInt(getLabel());

    public NestedShape() {
        super();
        createInnerShape(0, 0, (width / 2), (height / 2), color, borderColor, PathType.BOUNCING, ShapeType.RECTANGLE);
        label = Integer.toString(count++);
    }
    public NestedShape(int x, int y, int width, int height, int panelWidth, int panelHeight, Color c, Color bc, PathType pt){
        super(x, y, width, height, panelWidth, panelHeight, c, bc, pt);
        createInnerShape(0, 0, (width/2), (height/2), color, borderColor, PathType.BOUNCING, ShapeType.RECTANGLE);
        label = Integer.toString(count++);
    }
    public NestedShape(int width, int height){
        super(0, 0, width, height, DEFAULT_PANEL_WIDTH, DEFAULT_PANEL_HEIGHT, Color.BLACK, Color.BLACK, PathType.BOUNCING);
    }


    public Shape createInnerShape(int x, int y, int w, int h, Color c, Color bc, PathType pt, ShapeType st){
        Shape s;
        switch(st){
            case RECTANGLE:
                s = new RectangleShape(x,y,w,h,width, height, c,bc,pt);
                break;
            case OVAL:
                s = new OvalShape(x,y,w,h,width, height, c,bc,pt);
                break;
            case NESTED:
                s = new NestedShape(x,y,w,h,width, height, c,bc,pt);
                break;
            default:
                s = null;
                break;
        }
        s.setParent(this);
        innerShapes.add(s);
        return s;
    }
    public Shape createInnerShape(PathType pt, ShapeType st){
        Shape s;
        switch(st){
            case RECTANGLE:
                s = new RectangleShape(0,0,(width/2),(height/2), width, height, color,borderColor,pt);
                break;
            case OVAL:
                s = new OvalShape(0,0,(width/2),(height/2), width, height, color,borderColor,pt);
                break;
            case NESTED:
                s = new NestedShape(0,0,(width/2),(height/2), width, height, color,borderColor,pt);
                break;
            default:
                s = null;
                break;
        }
        s.setParent(this);
        innerShapes.add(s);
        return s;
    }

    public Shape getInnerShapeAt(int index){return innerShapes.get(index);}
    public int getSize(){return innerShapes.size();}


    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.drawRect(x,y,width,height);
        g.translate(x,y);
        for(Shape s: innerShapes){
                s.draw(g);
                if(s.isSelected()){
                    s.drawHandles(g);
                    s.drawString(g);
                }
            }
        
        g.translate(-x,-y);
        
        }

    public void move(){
        super.move();
        for(Shape s: innerShapes){
            s.move();
        }
    }

    public int indexOf(Shape s){return innerShapes.indexOf(s);}
    public void addInnerShape(Shape s){innerShapes.add(s);s.setParent(this);}
    public void removeInnerShape(Shape s){innerShapes.remove(s);s.setParent(null);}
    public void removeInnerShapeAt(int index){
        if(index>=0 && index < innerShapes.size()){
            Shape s = innerShapes.remove(index);
            s.setParent(null);
        }
       }
    public ArrayList<Shape> getAllInnerShapes(){return innerShapes;}
}