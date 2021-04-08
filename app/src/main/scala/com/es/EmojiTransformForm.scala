package com.es

import cats.effect.{IO, Ref}
import cats.effect.std.Dispatcher
import com.es.components.{Button, ComboBox, EmojiCheckBox, EmojiOpenDialog, EmojiView, Frame, Label, Panel, Spinner}
import cats.syntax.traverse._
import com.es.componentBases.EmojiViewBase
import com.sksamuel.scrimage.nio.PngWriter

import java.io.File
import javax.swing.{JComponent, JOptionPane}

class EmojiTransformForm(frame: Frame[Unit], restartFlagRef: Ref[IO, Boolean]) {
  val show: IO[Boolean] = frame.show *> restartFlagRef.get
  val close: IO[Unit] = frame.close
}

object EmojiTransformForm {
  private def saveEmoji(sourceEmojiName: String)(info: EmojiInfo): IO[Unit] =
    IO {
      val applicationDir = System.getProperty("user.dir")
      val file = new File(s"$applicationDir/$sourceEmojiName/${info.name}.png")
      file.getParentFile.mkdirs
      info.emoji.image.output(PngWriter.NoCompression, file)
    }
  private def updateView(
      info: EmojiInfo
  )(transformer: EmojiTransformer, checkBox: EmojiCheckBox): IO[(EmojiCheckBox, EmojiInfo)] = {
    val newEmoji = transformer.transform(info)
    checkBox.setEmoji(Some(newEmoji.emoji)).as(checkBox -> newEmoji)
  }
  def apply(emojiInfo: EmojiInfo)(implicit dispatcher: Dispatcher[IO]): IO[EmojiTransformForm] =
    for {
      restartFlagRef <- IO.ref[Boolean](false)
      frame <- Frame[Unit](resizable = true)
      selectedEmoji <- EmojiView()
      _ <- selectedEmoji.setEmoji(Some(emojiInfo.emoji))

      emojiVerticalMirror <- EmojiCheckBox()
      emojiHorizontalMirror <- EmojiCheckBox()
      emojiTestClockwiseRotation <- EmojiCheckBox()
      emojiTestCounterClockwiseRotation <- EmojiCheckBox()

      update = updateView(emojiInfo)(_, _)
      transformedEmojies <- List(
        update(Transformers.verticalMirrorTransformer, emojiVerticalMirror),
        update(Transformers.verticalMirrorTransformer, emojiVerticalMirror),
        update(Transformers.horizontalMirrorTransformer, emojiHorizontalMirror),
        update(Transformers.rotationClockwiseTransformer, emojiTestClockwiseRotation),
        update(Transformers.rotationCounterclockwiseTransformer, emojiTestCounterClockwiseRotation)
      ).sequence

      emojiNameLabel <- Label("Исходное эмодзи")
      emojiName <- Label(emojiInfo.name)
      speedRateLabel <- Label("скорость")
      rotationTypeLabel <- Label("тип поворотов")

      emojiSpeedRate <- Spinner(1, 1, 10, 1)
      emojiCornerStyle <- ComboBox("1", "2")

      exportButton <- Button("export") {
        for {
          emojies <- transformedEmojies.traverse { case (view, info) => view.state.map(_ -> info) }
          filtered = emojies.collect { case (true, info) => info }
          _ <- filtered.traverse(saveEmoji(emojiInfo.name))
        } yield ()
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
