import javax.swing._
import java.awt.{Component, Container, Image, Toolkit}
import javax.imageio.ImageIO
import javax.swing.border.EmptyBorder
import javax.swing.JFileChooser

object Main {
  def createBoxLayout(axis: Int, components: Component*): Container = {
    val panel = new JPanel()
    panel.setLayout(new BoxLayout(panel, axis))
    panel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT)
    for (component <- components) {
      panel.setBorder(new EmptyBorder(2, 2, 2, 2))
      panel.add(component)
    }
    panel
  }

  def createRow(components: Component*): Container = createBoxLayout(BoxLayout.X_AXIS, components: _*)

  def createColumn(components: Component*): Container = createBoxLayout(BoxLayout.Y_AXIS, components: _*)

  def createMainLayout: Container = {
    val selectedEmoji = {
      val label = new JLabel()
      label
    }
    val selectFileButton = Button("Select emoji source") {
      val fileChooser = new JFileChooser
      val ret = fileChooser.showDialog(null, "Открыть файл")
      if (ret == JFileChooser.APPROVE_OPTION)
        Option(fileChooser.getSelectedFile).foreach { file =>
          val image = Toolkit.getDefaultToolkit.createImage(file.getCanonicalPath)
          val height = image.getHeight(selectedEmoji)
          val width = image.getWidth(selectedEmoji)
          val maxFactor = 128 / Math.max(height, width)
          val scaledImage = image.getScaledInstance(width * maxFactor, height * maxFactor, Image.SCALE_DEFAULT)
          val icon = new ImageIcon(scaledImage)
          selectedEmoji.setIcon(icon)
          icon.setImageObserver(selectedEmoji)
        }
    }

    createColumn(
      createRow(
        selectFileButton,
        selectedEmoji
      ),
      new JLabel("asdend")
    )
  }

  def main(args: Array[String]): Unit = {
    val frame = new JFrame("My First GUI")
    frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
    frame.setSize(400, 300)
    frame.getContentPane.add(createMainLayout)
    frame.setVisible(true)
  }
}

object Button {
  def apply(label: String)(onClick: => Unit): JButton = {
    val button = new JButton(label)
    button.addActionListener(_ => onClick)
    button
  }
}