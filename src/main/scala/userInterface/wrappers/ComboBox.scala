package userInterface.wrappers

import javax.swing.JComboBox

case class ComboBox(items: String*) extends JComboBox(items.toArray) with Component {
  val padding = 4
}
