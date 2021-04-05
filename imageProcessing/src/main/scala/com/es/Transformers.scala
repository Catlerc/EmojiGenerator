package com.es

import com.sksamuel.scrimage.angles.Degrees

object Transformers {
  val horizontalMirrorTransformer: EmojiTransformer = emoji => Emoji(emoji.image.flipX)
  val verticalMirrorTransformer: EmojiTransformer = emoji => Emoji(emoji.image.flipY)
  val rotationClockwiseTransformer: EmojiTransformer = emoji => Emoji(emoji.image.rotate(new Degrees(90)))
  val rotationCounterclockwiseTransformer: EmojiTransformer = emoji => Emoji(emoji.image.rotate(new Degrees(-90)))
  val rotationHalfTurnTransformer: EmojiTransformer = emoji => Emoji(emoji.image.rotate(new Degrees(180)))
}
