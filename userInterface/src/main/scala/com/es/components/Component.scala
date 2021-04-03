package com.es.components

import cats.effect.IO

import javax.swing.JComponent

trait Component[+C <: JComponent] {
  val underlying: C
  def setBounds(x: Int, y: Int, w: Int, h: Int): IO[Unit] =
    IO(underlying.setBounds(x, y, w, h))
}
