package com.es

import cats.effect.std.Dispatcher
import cats.effect.{IO, Ref}
import com.es.components.EmojiCheckBox
import cats.syntax.traverse._
import cats.instances.option._
import cats.syntax.option.none

import javax.swing.JButton

class TransformedEmojiCheckBox(
    underlying: JButton,
    isCheckedRef: Ref[IO, Boolean],
    emojiRef: Ref[IO, Option[Emoji]],
    transformer: EmojiTransformer
) extends EmojiCheckBox(underlying, isCheckedRef, emojiRef) {
  def transformAndSetEmoji(maybeEmoji: Option[Emoji]): IO[Unit] =
    maybeEmoji.traverse(emoji => setEmoji(Some(transformer(emoji)))).void
}

object TransformedEmojiCheckBox {
  def apply(
      transformer: EmojiTransformer,
      isChecked: Boolean = false
  )(implicit dispatcher: Dispatcher[IO]): IO[TransformedEmojiCheckBox] =
    for {
      isCheckedRef <- IO.ref(isChecked)
      emojiRef <- IO.ref(none[Emoji])
      underlying <- IO(new JButton)
      checkBox = new TransformedEmojiCheckBox(underlying, isCheckedRef, emojiRef, transformer)
      _ <- checkBox.registerEvent
      _ <- checkBox.changeBackGroundColor
    } yield checkBox
}
