package com.es

import cats.effect.std.Dispatcher
import cats.effect.{IO, Ref}
import cats.syntax.traverse._
import com.es.components._
import com.sksamuel.scrimage.nio.PngWriter

import java.io.File

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
      frame <- Frame[Unit]()
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
        (5, 0, 5) -> emojiName,
        (0, 1, 1) -> emojiVerticalMirror,
        (1, 1, 1) -> emojiHorizontalMirror,
        (2, 1, 1) -> emojiTestClockwiseRotation,
        (3, 1, 1) -> emojiTestCounterClockwiseRotation,
        (0, 2, 10) -> exportButton
      )
      _ <- frame.setContent(content)
    } yield new EmojiTransformForm(frame, restartFlagRef)
}
