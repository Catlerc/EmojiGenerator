package com.es.componentBases

import cats.effect.{IO, Ref}
import com.es.Emoji
import com.es.components.Component

import javax.swing.JComponent

trait EmojiViewBase[+C <: JComponent] extends Component[C] {

  val emojiRef: Ref[IO, Option[Emoji]]

  def setEmoji(newEmoji: Emoji): IO[Unit]
}
