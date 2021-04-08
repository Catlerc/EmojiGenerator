package com.es

import cats.effect.{IO, Ref}
import cats.effect.std.Dispatcher
import com.es.components.{Button, EmojiOpenDialog, EmojiView, Frame, Panel}

class EmojiOpenForm(frame: Frame[EmojiInfo], emojiInfoRef: Ref[IO, Option[EmojiInfo]]) {
  val show: IO[Option[EmojiInfo]] = frame.show *> emojiInfoRef.get
  val close: IO[Unit] = emojiInfoRef.set(None) *> frame.close

}

object EmojiOpenForm {
  def apply()(implicit dispatcher: Dispatcher[IO]): IO[EmojiOpenForm] =
    for {
      emojiInfoRef <- IO.ref[Option[EmojiInfo]](None)
      frame <- Frame[EmojiInfo](resizable = true)
      emojiView <- EmojiView()
      emojiOpenInfo <- EmojiOpenDialog("Открыть эмодзи") { emoji =>
        emojiInfoRef.set(Some(emoji)) *>
          emojiView.setEmoji(Some(emoji.emoji))
      }
      goButton <- Button("Идём дальше") {
        for {
          maybeEmojiInfo <- emojiInfoRef.get
          _ <- maybeEmojiInfo match {
            case Some(emojiInfo) => frame.setResult(emojiInfo) *> frame.close
            case None            => Notification.info("?", "Дальше вы не пройдёте, пока не выберите эмодзи")
          }
        } yield ()

      }
      content <- Panel(
        (0, 0, 5) -> emojiOpenInfo,
        (5, 0, 1) -> emojiView,
        (0, 1, 6) -> goButton
      )
      _ <- frame.setContent(content)
    } yield new EmojiOpenForm(frame, emojiInfoRef)
}
