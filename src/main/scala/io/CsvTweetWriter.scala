package io

import com.github.tototoshi.csv.CSVWriter
import io.TweetIO._
/**
 * Created by @davidelnunes on 09-02-2015.
 */
class CsvTweetWriter(output: String) extends TweetWriter{


  var writer: Option[CSVWriter] = None
  var writeCount:Map[String,Int] = Map()



  /**
   * What we need to store is the tweet id, the text, and the query
   * used to find that tweet.
   *
   * @param id Tweet ID
   * @param text Tweet text
   * @param keyword Query used to retrieve the tweet
   */
  override def write(id: Long, text: String, keyword: String): Unit = {
    if(writeCount.get(keyword) == None){
      writer = Some(getWriter(Some(keyword),output))
      writeHeader(writer.get)
      writeCount += (keyword -> 0)
    }

    writer.get.writeRow(List(id.toString,keyword,text.toString))
    writeCount += (keyword -> (writeCount(keyword) + 1))
  }

  /**
   * Writers the header of the csv file.
   * @param writer a csv writer
   */
  private def writeHeader(writer: CSVWriter): Unit ={
    writer.writeRow(List(Headers.Id,Headers.Keyword,Headers.Tweet))
  }


  override def finish(): Unit = {
    if(writer.isDefined) writer.get.close()
  }

  private def getWriter(kw:Option[String],basename:String): CSVWriter ={
    if(writer.isDefined) writer.get.close()

    val filename = kw match {
      case None => basename
      case Some(w) => w+"_"+basename
    }
    CSVWriter.open(filename, append = true)
  }
}

