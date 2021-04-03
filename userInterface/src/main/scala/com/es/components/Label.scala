package com.es.components

import cats.effect.IO

import javax.swing.JLabel

case class Label(underlying: JLabel) extends Component[JLabel]

object Label {
  def apply(label: String): IO[Label] = IO(new JLabel(label)).map(new Label(_))
}
