/*
 * ==========================================================================================
 * AnimationViewer.java : Moves shapes around on the screen according to different paths.
 * It is the main drawing area where shapes are added and manipulated.
 * YOUR UPI: uabd315
 * ==========================================================================================
 */

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.tree.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.event.ListDataListener;
import java.lang.reflect.Field;

class AnimationViewer extends JComponent implements Runnable {
	private Thread animationThread = null; // the thread for animation
	private static int DELAY = 120; // the current animation speed
	private ShapeType currentShapeType = Shape.DEFAULT_SHAPETYPE; // the current shape type,
	private PathType currentPathType = Shape.DEFAULT_PATHTYPE; // the current path type
	private Color currentColor = Shape.DEFAULT_COLOR; // the current fill colour of a shape
	private Color currentBorderColor = Shape.DEFAULT_BORDER_COLOR;
	private int currentPanelWidth = Shape.DEFAULT_PANEL_WIDTH, currentPanelHeight = Shape.DEFAULT_PANEL_HEIGHT,currentWidth = Shape.DEFAULT_WIDTH, currentHeight = Shape.DEFAULT_HEIGHT;
	private String currentLabel = Shape.DEFAULT_LABEL;
	protected NestedShape root;
    protected MyModel model;

	public AnimationViewer() {
		root = new NestedShape(currentPanelWidth, currentPanelHeight);
		model =  new MyModel();
		start();
		addMouseListener(new MyMouseAdapter());
	}
	
