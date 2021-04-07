package com.es.components

import cats.effect.IO
import cats.effect.std.{CountDownLatch, Dispatcher}

import java.awt.event.{WindowAdapter, WindowEvent}
import javax.swing.{JComponent, JFrame}

class Frame(underlying: JFrame, windowLock: CountDownLatch[IO])(implicit dispatcher: Dispatcher[IO]) {
  val show: IO[Unit] = IO {
    underlying.addWindowListener(new WindowAdapter {
      override def windowClosed(e: WindowEvent): Unit = dispatcher.unsafeRunSync(windowLock.release)
    })
    underlying.setVisible(true)
  } *> windowLock.await
  val close: IO[Unit] = IO {
    underlying.dispatchEvent(new WindowEvent(underlying, WindowEvent.WINDOW_CLOSING))
  }
  def setContent(content: Component[JComponent]): IO[Unit] = {
    for {
      rectangle <- content.getSize
      _ <- IO {
        underlying.getContentPane.add(content.underlying)
        underlying.getContentPane.setPreferredSize(rectangle)
        underlying.pack()
      }
    } yield ()

  }
}

object Frame {
  def apply(resizable: Boolean)(implicit dispatcher: Dispatcher[IO]): IO[Frame] =
    for {
      windowLock <- CountDownLatch[IO](1)
      frame <- IO.delay {
        val frame = new JFrame
        frame.setDefaultCloseOperation(
          javax.swing.WindowConstants.DISPOSE_ON_CLOSE
        )
        frame.setResizable(resizable)
        frame
      }
    } yield new Frame(frame, windowLock)
}
