package userInterface.wrappers

import javax.swing.JLabel

case class Label(label: String) extends JLabel(label) with Component {
  val padding = 4
}
