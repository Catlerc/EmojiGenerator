package com.es.componentBases

import cats.effect.IO
import cats.effect.std.Dispatcher
import com.es.components.Component

import java.io.{PrintWriter, StringWriter}
import javax.swing.{JButton, JOptionPane}

trait ButtonBase extends Component[JButton] {
  val runOnClick: IO[Unit]
  def registerEvent(implicit dispatcher: Dispatcher[IO]): IO[Unit] =
    IO(underlying.addActionListener(_ => dispatcher.unsafeRunAndForget(errorHandler(runOnClick))))
  private def errorHandler(io: IO[Unit]): IO[Unit] =
    io.handleErrorWith(throwable =>
      IO.delay {
        val stringWriter = new StringWriter
        val printWriter = new PrintWriter(stringWriter)
        throwable.printStackTrace(printWriter)
        val errorString = stringWriter.toString
        JOptionPane.showMessageDialog(null, errorString, "Error!", JOptionPane.ERROR_MESSAGE) // null :c
      }
    )
}
