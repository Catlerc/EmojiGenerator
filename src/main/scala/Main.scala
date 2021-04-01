import imageProcessing.ImageProcessor
import userInterface.components.{Button, ComboBox, Label, Panel}
import userInterface.{MainForm, components}

import javax.swing._
import java.awt.{
  Color,
  Component,
  Container,
  Dimension,
  GridLayout,
  Image,
  Toolkit
}
import javax.imageio.ImageIO
import javax.swing.border.EmptyBorder
import javax.swing.JFileChooser
import javax.swing.JFormattedTextField
import javax.swing.text.NumberFormatter
import java.text.NumberFormat

object Main {
  def main(args: Array[String]): Unit = {
    val form = MainForm()
    form.show()
  }
}
