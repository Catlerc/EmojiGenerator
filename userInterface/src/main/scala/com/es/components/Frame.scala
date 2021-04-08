package com.es.components

import cats.syntax.option._
import cats.effect.{IO, Ref}
import cats.effect.std.{CountDownLatch, Dispatcher}

import java.awt.event.{WindowAdapter, WindowEvent}
import javax.swing.{JComponent, JFrame}

class Frame[T](underlying: JFrame, windowLock: CountDownLatch[IO], resultRef: Ref[IO, Option[T]])(implicit
    dispatcher: Dispatcher[IO]
) {
  val show: IO[Option[T]] = IO {
    underlying.addWindowListener(new WindowAdapter {
      override def windowClosed(e: WindowEvent): Unit = dispatcher.unsafeRunSync(windowLock.release)
    })
    underlying.setVisible(true)
  } *> windowLock.await *> resultRef.get
  def setResult(result: T): IO[Unit] = resultRef.set(Some(result))
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
  def apply[T](resizable: Boolean = false)(implicit dispatcher: Dispatcher[IO]): IO[Frame[T]] =
    for {
      windowLock <- CountDownLatch[IO](1)
      resultRef <- IO.ref(none[T])
      frame <- IO.delay {
        val frame = new JFrame
        frame.setDefaultCloseOperation(
          javax.swing.WindowConstants.DISPOSE_ON_CLOSE
        )
        frame.setResizable(resizable)
        frame.setLocationRelativeTo(null)
        frame
      }
    } yield new Frame[T](frame, windowLock, resultRef)
}
