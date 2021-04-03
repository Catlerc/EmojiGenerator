package com.es.components

import cats.effect.IO
import cats.syntax.traverse._
import cats.instances.list._

import javax.swing.JPanel
import javax.swing.JComponent

class Panel(val underlying: JPanel) extends Component[JPanel]

object Panel {
  def apply(
      components: ((Int, Int, Int), Component[JComponent])*
  ): IO[Panel] = {
    for {
      underlying <- IO.pure(new JPanel())
      _ <- IO(underlying.setLayout(null))
      _ <- components.toList.traverse {
        case ((x, y, w), component) =>
          component
            .setBounds(
              x * cellSize,
              y * cellSize,
              w * cellSize,
              cellSize
            ) *> IO(underlying.add(component.underlying))
      }
    } yield new Panel(underlying)
  }
  val cellSize = 36
}
