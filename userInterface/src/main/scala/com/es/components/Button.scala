package com.es.components

import cats.effect.IO
import cats.effect.std.Dispatcher
import com.es.componentBases.ButtonBase

import javax.swing.JButton

class Button(val underlying: JButton, val runOnClick: IO[Unit]) extends ButtonBase

object Button {
  def apply(label: String)(runOnClick: IO[Unit])(implicit dispatcher: Dispatcher[IO]): IO[Button] =
    for {
      underlying <- IO(new JButton(label))
      button = new Button(underlying, runOnClick)
      _ <- button.registerEvent
    } yield button
}
