import CheckIcon.{checkedColor, uncheckedColor}

import java.awt.{Color, Component, Image, Insets}
import javax.swing.{ImageIcon, JButton, JComboBox, JLabel, JPanel, JSpinner, SpinnerNumberModel}

trait Padding {
  val padding: Int
}

case class Panel(components: ((Int, Int, Int), Component with Padding)*) extends JPanel {

  val padding = 2

  import Panel._

  setLayout(null)
  components.foreach {
    case (x, y, w) -> component =>
      component.setBounds(
        x * cellSize + component.padding,
        y * cellSize + component.padding,
        w * cellSize - component.padding * 2,
        cellSize - component.padding * 2
      )
      add(component)
  }
}

object Panel {
  val cellSize = 36
}

case class Button(label: String)(onClick: => Unit) extends JButton(label) with Padding {
  val padding = 4

  addActionListener(_ => onClick)
}

object Button {
  implicit val iconSetter: IconSetter[Button] = _.setIcon(_)
}

case class CheckIcon(var isChecked: Boolean = false, maybeImage: Option[Image] = None) extends JButton with Padding {
  val padding = 0

  def changeBackGroundColor(): Unit = setBackground(if (isChecked) checkedColor else uncheckedColor)

  def switch(): Unit = {
    isChecked = !isChecked
    changeBackGroundColor()
  }

  addActionListener(_ => switch())
  maybeImage.foreach(image => setIcon(new ImageIcon(image)))
  setBorderPainted(false)
  changeBackGroundColor()
}

object CheckIcon {
  val checkedColor = new Color(0, 0, 180)
  val uncheckedColor = new Color(0, 0, 0)
  implicit val iconSetter: IconSetter[CheckIcon] = _.setIcon(_)
}

case class ComboBox(items: String*) extends JComboBox(items.toArray) with Padding {
  val padding = 4
}

case class Spinner(initialValue: Int, minimum: Int, maximum: Int, stepSize: Double) extends
  JSpinner(new SpinnerNumberModel(initialValue, minimum, maximum, stepSize))

case class Label(label: String) extends JLabel(label) with Padding {
  val padding = 4
}