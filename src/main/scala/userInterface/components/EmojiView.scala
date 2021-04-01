package userInterface.components

import imageProcessing.Emoji
import userInterface.IconSetter

import java.awt.Color
import javax.swing.{Icon, ImageIcon, JButton, JComponent, JLabel}

case class EmojiView(
    var maybeImage: Option[Emoji] = None
) extends JLabel
    with EmojiViewBase

object EmojiView {
  implicit val iconSetter: IconSetter[EmojiView] = _.setIcon(_)
}

trait EmojiViewBase extends Component {

  def setIcon(icon: Icon): Unit

  var maybeImage: Option[Emoji]

  val padding = 0

  def setEmoji(newEmoji: Emoji): Unit = {
    maybeImage = Some(newEmoji)
    setIcon(new ImageIcon(newEmoji.toSwingImage))

  }

  maybeImage.foreach(image => setIcon(new ImageIcon(image.toSwingImage)))
}

object EmojiViewBase {
  implicit val iconSetter: IconSetter[EmojiViewBase] = _.setIcon(_)
}
