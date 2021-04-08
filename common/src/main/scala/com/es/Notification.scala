package com.es

import cats.effect.IO

import javax.swing.JOptionPane

object Notification {
  import com.es.NotificationType.{NotificationType, _}
  def notify(iconType: NotificationType)(label: String, message: String): IO[Unit] =
    IO(JOptionPane.showMessageDialog(null, message, label, iconType.swingType))
  def error(label: String, message: String): IO[Unit] = notify(Error)(label, message)
  def warning(label: String, message: String): IO[Unit] = notify(Warning)(label, message)
  def question(label: String, message: String): IO[Unit] = notify(Question)(label, message)
  def info(label: String, message: String): IO[Unit] = notify(Info)(label, message)
  def plain(label: String, message: String): IO[Unit] = notify(Plain)(label, message)

}
object NotificationType extends Enumeration {

  protected case class Val(swingType: Int) extends super.Val
  type NotificationType = Val
  val Error: Val = Val(JOptionPane.ERROR_MESSAGE)
  val Info: Val = Val(JOptionPane.INFORMATION_MESSAGE)
  val Warning: Val = Val(JOptionPane.WARNING_MESSAGE)
  val Question: Val = Val(JOptionPane.QUESTION_MESSAGE)
  val Plain: Val = Val(JOptionPane.PLAIN_MESSAGE)
}
