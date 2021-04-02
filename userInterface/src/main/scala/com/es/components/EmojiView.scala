package com.es.components

import com.es.{Emoji, IconSetter}

import javax.swing.{Icon, ImageIcon, JLabel}

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
