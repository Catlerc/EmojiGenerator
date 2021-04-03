package com.es

import cats.effect.IO
import com.sksamuel.scrimage.ImmutableImage

import java.awt.Image
import java.io.File

case class Emoji(image: ImmutableImage) {
  def toSwingImage: Image = image.copy().awt
}

object Emoji {
  val size = 64
  def fromFile(file: File): IO[Emoji] =
    for {
      image <- IO(ImmutableImage.loader().fromFile(file))
      sizedImage = image.scale(size / math.max(image.width, image.height))
    } yield Emoji(sizedImage)
}
