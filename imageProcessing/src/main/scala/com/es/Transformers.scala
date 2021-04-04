package com.es

object Transformers {
  val horizontalTransformer: EmojiTransformer = emoji => Emoji(emoji.image.flipX)
  val verticalTransformer: EmojiTransformer = emoji => Emoji(emoji.image.flipY)
}
