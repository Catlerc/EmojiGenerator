package imageProcessing

import userInterface.IconSetter
import userInterface.wrappers.Panel

import java.awt.image.ImageObserver
import java.awt.{Component, Image, Toolkit}
import javax.swing.ImageIcon

case class ImageProcessor(image: Image) {
  def toIcon[C <: Component with ImageObserver](
      component: C
  )(implicit iconSetter: IconSetter[C]): Unit = {
    val height    = image.getHeight(component)
    val width     = image.getWidth(component)
    val maxFactor = (Panel.cellSize - 8) / Math.max(height, width)
    val scaledImage =
      image.getScaledInstance(width * maxFactor, height * maxFactor, Image.SCALE_DEFAULT)
    val icon = new ImageIcon(scaledImage)
    iconSetter.setIcon(component, icon)
    icon.setImageObserver(component)
  }
}

object ImageProcessor {
  def fromImagePath(path: String): ImageProcessor =
    ImageProcessor(Toolkit.getDefaultToolkit.createImage(path))
}
