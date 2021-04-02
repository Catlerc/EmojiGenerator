package com.es.components

import com.es.Emoji

import javax.swing.{JButton, JFileChooser}

case class EmojiOpenDialog(label: String)(onEmoji: Emoji => Unit)
    extends JButton(label)
    with Component {
  val padding = 4

  addActionListener(_ => {
    val fileChooser = new JFileChooser
    val ret = fileChooser.showDialog(null, "Открыть файл")
    if (ret == JFileChooser.APPROVE_OPTION)
      Option(fileChooser.getSelectedFile).foreach(file =>
        onEmoji(Emoji.fromFile(file))
      )
  })
}
