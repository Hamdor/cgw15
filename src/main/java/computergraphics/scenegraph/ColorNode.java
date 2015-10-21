package computergraphics.scenegraph;

import com.jogamp.opengl.GL2;

public class ColorNode extends Node {

  private double red;
  private double green;
  private double blue;
  
  public ColorNode(double red, double green, double blue) {
    this.red = red;
    this.blue = blue;
    this.green = green;
  }
  
  @Override
  public void drawGl(GL2 gl) {
    // Remember current state of the render system
    gl.glPushMatrix();

    // Apply color
    gl.glColor3d(red, green, blue);
    
    // Draw all children
    for (int childIndex = 0; childIndex < getNumberOfChildren(); childIndex++) {
      getChildNode(childIndex).drawGl(gl);
    }

    // Restore original state
    gl.glPopMatrix();

  }

}
