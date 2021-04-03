package com.es.components

import cats.effect.IO

import javax.swing.{JSpinner, SpinnerNumberModel}

case class Spinner(underlying: JSpinner) extends Component[JSpinner]

object Spinner {
  def apply(initialValue: Int, minimum: Int, maximum: Int, stepSize: Double): IO[Spinner] =
    IO(new JSpinner(new SpinnerNumberModel(initialValue, minimum, maximum, stepSize))).map(Spinner(_))
}
