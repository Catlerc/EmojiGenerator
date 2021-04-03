package com.es

import cats.effect.IO
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
    emojiAnimationDownToRight: CheckEmojiBox,
    emojiAnimationLeftToRight: CheckEmojiBox,
    emojiAnimationLeftToDown: CheckEmojiBox,
    emojiAnimationUpToDown: CheckEmojiBox,
    emojiAnimationUpToLeft: CheckEmojiBox,
    emojiAnimationRightToLeft: CheckEmojiBox,
    emojiAnimationRightToUp: CheckEmojiBox,
    emojiAnimationDownToUp: CheckEmojiBox,
    emojiAnimationRightDownAlt: CheckEmojiBox,
    emojiAnimationUpRightAlt: CheckEmojiBox,
    emojiAnimationLeftUpAlt: CheckEmojiBox,
    emojiAnimationDownLeftAlt: CheckEmojiBox,
    emojiRotationClockwise: CheckEmojiBox,
    emojiRotationCounterClockwise: CheckEmojiBox,
    exportButton: Button
)

class MainForm(underlying: JFrame, countDown: CountDownLatch[IO])(implicit dispatcher: Dispatcher[IO]) {
  def arrangeLayout(editorComponents: EmojiEditorComponents): IO[Unit] =
    for {
      speedRateLabel <- Label("speed rate")
      rotationTypeLabel <- Label("rotation type")
      panel <- Panel(
        (0, 0, 4) -> editorComponents.emojiSelectButton,
        (4, 0, 1) -> editorComponents.selectedEmoji,
        (5, 0, 3) -> editorComponents.emojiName,
        (0, 1, 4) -> speedRateLabel,
        (4, 1, 4) -> editorComponents.emojiSpeedRate,
        (0, 2, 4) -> rotationTypeLabel,
        (4, 2, 4) -> editorComponents.emojiCornerStyle,
        (0, 3, 1) -> editorComponents.emojiAnimationDownToRight,
        (1, 3, 1) -> editorComponents.emojiAnimationLeftToRight,
        (2, 3, 1) -> editorComponents.emojiAnimationLeftToDown,
        (3, 3, 1) -> editorComponents.emojiAnimationRightDownAlt,
        (4, 3, 1) -> editorComponents.emojiAnimationDownLeftAlt,
        (0, 4, 1) -> editorComponents.emojiAnimationDownToUp,
        (1, 4, 1) -> editorComponents.emojiRotationClockwise,
        (2, 4, 1) -> editorComponents.emojiAnimationUpToDown,
        (3, 4, 1) -> editorComponents.emojiAnimationUpRightAlt,
        (4, 4, 1) -> editorComponents.emojiAnimationLeftUpAlt,
        (0, 5, 1) -> editorComponents.emojiAnimationRightToUp,
        (1, 5, 1) -> editorComponents.emojiAnimationRightToLeft,
        (2, 5, 1) -> editorComponents.emojiAnimationUpToLeft,
        (0, 6, 8) -> editorComponents.exportButton
      )
      _ <- IO.delay {
        underlying.setDefaultCloseOperation(
          javax.swing.WindowConstants.DISPOSE_ON_CLOSE
        )
        underlying.setSize(400, 300)
        underlying.getContentPane.add(
          panel.underlying
        )
        underlying.setResizable(false)
      }
    } yield ()

  val show: IO[Unit] = IO {
    underlying.setFocusableWindowState(true)
    underlying.addWindowListener(new WindowAdapter {
      override def windowClosed(e: WindowEvent): Unit = dispatcher.unsafeRunSync(countDown.release)
    })
    underlying.setVisible(true)
  } *> countDown.await
}

object MainForm {
  def apply()(implicit dispatcher: Dispatcher[IO]): IO[MainForm] =
    for {
      countDown <- CountDownLatch[IO](1)
      underlying <- IO(new JFrame)
      selectedEmoji <- EmojiView()
      selectFileButton <- EmojiOpenDialog("Select emoji source") { emoji =>
        selectedEmoji.setEmoji(emoji)
      }
      emojiAnimationDownToRight <- CheckEmojiBox()
      emojiAnimationLeftToRight <- CheckEmojiBox()
      emojiAnimationLeftToDown <- CheckEmojiBox()
      emojiAnimationUpToDown <- CheckEmojiBox()
      emojiAnimationUpToLeft <- CheckEmojiBox()
      emojiAnimationRightToLeft <- CheckEmojiBox()
      emojiAnimationRightToUp <- CheckEmojiBox()
      emojiAnimationDownToUp <- CheckEmojiBox()
      emojiAnimationRightDownAlt <- CheckEmojiBox()
      emojiAnimationUpRightAlt <- CheckEmojiBox()
      emojiAnimationLeftUpAlt <- CheckEmojiBox()
      emojiAnimationDownLeftAlt <- CheckEmojiBox()
      emojiRotationClockwise <- CheckEmojiBox()
      emojiRotationCounterClockwise <- CheckEmojiBox()
      exportButton <- Button("export") {
        for {
          _ <- IO.delay(println("died?"))
          _ <- IO.raiseError(new Exception("you died"))
        } yield ()

      }
      emojiName <- Label("<com.es.Emoji Not Selected>")
      emojiSpeedRate <- Spinner(1, 1, 10, 1)
      emojiCornerStyle <- ComboBox("1", "2")
      components = EmojiEditorComponents(
        emojiSelectButton = selectFileButton,
        selectedEmoji = selectedEmoji,
        emojiName = emojiName,
        emojiSpeedRate = emojiSpeedRate,
        emojiCornerStyle = emojiCornerStyle,
        emojiAnimationDownToRight = emojiAnimationDownToRight,
        emojiAnimationLeftToRight = emojiAnimationLeftToRight,
        emojiAnimationLeftToDown = emojiAnimationLeftToDown,
        emojiAnimationUpToDown = emojiAnimationUpToDown,
        emojiAnimationUpToLeft = emojiAnimationUpToLeft,
        emojiAnimationRightToLeft = emojiAnimationRightToLeft,
        emojiAnimationRightToUp = emojiAnimationRightToUp,
        emojiAnimationDownToUp = emojiAnimationDownToUp,
        emojiAnimationRightDownAlt = emojiAnimationRightDownAlt,
        emojiAnimationUpRightAlt = emojiAnimationUpRightAlt,
        emojiAnimationLeftUpAlt = emojiAnimationLeftUpAlt,
        emojiAnimationDownLeftAlt = emojiAnimationDownLeftAlt,
        emojiRotationClockwise = emojiRotationClockwise,
        emojiRotationCounterClockwise = emojiRotationCounterClockwise,
        exportButton = exportButton // FIXME: alco
      )
      form = new MainForm(underlying, countDown)
      _ <- form.arrangeLayout(components)
    } yield form
}
