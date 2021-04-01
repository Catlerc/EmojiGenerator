package userInterface.components

import imageProcessing.Emoji
import userInterface.IconSetter

import java.awt.{Color, Image}
import javax.swing.{ImageIcon, JButton}

case class CheckEmojiBox(
    var isChecked: Boolean = false,
    var maybeImage: Option[Emoji] = None
) extends EmojiViewBase
    with Component {

  import CheckEmojiBox._

  def changeBackGroundColor(): Unit =
    setBackground(if (isChecked) checkedColor else uncheckedColor)

  def switch(): Unit = {
    isChecked = !isChecked
    changeBackGroundColor()
  }

  addActionListener(_ => switch())
  setBorderPainted(false)
  changeBackGroundColor()
}

object CheckEmojiBox {
  val checkedColor = new Color(0, 0, 180)
  val uncheckedColor = new Color(0, 0, 0)
  implicit val iconSetter: IconSetter[CheckEmojiBox] = _.setIcon(_)
}