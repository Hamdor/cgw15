package computergraphics.framework;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Simple viewer for images.
 */
public class ImageViewer extends JFrame {

  /**
   * Generated ID, required for JFrame classes.
   */
  private static final long serialVersionUID = 8140257581540101857L;

  /**
   * Constructor. Displays the image.
   */
  public ImageViewer(Image image) {
    JLabel label = new JLabel();
    label.setIcon(new ImageIcon(image));
    getContentPane().add(label);

    setLocation(100, 300);
    setSize(image.getWidth(null), image.getHeight(null));
    setVisible(true);
  }

}
