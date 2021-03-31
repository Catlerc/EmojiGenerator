package imageProcessing

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.angles.Degrees

import java.awt.Image
import java.io.File

case class Emoji(image: ImmutableImage) {
  def toSwingImage: Image = image.copy().awt
  def rotateRight: Emoji = Emoji(image.rotate(new Degrees(90)))
  def rotateLeft: Emoji = Emoji(image.rotate(new Degrees(90)))
  def rotationHalfTurn: Emoji = Emoji(image.rotate(new Degrees(180)))
  def mirrorHorizontal: Emoji = Emoji(image.flipX())
  def mirrorVertical: Emoji = Emoji(image.flipY())
}

object Emoji {
  val size = 64
  def fromFile(file: File): Emoji = {
    val image = ImmutableImage.loader().fromFile(file)
    val sizedImage = image.scale(size / math.max(image.width, image.height))
    Emoji(sizedImage)
  }
}
