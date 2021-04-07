package com.es

import com.sksamuel.scrimage.angles.Degrees

object Transformers {
  val horizontalMirrorTransformer: EmojiTransformer =
    EmojiTransformer("mh", emoji => Emoji(emoji.image.flipX))
  val verticalMirrorTransformer: EmojiTransformer =
    EmojiTransformer("mv", emoji => Emoji(emoji.image.flipY))
  val rotationClockwiseTransformer: EmojiTransformer =
    EmojiTransformer("rc", emoji => Emoji(emoji.image.rotate(new Degrees(90))))
  val rotationCounterclockwiseTransformer: EmojiTransformer =
    EmojiTransformer("rcc", emoji => Emoji(emoji.image.rotate(new Degrees(-90))))
  val rotationHalfTurnTransformer: EmojiTransformer =
    EmojiTransformer("rht", emoji => Emoji(emoji.image.rotate(new Degrees(180))))
}
