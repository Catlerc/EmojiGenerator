package com.es.components

import cats.effect.IO
import cats.syntax.traverse._
import cats.instances.list._
import com.es.componentBases.EmojiViewBase

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
          val padding = getPadding(component)
          component
            .setBounds(
              x * cellSize + padding,
              y * cellSize + padding,
              w * cellSize - padding * 2,
              cellSize - padding * 2
            ) *> IO(underlying.add(component.underlying))
      }
    } yield new Panel(underlying)
  }
  val cellSize = 36

  private def getPadding(component: Component[_]): Int =
    component match {
      case _: EmojiViewBase[_] => 0
      case _                   => 4
    }
}
