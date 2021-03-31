package userInterface

import imageProcessing.ImageProcessor
import userInterface.wrappers._

import java.awt.Container
import javax.swing.{JFileChooser, JFrame}

case class EmojiEditorComponents(
    emojiSelectButton: Button,
    emojiName: EmojiView,
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
    emojiRotationCounterClockwise: CheckEmojiBox
)

class MainForm(editorComponents: EmojiEditorComponents) {
  val frame = {
    val frame = new JFrame("My First GUI")
    frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
    frame.setSize(400, 300)
    frame.getContentPane.add(
      Panel(
        (5, 4, 1) -> wrappers.CheckEmojiBox()
      )
    )
    frame.setResizable(false)
    frame
  }
  def show(): Unit = {
    frame.setVisible(true)
  }
}

object MainForm {
  def apply: MainForm = {
    val selectedEmoji = wrappers.CheckEmojiBox()
    val selectFileButton = Button("Select emoji source") {
      val fileChooser = new JFileChooser
      val ret = fileChooser.showDialog(null, "Открыть файл")
      if (ret == JFileChooser.APPROVE_OPTION)
        Option(fileChooser.getSelectedFile).foreach { file =>
          ImageProcessor
            .fromImagePath(file.getCanonicalPath)
            .toIcon(selectedEmoji)
        }
    }
    val components = EmojiEditorComponents(
      emojiSelectButton = selectFileButton,
      selectedEmoji = selectedEmoji
    )
    new MainForm(components)
  }
}
