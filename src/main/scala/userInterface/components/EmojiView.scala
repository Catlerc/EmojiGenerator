package userInterface.components

import imageProcessing.Emoji
import userInterface.IconSetter

import java.awt.Color
import javax.swing.{ImageIcon, JButton}

case class EmojiView(
    var maybeImage: Option[Emoji] = None
) extends EmojiViewBase

object EmojiView {
  implicit val iconSetter: IconSetter[EmojiView] = _.setIcon(_)
}

trait EmojiViewBase extends JButton with Component {

  var maybeImage: Option[Emoji]

  val padding = 0

  def updateEmoji(newEmoji: Option[Emoji]): Unit = {
    maybeImage = newEmoji
    newEmoji.foreach(image => setIcon(new ImageIcon(image.toSwingImage)))
  }

  maybeImage.foreach(image => setIcon(new ImageIcon(image.toSwingImage)))
}

object EmojiViewBase {
  implicit val iconSetter: IconSetter[EmojiViewBase] = _.setIcon(_)
}
