package com.es.components

import cats.effect.std.Dispatcher
import cats.effect.{IO, Ref}
import cats.syntax.option._
import com.es.Emoji
import com.es.componentBases.{ButtonBase, EmojiViewBase}

import java.awt.Color
import javax.swing.{ImageIcon, JButton}

class CheckEmojiBox(
    val underlying: JButton,
    isCheckedRef: Ref[IO, Boolean],
    val emojiRef: Ref[IO, Option[Emoji]]
) extends ButtonBase
    with EmojiViewBase[JButton] {

  import CheckEmojiBox._

  def setBackgroundColor(color: Color): IO[Unit] =
    IO(underlying.setBackground(color))

  val changeBackGroundColor: IO[Unit] =
    isCheckedRef.get.flatMap(isChecked => setBackgroundColor(if (isChecked) checkedColor else uncheckedColor))

  val switch: IO[Unit] =
    isCheckedRef.getAndUpdate(!_) *>
      changeBackGroundColor

  override def setEmoji(newEmoji: Emoji): IO[Unit] =
    emojiRef.set(Some(newEmoji)) *>
      IO {
        val awtEmojiImage = new ImageIcon(newEmoji.toSwingImage)
        underlying.setIcon(awtEmojiImage)
        awtEmojiImage.setImageObserver(underlying)
      }

  val runOnClick: IO[Unit] = switch
}

object CheckEmojiBox {
  val checkedColor = new Color(0, 0, 180)
  val uncheckedColor = new Color(0, 0, 0)

  def apply(
      isChecked: Boolean = false
  )(implicit dispatcher: Dispatcher[IO]): IO[CheckEmojiBox] =
    for {
      isCheckedRef <- IO.ref(isChecked)
      emojiRef <- IO.ref(none[Emoji])
      underlying <- IO(new JButton)
      checkEmojiBox = new CheckEmojiBox(underlying, isCheckedRef, emojiRef)
      _ <- checkEmojiBox.registerEvent
    } yield checkEmojiBox
}
