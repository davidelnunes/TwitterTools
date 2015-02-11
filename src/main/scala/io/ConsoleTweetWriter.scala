package io

/**
 * Created by @davidelnunes on 06-02-2015.
 */
class ConsoleTweetWriter extends TweetWriter{
  var printed = 0

  /**
   * What we need to store is the tweet id, the text, and the query
   * used to find that tweet.
   *
   * @param id Tweet ID
   * @param text Tweet text
   * @param keyword keyword used to retrieve the tweet
   */
  override def write(id: Long, text: String, keyword: String): Unit = {
    println("ID: "+id.toString+ "| "+text+" | K="+keyword+" |")
    printed += 1
  }

  override def finish(): Unit = {println("-- done --")}
}
