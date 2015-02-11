package io

/**
 * Created by @davidelnunes on 06-02-2015.
 */
trait TweetWriter {
  /**
   * What we need to store is the tweet id, the text, and the query
   * used to find that tweet.
   *
   * @param id Tweet ID
   * @param text Tweet text
   * @param keyword Query used to retrieve the tweet
   */
  def write(id: Long, text:String, keyword: String)

  def finish(): Unit
}
