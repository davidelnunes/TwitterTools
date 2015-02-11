package io

/**
 * Created by @davidelnunes on 06-02-2015.
 */
class MemoryTweetWriter extends TweetWriter{
  type Tweet = (Long,String)

  var tweets: Map[String, List[Tweet]] = Map()
  /**
   * What we need to store is the tweet id, the text, and the query
   * used to find that tweet.
   *
   * @param id Tweet ID
   * @param text Tweet text
   * @param keyword Query used to retrieve the tweet
   */
  override def write(id: Long, text: String, keyword: String): Unit = {
    val nt:Tweet = (id,text)

    val tl = tweets.get(keyword) match {
      case None => List(nt)
      case Some(l) => l :+ nt
    }

    tweets += (keyword -> tl)
  }

  override def finish(): Unit = {
    //do nothing
  }
}
