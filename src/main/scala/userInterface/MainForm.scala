package userInterface

import imageProcessing.ImageProcessor
import userInterface.components._

import java.awt.Container
import javax.swing.{JFileChooser, JFrame}

case class EmojiEditorComponents(
    emojiSelectButton: Button,
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

class MainForm(editorComponents: EmojiEditorComponents) {
  val frame = {
    val frame = new JFrame("Emoji generator")
    frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
    frame.setSize(400, 300)
    frame.getContentPane.add(
      Panel(
        (0, 0, 4) -> editorComponents.emojiSelectButton,
        (4, 0, 1) -> editorComponents.selectedEmoji,
        (5, 0, 3) -> editorComponents.emojiName,
        (0, 1, 4) -> Label("speed rate"),
        (4, 1, 4) -> editorComponents.emojiSpeedRate,
        (0, 2, 4) -> Label("rotation type"),
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
    )
    frame.setResizable(false)
    frame
  }
  def show(): Unit = {
    frame.setVisible(true)
  }
}

object MainForm {
  def apply(): MainForm = {
    val selectedEmoji = EmojiView()
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
    val components =
      EmojiEditorComponents(
        emojiSelectButton = selectFileButton,
        selectedEmoji = selectedEmoji,
        emojiName = Label("<Emoji Not Selected>"),
        emojiSpeedRate = Spinner(1, 1, 10, 1),
        emojiCornerStyle = ComboBox("1", "2"),
        emojiAnimationDownToRight = CheckEmojiBox(),
        emojiAnimationLeftToRight = CheckEmojiBox(),
        emojiAnimationLeftToDown = CheckEmojiBox(),
        emojiAnimationUpToDown = CheckEmojiBox(),
        emojiAnimationUpToLeft = CheckEmojiBox(),
        emojiAnimationRightToLeft = CheckEmojiBox(),
        emojiAnimationRightToUp = CheckEmojiBox(),
        emojiAnimationDownToUp = CheckEmojiBox(),
        emojiAnimationRightDownAlt = CheckEmojiBox(),
        emojiAnimationUpRightAlt = CheckEmojiBox(),
        emojiAnimationLeftUpAlt = CheckEmojiBox(),
        emojiAnimationDownLeftAlt = CheckEmojiBox(),
        emojiRotationClockwise = CheckEmojiBox(),
        emojiRotationCounterClockwise = CheckEmojiBox(),
        exportButton = Button("export") {}
      )
    new MainForm(components)
  }
}
