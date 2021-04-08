package com.es

import cats.effect.std.Dispatcher
import cats.effect.{ExitCode, IO, IOApp}

import java.io.{PrintWriter, StringWriter}
import javax.swing.JOptionPane

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    Dispatcher[IO]
      .use { implicit dispatcher =>
        {
          for {
            emojiOpenForm <- EmojiOpenForm()
            maybeEmojiInfo <- emojiOpenForm.show
            restartFlag <- maybeEmojiInfo match {
              case Some(emojiInfo) => EmojiTransformForm(emojiInfo).flatMap(_.show)
              case None            => IO.pure(false)
            }
          } yield restartFlag
        }.iterateWhile(identity(_: Boolean)).as(ExitCode.Success)
      }
      .onError(throwable =>
        IO.delay {
            val stringWriter = new StringWriter
            val printWriter = new PrintWriter(stringWriter)
            throwable.printStackTrace(printWriter)
            stringWriter.toString
          }
          .flatMap(Notification.error(s"Error! ${throwable.getMessage}", _))
      )
}
