package com.es.components

import cats.effect.{IO, Ref}
import cats.syntax.option._
import com.es.Emoji
import com.es.componentBases.EmojiViewBase

import javax.swing.{ImageIcon, JLabel}

case class EmojiView(underlying: JLabel, emojiRef: Ref[IO, Option[Emoji]]) extends EmojiViewBase[JLabel] {

  override def setEmoji(maybeEmoji: Option[Emoji]): IO[Unit] =
    emojiRef.set(maybeEmoji) *>
      IO {
        maybeEmoji match {
          case Some(newEmoji) =>
            val awtEmojiImage = new ImageIcon(newEmoji.toSwingImage)
            underlying.setIcon(awtEmojiImage)
            awtEmojiImage.setImageObserver(underlying)
          case None =>
            underlying.setIcon(null)
        }
      }
}

object EmojiView {
  def apply(): IO[EmojiView] =
    IO.ref(none[Emoji]).map(new EmojiView(new JLabel(), _))
}
