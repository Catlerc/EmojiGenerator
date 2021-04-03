package com.es.componentBases

import cats.effect.IO
import cats.effect.std.Dispatcher
import com.es.components.Component

import javax.swing.JButton

trait ButtonBase extends Component[JButton] {
  val runOnClick: IO[Unit]
  def registerEvent(implicit dispatcher: Dispatcher[IO]): IO[Unit] =
    IO(underlying.addActionListener(_ => dispatcher.unsafeRunAndForget(runOnClick)))
}
