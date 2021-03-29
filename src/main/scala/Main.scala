import javax.swing._
import java.awt.{Color, Component, Container, Dimension, GridLayout, Image, Toolkit}
import javax.imageio.ImageIO
import javax.swing.border.EmptyBorder
import javax.swing.JFileChooser
import javax.swing.JFormattedTextField
import javax.swing.text.NumberFormatter
import java.text.NumberFormat

object Main {
  def createMainLayout: Container = {
    val selectedEmoji = CheckIcon()
    val selectFileButton = Button("Select emoji source") {
      val fileChooser = new JFileChooser
      val ret = fileChooser.showDialog(null, "Открыть файл")
      if (ret == JFileChooser.APPROVE_OPTION)
        Option(fileChooser.getSelectedFile).foreach { file =>
          ImageProcessor.fromImagePath(file.getCanonicalPath).toIcon(selectedEmoji)
        }
    }


    Panel(
      (0, 0, 4) -> selectFileButton,
      (5, 0, 1) -> selectedEmoji,
      (0, 1, 3) -> Label("rotation type"),
      (4, 1, 2) -> ComboBox("1", "2", "3"),

      (0, 2, 1) -> CheckIcon(),
      (1, 2, 1) -> CheckIcon(),
      (2, 2, 1) -> CheckIcon(),
      (3, 2, 1) -> CheckIcon(),
      (4, 2, 1) -> CheckIcon(),
      (5, 2, 1) -> CheckIcon(),

      (0, 3, 1) -> CheckIcon(),
      (1, 3, 1) -> CheckIcon(),
      (2, 3, 1) -> CheckIcon(),
      (3, 3, 1) -> CheckIcon(),
      (4, 3, 1) -> CheckIcon(),
      (5, 3, 1) -> CheckIcon(),

      (0, 4, 1) -> CheckIcon(),
      (1, 4, 1) -> CheckIcon(),
      (2, 4, 1) -> CheckIcon(),
      (3, 4, 1) -> CheckIcon(),
      (4, 4, 1) -> CheckIcon(),
      (5, 4, 1) -> CheckIcon(),
    )
  }

  def main(args: Array[String]): Unit = {
    val frame = new JFrame("My First GUI")
    frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
    frame.setSize(400, 300)
    frame.getContentPane.add(createMainLayout)
    frame.setResizable(false)
    frame.setVisible(true)
  }
}