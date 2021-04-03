package com.es

trait EmojiTransformer {
  def apply(emoji: Emoji): Emoji
  def andNext(next: EmojiTransformer): EmojiTransformer =
    emoji => next.apply(apply(emoji))
}
