package com.es.components

import cats.effect.IO

import javax.swing.JComboBox

case class ComboBox(underlying: JComboBox[String]) extends Component[JComboBox[String]]

object ComboBox {
  def apply(items: String*): IO[ComboBox] = IO(new JComboBox(items.toArray)).map(new ComboBox(_))
}
