import com.eteks.sweethome3d.model.*;
import com.eteks.sweethome3d.io.DefaultUserPreferences;
import com.eteks.sweethome3d.swing.PlanComponent;
import com.eteks.sweethome3d.viewcontroller.HomeController3D;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;

/** Regression for camera elevation lost while temporarily switching printed levels. */
public class PrintStateCheck {
  public static void main(String[] args) {
    Home home = new Home();
    Level lower = new Level("Lower", 0, 10, 250);
    Level upper = new Level("Upper", 300, 10, 250);
    home.addLevel(lower);
    home.addLevel(upper);
    home.setSelectedLevel(upper);
    DefaultUserPreferences preferences = new DefaultUserPreferences();
    new HomeController3D(home, preferences, null, null, null);
    home.getObserverCamera().setZ(170);
    Wall wall = new Wall(0, 0, 200, 0, 10, 240);
    wall.setLevel(lower);
    home.addWall(wall);
    PlanComponent plan = new PlanComponent(home, preferences, null);
    Graphics2D graphics = new BufferedImage(800, 1000, BufferedImage.TYPE_INT_RGB).createGraphics();
    try {
      if (plan.print(graphics, new PageFormat(), 0) != PlanComponent.PAGE_EXISTS) {
        throw new AssertionError("Lower level must print");
      }
      if (home.getSelectedLevel() != upper || home.getObserverCamera().getZ() != 170) {
        throw new AssertionError("Printing changed camera elevation: " + home.getObserverCamera().getZ());
      }
      upper.setViewable(false);
      if (plan.print(graphics, new PageFormat(), 1) != PlanComponent.NO_SUCH_PAGE) {
        throw new AssertionError("Hidden level must not print");
      }
      if (home.getSelectedLevel() != upper || home.getObserverCamera().getZ() != 170) {
        throw new AssertionError("Invalid page changed state");
      }
      try {
        plan.print(null, new PageFormat(), 0);
        throw new AssertionError("Expected rendering failure with missing graphics");
      } catch (NullPointerException expected) {
        if (home.getSelectedLevel() != upper || home.getObserverCamera().getZ() != 170) {
          throw new AssertionError("Rendering failure changed state");
        }
      }
      System.out.println("PASS: camera/level restoration, hidden levels and rendering failure");
    } finally {
      graphics.dispose();
    }
  }
}
