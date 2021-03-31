package userInterface.wrappers

import javax.swing.JPanel

case class Panel(components: ((Int, Int, Int), Component)*) extends JPanel {

  val padding = 2

  import Panel._

  setLayout(null)
  components.foreach {
    case (x, y, w) -> component =>
      component.setBounds(
        x * cellSize + component.padding,
        y * cellSize + component.padding,
        w * cellSize - component.padding * 2,
        cellSize - component.padding * 2
      )
      add(component)
  }
}

object Panel {
  val cellSize = 36
}