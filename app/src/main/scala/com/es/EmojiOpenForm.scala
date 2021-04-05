package com.es

import cats.effect.{IO, Ref}
import cats.effect.std.Dispatcher
import com.es.components.{Button, EmojiOpenDialog, EmojiView, Frame, Panel}

class EmojiOpenForm(frame: Frame, emojiInfoRef: Ref[IO, Option[EmojiInfo]]) {
  val show: IO[Option[EmojiInfo]] = frame.show *> emojiInfoRef.get
  val close: IO[Unit] = frame.close

}

object EmojiOpenForm {
  def apply()(implicit dispatcher: Dispatcher[IO]): IO[EmojiOpenForm] =
    for {
      frame <- Frame(resizable = true)
      emojiInfoRef <- IO.ref[Option[EmojiInfo]](None)
      emojiView <- EmojiView()
      emojiOpenInfo <- EmojiOpenDialog("Открыть эмодзи") { emoji =>
        emojiInfoRef.set(Some(emoji)) *>
          emojiView.setEmoji(Some(emoji.emoji))
      }
      goButton <- Button("Идём дальше") {
        frame.close
      }
      content <- Panel(
        (0, 0, 5) -> emojiOpenInfo,
        (5, 0, 1) -> emojiView,
        (0, 1, 6) -> goButton
      )
      _ <- frame.setContent(content)
    } yield new EmojiOpenForm(frame, emojiInfoRef)
}
