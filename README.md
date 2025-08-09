# 2D Shape Animation and Collision Simulation

A Java Swing application that provides an interactive environment for creating, animating, and managing 2D shapes with different movement patterns and collision detection.

## Features

### Shape Types
- **Rectangle**: Basic rectangular shapes with customizable dimensions
- **Oval**: Elliptical shapes with collision detection
- **Nested Shapes**: Container shapes that can hold other shapes in a hierarchical structure

### Animation Paths
- **Bouncing Path**: Shapes bounce off the boundaries of the animation area
- **Floating Path**: Shapes move in smooth, continuous patterns

### Interactive Controls
- **Shape Creation**: Add new shapes with customizable properties
- **Tree View**: Hierarchical view of nested shapes for easy management
- **List View**: Linear view of all shapes in the current context
- **Real-time Property Editing**: Modify shape properties while animation is running

### Customization Options
- **Dimensions**: Adjustable width and height for each shape
- **Colors**: Customizable fill and border colors using color picker
- **Labels**: Text labels for shape identification
- **Movement Patterns**: Different path types for varied animation effects

## Project Structure

### Core Classes

#### `A3.java`
- Main application class extending JFrame
- Sets up the GUI with split panes for model view and animation area
- Handles user interactions through various action listeners
- Manages toolbar controls and shape property inputs

#### `AnimationViewer.java`
- Main animation panel where shapes are rendered and animated
- Implements Runnable for continuous animation thread
- Handles mouse interactions for shape selection
- Manages the animation loop and shape updates

#### `Shape.java`
- Abstract base class for all shapes
- Defines common properties: position, size, colors, selection state
- Implements movement paths and collision detection
- Manages shape lifecycle and parent-child relationships

#### `NestedShape.java`
- Extends RectangleShape to create container shapes
- Manages collections of inner shapes
- Implements hierarchical shape management
- Supports tree-like structure for complex compositions

### Shape Implementations

#### `RectangleShape.java`
- Concrete implementation for rectangular shapes
- Handles rectangular drawing and point containment testing

#### `OvalShape.java`
- Concrete implementation for elliptical shapes
- Uses mathematical collision detection for curved boundaries

### Enumerations

#### `ShapeType.java`
- Defines available shape types: RECTANGLE, OVAL, NESTED

#### `PathType.java`
- Defines movement patterns: BOUNCING, FLOATING

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Java Runtime Environment (JRE)

### Compilation
```bash
javac *.java
```

### Running the Application
```bash
java A3
```

## Usage Guide

### Creating Shapes
1. Select shape type from the dropdown menu
2. Choose movement path type
3. Adjust width and height values
4. Set fill and border colors using color buttons
5. Click in the animation area to create the shape

### Managing Nested Shapes
1. Select a nested shape in the tree view
2. Use "Add Node" button to add child shapes
3. Use "Remove Node" button to delete selected shapes
4. Navigate the hierarchy using the tree structure

### Shape Interaction
- **Selection**: Click on shapes to select/deselect them
- **Property Editing**: Selected shapes can be modified using the toolbar controls
- **Animation**: All shapes animate automatically based on their assigned path type

### Interface Layout
- **Left Panel**: Split between tree view (top) and list view (bottom)
- **Right Panel**: Animation area where shapes move and can be interacted with
- **Top Toolbar**: Controls for shape creation and property modification

## Technical Details

### Animation System
- Uses Java Swing Timer for smooth animation
- Default animation delay: 120ms
- Shapes update position based on their movement path
- Collision detection prevents shapes from moving outside boundaries

### Model-View Architecture
- `MyModel` class implements both TreeModel and ListModel
- Supports multiple view representations of the same data
- Real-time updates across all views when data changes

### Memory Management
- Efficient shape storage using ArrayList collections
- Proper cleanup of animation threads
- Event-driven updates minimize unnecessary redraws


