package userInterface.wrappers

import userInterface.IconSetter

import java.awt.{Color, Image}
import javax.swing.{ImageIcon, JButton}

case class CheckIcon(var isChecked: Boolean = false, maybeImage: Option[Image] = None) extends JButton with Component {

  import CheckIcon._

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