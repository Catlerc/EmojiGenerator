package userInterface.wrappers

import userInterface.IconSetter

import javax.swing.JButton

case class Button(label: String)(onClick: => Unit) extends JButton(label) with Component {
  val padding = 4

  addActionListener(_ => onClick)
}

object Button {
  implicit val iconSetter: IconSetter[Button] = _.setIcon(_)
}