	public void setCurrentLabel(String text) {
		currentLabel = text;
		for (Shape currentShape : root.getAllInnerShapes())
			if (currentShape.isSelected())
				currentShape.setLabel(currentLabel);
	}
	public void setCurrentColor(Color bc) {
	    currentColor = bc;
	    for (Shape currentShape: root.getAllInnerShapes())
	      if ( currentShape.isSelected())
	        currentShape.setColor(currentColor);
	  }
	public void setCurrentBorderColor(Color bc) {
	    currentBorderColor = bc;
	    for (Shape currentShape: root.getAllInnerShapes())
	      if ( currentShape.isSelected())
	        currentShape.setBorderColor(currentBorderColor);
	 }
	public void setCurrentHeight(int h) {
	    currentHeight = h;
	    for (Shape currentShape: root.getAllInnerShapes())
	      if ( currentShape.isSelected())
	        currentShape.setHeight(currentHeight);
	 }
	public void setCurrentWidth(int w) {
	    currentWidth = w;
	    for (Shape currentShape: root.getAllInnerShapes())
	      if ( currentShape.isSelected())
	        currentShape.setWidth(currentWidth);
	 }
	class MyMouseAdapter extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			boolean found = false;
			for (Shape currentShape : root.getAllInnerShapes())
				if (currentShape.contains(e.getPoint())) { // if the mousepoint is within a shape, then set the shape to
					 currentShape.setSelected(!currentShape.isSelected());
					found = true;
				}
			if (!found) {
				Shape m = root.createInnerShape(e.getX(), e.getY(), currentWidth, currentHeight, currentColor, currentBorderColor, currentPathType, currentShapeType);
				model.insertNodeInto(m, root);}
		}
	}
	class MyModel extends AbstractListModel<Shape> implements TreeModel{
		private ArrayList<TreeModelListener> treeModelListeners;
		private ArrayList<Shape> selectedShapes;
		public MyModel() {
			treeModelListeners = new ArrayList<TreeModelListener>();
		    selectedShapes = root.getAllInnerShapes();

		}
		public int getSize(){return selectedShapes.size();}
		public Shape getElementAt(int index){return selectedShapes.get(index);}
		public void reload(NestedShape selected) {
            selectedShapes = selected.getAllInnerShapes();
            fireContentsChanged(this, 0, selectedShapes.size());
        }
		public NestedShape getRoot() {return root;}
		public boolean isLeaf(Object node){return !(node instanceof NestedShape);}
		public boolean  isRoot(Shape selectedNode){return selectedNode == root;}
		public Object getChild(Object parent, int index){return (parent instanceof NestedShape && index < ((NestedShape)parent).getSize())?(((NestedShape)parent).getInnerShapeAt(index)):null;}//index >0
		public int getChildCount(Object parent){return (parent instanceof NestedShape)?(((NestedShape)parent).getSize()):0;}
		public int getIndexOfChild(Object parent, Object child){return (parent instanceof NestedShape)?(((NestedShape)parent).indexOf((Shape)child)):-1;}
		public void addTreeModelListener(final TreeModelListener tml){treeModelListeners.add(tml);}
		public void removeTreeModelListener(final TreeModelListener tml){treeModelListeners.remove(tml);}
		public void valueForPathChanged(TreePath path, Object newValue) {}
		public void  fireTreeNodesInserted(Object source, Object[] path,int[] childIndices,Object[] children){
			TreeModelEvent event = new TreeModelEvent(source, path, childIndices, children);
			for(TreeModelListener tree : treeModelListeners){tree.treeNodesInserted(event);}
		    System.out.printf("Called fireTreeNodesInserted: path=%s, childIndices=%s, children=%s\n", Arrays.toString(path), Arrays.toString(childIndices), Arrays.toString(children));
		} 
 		public void  insertNodeInto(Shape newChild, NestedShape parent){
			int[] childIndices = { parent.indexOf(newChild) };
			Object[] children = { newChild };
			fireTreeNodesInserted(this, parent.getPath(), childIndices, children);
			fireIntervalAdded(this, parent.indexOf(newChild), parent.indexOf(newChild));
		}
		public void addShapeNode(NestedShape selectedNode){
			Shape newChild;
			if(root == selectedNode){
				newChild = root.createInnerShape(0, 0, currentWidth, currentHeight, currentColor, currentBorderColor, currentPathType, currentShapeType);	
			}
			else{
				newChild = selectedNode.createInnerShape(0,0,selectedNode.width/2, selectedNode.height/2, selectedNode.color, selectedNode.borderColor, currentPathType, currentShapeType);
			}
			insertNodeInto(newChild, selectedNode);
		}

		public void  fireTreeNodesRemoved(Object source, Object[] path,int[] childIndices,Object[] children){
			TreeModelEvent event = new TreeModelEvent(source, path, childIndices, children);
			for(TreeModelListener tree : treeModelListeners){tree.treeNodesRemoved(event);}
		    System.out.printf("Called fireTreeNodesRemoved: path=%s, childIndices=%s, children=%s\n", Arrays.toString(path), Arrays.toString(childIndices), Arrays.toString(children));
		}

		public void  removeNodeFromParent(Shape selectedNode){
			NestedShape parent = selectedNode.getParent();
			int index = parent.indexOf(selectedNode);
			parent.removeInnerShape(selectedNode);
			int[] childIndices = {index};
			Object[] children = {selectedNode};
			fireTreeNodesRemoved(this, parent.getPath(), childIndices, children);
			fireIntervalRemoved(this, index, index);
		}
	}


	public final void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Shape currentShape : root.getAllInnerShapes()) {
			currentShape.move();
			currentShape.draw(g);
			currentShape.drawHandles(g);
			currentShape.drawString(g);
		}
	}
	public void resetMarginSize() {
		currentPanelWidth = getWidth();
		currentPanelHeight = getHeight();
		for (Shape currentShape : root.getAllInnerShapes())
			currentShape.resetPanelSize(currentPanelWidth, currentPanelHeight);
	}

	// you don't need to make any changes after this line ______________
	public String getCurrentLabel() {return currentLabel;}
	public int getCurrentHeight() { return currentHeight; }
	public int getCurrentWidth() { return currentWidth; }
	public Color getCurrentColor() { return currentColor; }
	public Color getCurrentBorderColor() { return currentBorderColor; }
	public void setCurrentShapeType(ShapeType value) {currentShapeType = value;}
	public void setCurrentPathType(PathType value) {currentPathType = value;}
	public ShapeType getCurrentShapeType() {return currentShapeType;}
	public PathType getCurrentPathType() {return currentPathType;}
	public void update(Graphics g) {
		paint(g);
	}
	public void start() {
		animationThread = new Thread(this);
		animationThread.start();
	}
	public void stop() {
		if (animationThread != null) {
			animationThread = null;
		}
	}
	public void run() {
		Thread myThread = Thread.currentThread();
		while (animationThread == myThread) {
			repaint();
			pause(DELAY);
		}
	}
	private void pause(int milliseconds) {
		try {
			Thread.sleep((long) milliseconds);
		} catch (InterruptedException ie) {}
	}
}
