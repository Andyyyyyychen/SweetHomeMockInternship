import com.eteks.sweethome3d.io.*;
import com.eteks.sweethome3d.model.*;
import com.eteks.sweethome3d.swing.*;
import com.eteks.sweethome3d.viewcontroller.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.undo.*;

/** Exercises the application APIs and displays their real Swing views for visual verification. */
public class AcceptanceCheck {
  public static void main(final String[] args) throws Exception {
    System.setProperty("com.eteks.sweethome3d.no3D", "true");
    Locale.setDefault(Locale.ENGLISH);
    if (args.length > 0 && "volume".equals(args[0])) {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    }
    SwingUtilities.invokeAndWait(new Runnable() {
      public void run() {
        try { check(args.length == 0 ? "volume" : args[0]); }
        catch (Exception ex) { throw new RuntimeException(ex); }
      }
    });
  }

  private static void check(String mode) throws Exception {
    DefaultUserPreferences preferences = new DefaultUserPreferences();
    preferences.setLanguage("en");
    SwingViewFactory factory = new SwingViewFactory();
    HomeFileRecorder recorder = new HomeFileRecorder();
    Home home = recorder.readHome("samples/MultiLevelHome.sh3d");
    HomePieceOfFurniture piece = home.getFurniture().get(0);
    home.setSelectedItems(Collections.singletonList(piece));
    UndoableEditSupport undoSupport = new UndoableEditSupport();
    UndoManager undo = new UndoManager();
    undoSupport.addUndoableEditListener(undo);
    HomeFurnitureController editor = new HomeFurnitureController(home, preferences, factory, null, undoSupport);
    String before = piece.getDescription();
    editor.setDescription("Updated description for the mock internship");
    editor.modifyFurniture();
    require("Updated description for the mock internship".equals(piece.getDescription()), "Description save");
    undo.undo();
    require(Objects.equals(before, piece.getDescription()), "Description undo");
    undo.redo();
    require("Updated description for the mock internship".equals(piece.getDescription()), "Description redo");
    File saved = File.createTempFile("sweethome-verification-", ".sh3d");
    try {
      recorder.writeHome(home, saved.getPath());
      Home reopened = recorder.readHome(saved.getPath());
      require(piece.getDescription().equals(reopened.getFurniture().get(0).getDescription()), "Description persistence");
    } finally { saved.delete(); }

    HomeController controller = new HomeController(home, preferences, factory, new FileContentManager(preferences));
    HomePane pane = (HomePane)controller.getView();
    JFrame frame = new JFrame("Sweet Home 3D - acceptance verification");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(pane);
    frame.setSize(1250, 800);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    configureCapture(pane);
    configureCapture(pane.getJMenuBar());
    FurnitureTable table = (FurnitureTable)controller.getFurnitureController().getView();
    HomePieceOfFurniture.SortableProperty volume = HomePieceOfFurniture.SortableProperty.VOLUME;
    if (!home.getFurnitureVisibleProperties().contains(volume)) {
      controller.getFurnitureController().toggleFurnitureVisibleProperty(volume);
    }
    home.setFurnitureSortedProperty(volume);
    home.setFurnitureDescendingSorted(false);
    assertSorted(table, false);
    home.setFurnitureDescendingSorted(true);
    assertSorted(table, true);
    home.setFurnitureDescendingSorted(false);
    float width = piece.getWidth();
    piece.setWidth(width * 2);
    assertSorted(table, false);
    piece.setWidth(width);
    assertSorted(table, false);
    require(new CatalogPieceOfFurniture("Box", null, null, 2, 3, 4, true, false).getVolume() == 24, "Catalog volume");
    System.out.println("PASS: description save/undo/redo/reopen, catalog volume, ascending/descending sorting and resize");

    if ("print".equals(mode)) {
      java.util.List<HomePieceOfFurniture.SortableProperty> columns = new ArrayList<HomePieceOfFurniture.SortableProperty>(home.getFurnitureVisibleProperties());
      Level selected = home.getSelectedLevel();
      float elevation = home.getObserverCamera().getZ();
      javax.swing.table.TableColumnModel screenColumns = table.getColumnModel();
      FurnitureTable.FurnitureFilter filter = new FurnitureTable.FurnitureFilter() {
        public boolean include(Home h, HomePieceOfFurniture p) { return p.isAtLevel(h.getSelectedLevel()); }
      };
      table.setFurnitureFilter(filter);
      try (OutputStream output = new FileOutputStream("submissions/task4-all-levels.pdf")) {
        new HomePDFPrinter(home, preferences, controller, pane.getFont()).write(output);
      }
      require(home.getSelectedLevel() == selected, "Selected level restored");
      require(home.getObserverCamera().getZ() == elevation, "Camera restored");
      require(home.getFurnitureVisibleProperties().equals(columns), "Screen visible columns unchanged");
      require(table.getColumnModel() == screenColumns, "Screen column model restored");
      require(table.getFurnitureFilter() == filter, "Screen furniture filter restored");
      com.lowagie.text.pdf.PdfReader pdf = new com.lowagie.text.pdf.PdfReader("submissions/task4-all-levels.pdf");
      require(pdf.getNumberOfPages() == 4, "Sample output must have four pages, got " + pdf.getNumberOfPages());
      pdf.close();
      System.out.println("PASS: four-page application PDF; selected level, camera, columns and filter restored");
      frame.dispose();
      System.exit(0);
    } else if ("volume".equals(mode)) {
      final JMenu furnitureMenu = pane.getJMenuBar().getMenu(2);
      SwingUtilities.invokeLater(new Runnable() {
        public void run() { furnitureMenu.setPopupMenuVisible(true); }
      });
    } else if ("description".equals(mode)) {
      home.setSelectedItems(Collections.singletonList(piece));
      new HomeFurnitureController(home, preferences, factory, null, undoSupport).displayView(pane);
    }
  }

  // Heavyweight popup windows are omitted by per-window screen capture on macOS.
  // Only this verification preview uses lightweight menus; the application is unchanged.
  private static void configureCapture(Component component) {
    if (component instanceof JMenu) {
      JMenu menu = (JMenu)component;
      menu.getPopupMenu().setLightWeightPopupEnabled(true);
      for (Component child : menu.getMenuComponents()) configureCapture(child);
    }
    if (component instanceof JComponent) {
      JPopupMenu popup = ((JComponent)component).getComponentPopupMenu();
      if (popup != null) {
        popup.setLightWeightPopupEnabled(true);
        for (Component child : popup.getComponents()) configureCapture(child);
      }
    }
    if (component instanceof JSplitPane) {
      JSplitPane split = (JSplitPane)component;
      split.setDividerLocation(split.getOrientation() == JSplitPane.HORIZONTAL_SPLIT ? 600 : 350);
    }
    if (component instanceof Container) {
      for (Component child : ((Container)component).getComponents()) configureCapture(child);
    }
  }

  private static void assertSorted(FurnitureTable table, boolean descending) {
    float previous = descending ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
    for (int row = 0; row < table.getRowCount(); row++) {
      HomePieceOfFurniture p = (HomePieceOfFurniture)table.getValueAt(row, 0);
      float value = p.getVolume();
      require(descending ? value <= previous : value >= previous, "Volume row order");
      previous = value;
    }
  }
  private static void require(boolean condition, String message) {
    if (!condition) throw new AssertionError(message);
  }
}
