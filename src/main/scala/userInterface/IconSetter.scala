package userInterface

import javax.swing._

trait IconSetter[C] {
  def setIcon(component: C, icon: Icon): Unit
}
