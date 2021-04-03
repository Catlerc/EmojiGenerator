package com.es

import cats.effect.{IO, Ref}
import cats.effect.std.{CountDownLatch, Dispatcher}
import com.es.components._

import java.awt.event.{WindowAdapter, WindowEvent}
import javax.swing.JFrame

case class EmojiEditorComponents(
    emojiSelectButton: EmojiOpenDialog,
    selectedEmoji: EmojiView,
    emojiName: Label,
    emojiSpeedRate: Spinner,
    emojiCornerStyle: ComboBox,
    emojiTest: CheckEmojiBox,
    exportButton: Button
)

class MainForm(underlying: JFrame, windowLock: CountDownLatch[IO])(implicit dispatcher: Dispatcher[IO]) {

  val show: IO[Unit] = IO {
    underlying.setFocusableWindowState(true)
    underlying.addWindowListener(new WindowAdapter {
      override def windowClosed(e: WindowEvent): Unit = dispatcher.unsafeRunSync(windowLock.release)
    })
    underlying.setVisible(true)
  } *> windowLock.await
}

object MainForm {
  def apply(contextRef: Ref[IO, Context])(implicit dispatcher: Dispatcher[IO]): IO[MainForm] =
    for {
      selectedEmoji <- EmojiView()
      emojiTest <- CheckEmojiBox()

      emojiName <- Label("no name")
      speedRateLabel <- Label("speed rate")
      rotationTypeLabel <- Label("rotation type")

      emojiSpeedRate <- Spinner(1, 1, 10, 1)
      emojiCornerStyle <- ComboBox("1", "2")

      exportButton <- Button("export") { IO.raiseError(new Exception("you died")) }
      selectFileButton <- EmojiOpenDialog("Select emoji source")(emoji =>
        for {
          _ <- contextRef.set(Context(Some(emoji)))
        } yield ()
      )

      panel <- Panel(
        (0, 0, 4) -> selectFileButton,
        (4, 0, 1) -> selectedEmoji,
        (5, 0, 3) -> emojiName,
        (0, 1, 4) -> speedRateLabel,
        (4, 1, 4) -> emojiSpeedRate,
        (0, 2, 4) -> rotationTypeLabel,
        (4, 2, 4) -> emojiCornerStyle,
        (0, 3, 1) -> emojiTest,
        (0, 6, 8) -> exportButton
      )
      form <- fromPanel(panel)
    } yield form

  def fromPanel(panel: Panel)(implicit dispatcher: Dispatcher[IO]): IO[MainForm] =
    for {
      windowLock <- CountDownLatch[IO](1)
      frame <- IO.delay {
        val frame = new JFrame
        frame.setDefaultCloseOperation(
          javax.swing.WindowConstants.DISPOSE_ON_CLOSE
        )
        frame.setSize(400, 300)
        frame.getContentPane.add(
          panel.underlying
        )
        frame.setResizable(false)
        frame
      }
    } yield new MainForm(frame, windowLock)
}
