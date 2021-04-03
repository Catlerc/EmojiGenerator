package com.es.components

import cats.effect.IO
import com.es.componentBases.ButtonBase

import javax.swing.JButton

class Button(val underlying: JButton, val runOnClick: IO[Unit]) extends ButtonBase

object Button {
  def apply(label: String)(runOnClick: IO[Unit]): IO[Button] =
    IO(new JButton(label)).map(new Button(_, runOnClick))
}
