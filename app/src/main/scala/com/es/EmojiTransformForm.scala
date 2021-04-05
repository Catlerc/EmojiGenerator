package com.es

import cats.effect.{IO, Ref}
import cats.effect.std.Dispatcher
import com.es.components.{Button, ComboBox, EmojiOpenDialog, EmojiView, Frame, Label, Panel, Spinner}
import cats.syntax.traverse._
import javax.swing.JOptionPane

class EmojiTransformForm(frame: Frame, restartFlagRef: Ref[IO, Boolean]) {
  val show: IO[Boolean] = frame.show *> restartFlagRef.get
  val close: IO[Unit] = frame.close
}

object EmojiTransformForm {
  def apply(emojiInfo: EmojiInfo)(implicit dispatcher: Dispatcher[IO]): IO[EmojiTransformForm] =
    for {
      restartFlagRef <- IO.ref[Boolean](false)
      frame <- Frame(resizable = true)
      selectedEmoji <- EmojiView()
      _ <- selectedEmoji.setEmoji(Some(emojiInfo.emoji))

      emojiVerticalMirror <- TransformedEmojiCheckBox(Transformers.verticalMirrorTransformer)
      emojiHorizontalMirror <- TransformedEmojiCheckBox(Transformers.horizontalMirrorTransformer)
      emojiTestClockwiseRotation <- TransformedEmojiCheckBox(Transformers.rotationClockwiseTransformer)
      emojiTestCounterClockwiseRotation <- TransformedEmojiCheckBox(Transformers.rotationCounterclockwiseTransformer)

      toUpdate = List(
        emojiVerticalMirror,
        emojiHorizontalMirror,
        emojiTestClockwiseRotation,
        emojiTestCounterClockwiseRotation
      )
      _ <- toUpdate.traverse(_.transformAndSetEmoji(Some(emojiInfo.emoji)))

      emojiNameLabel <- Label("Исходное эмодзи")
      emojiName <- Label(emojiInfo.name)
      speedRateLabel <- Label("скорость")
      rotationTypeLabel <- Label("тип поворотов")

      emojiSpeedRate <- Spinner(1, 1, 10, 1)
      emojiCornerStyle <- ComboBox("1", "2")

      exportButton <- Button("export") {
        IO.unit
      }

      content <- Panel(
        (0, 0, 4) -> emojiNameLabel,
        (4, 0, 1) -> selectedEmoji,
        (5, 0, 3) -> emojiName,
        (0, 1, 4) -> speedRateLabel,
        (4, 1, 4) -> emojiSpeedRate,
        (0, 2, 4) -> rotationTypeLabel,
        (4, 2, 4) -> emojiCornerStyle,
        (0, 3, 1) -> emojiVerticalMirror,
        (1, 3, 1) -> emojiHorizontalMirror,
        (2, 3, 1) -> emojiTestClockwiseRotation,
        (3, 3, 1) -> emojiTestCounterClockwiseRotation,
        (0, 6, 8) -> exportButton
      )
      _ <- frame.setContent(content)
    } yield new EmojiTransformForm(frame, restartFlagRef)
}
