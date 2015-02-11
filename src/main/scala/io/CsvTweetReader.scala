package io

import java.io.File

import com.github.tototoshi.csv.CSVReader
import io.TweetIO._

/**
 * Created by @davidelnunes on 09-02-2015.
 */
class CsvTweetReader(filePath: String, withHeaders: Boolean) extends Iterator[(Long,String)]{
  val reader = CSVReader.open(new File(filePath))
  val tweets = reader.allWithHeaders().iterator

  override def hasNext: Boolean = tweets.hasNext

  override def next(): (Long, String) = {
    val nextTweet = tweets.next()
    val id = nextTweet(Headers.Id).toLong
    val text = nextTweet(Headers.Tweet)

    (id,text)
  }

  def close(): Unit ={
    reader.close()
  }
}
