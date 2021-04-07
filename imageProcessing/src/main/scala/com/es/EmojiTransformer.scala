package com.es

class EmojiTransformer(postfix: String, func: Emoji => Emoji) {
  def transform(info: EmojiInfo): EmojiInfo = {
    EmojiInfo(func(info.emoji), s"${info.name}_$postfix")
  }
}

object EmojiTransformer {
  def apply(postfix: String, func: Emoji => Emoji): EmojiTransformer = new EmojiTransformer(postfix, func)
}
