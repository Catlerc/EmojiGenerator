package com.es.components

import cats.effect.IO
import cats.effect.std.Dispatcher
import cats.instances.option._
import cats.syntax.traverse._
import com.es.{Emoji, EmojiInfo}
import com.es.componentBases.ButtonBase

import javax.swing.{JButton, JFileChooser}

class EmojiOpenDialog(
    val underlying: JButton,
    onSelectEmoji: EmojiInfo => IO[Unit]
) extends ButtonBase
    with Component[JButton] {

  val runOnClick: IO[Unit] = for {
    fileChooser <- IO(new JFileChooser)
    ret <- IO(fileChooser.showDialog(null, "Открыть файл"))
    maybeFile <-
      if (ret == JFileChooser.APPROVE_OPTION)
        IO(Option(fileChooser.getSelectedFile))
      else
        IO.none
    _ <- maybeFile.traverse(file => Emoji.fromFile(file).map(EmojiInfo(_, file.getName)).flatMap(onSelectEmoji))
  } yield ()
}

object EmojiOpenDialog {
  def apply(
      label: String
  )(onSelectEmoji: EmojiInfo => IO[Unit])(implicit dispatcher: Dispatcher[IO]): IO[EmojiOpenDialog] =
    for {
      underlying <- IO(new JButton(label))
      button = new EmojiOpenDialog(underlying, onSelectEmoji)
      _ <- button.registerEvent
    } yield button
}
