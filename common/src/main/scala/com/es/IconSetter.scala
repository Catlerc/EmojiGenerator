package com.es

import javax.swing.Icon

trait IconSetter[C] {
  def setIcon(component: C, icon: Icon): Unit
}
