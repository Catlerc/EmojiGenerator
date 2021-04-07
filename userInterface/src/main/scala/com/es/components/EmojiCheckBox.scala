package com.es.components

import cats.effect.std.Dispatcher
import cats.effect.{IO, Ref}
import cats.syntax.option._
import com.es.Emoji
import com.es.componentBases.{ButtonBase, EmojiViewBase}

import java.awt.Color
import javax.swing.{ImageIcon, JButton}

class EmojiCheckBox(
    val underlying: JButton,
    isCheckedRef: Ref[IO, Boolean],
    val emojiRef: Ref[IO, Option[Emoji]]
) extends ButtonBase
    with EmojiViewBase[JButton] {

  import EmojiCheckBox._

  def setBackgroundColor(color: Color): IO[Unit] =
    IO(underlying.setBackground(color))

  val state: IO[Boolean] = isCheckedRef.get

  val changeBackGroundColor: IO[Unit] =
    isCheckedRef.get.flatMap(isChecked => setBackgroundColor(if (isChecked) checkedColor else uncheckedColor))

  val switch: IO[Unit] =
    isCheckedRef.getAndUpdate(!_) *>
      changeBackGroundColor

  def setEmoji(maybeEmoji: Option[Emoji]): IO[Unit] =
    emojiRef.set(maybeEmoji) *>
      IO {
        maybeEmoji match {
          case Some(newEmoji) =>
            val awtEmojiImage = new ImageIcon(newEmoji.toSwingImage)
            underlying.setIcon(awtEmojiImage)
            awtEmojiImage.setImageObserver(underlying)
          case None =>
            underlying.setIcon(null)
        }
      }

  val runOnClick: IO[Unit] = switch
}

object EmojiCheckBox {
  val checkedColor = new Color(0, 0, 180)
  val uncheckedColor = new Color(0, 0, 0)

  def apply(
      isChecked: Boolean = false
  )(implicit dispatcher: Dispatcher[IO]): IO[EmojiCheckBox] =
    for {
      isCheckedRef <- IO.ref(isChecked)
      emojiRef <- IO.ref(none[Emoji])
      underlying <- IO(new JButton)
      checkBox = new EmojiCheckBox(underlying, isCheckedRef, emojiRef)
      _ <- checkBox.registerEvent
      _ <- checkBox.changeBackGroundColor
    } yield checkBox
}
