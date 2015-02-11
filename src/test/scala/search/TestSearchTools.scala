package search

import auth.ToolConfig
import io.{ConsoleTweetWriter, MemoryTweetWriter}
import org.scalatest.{FlatSpec, Matchers}
import tools.SearchTools

/**
 * Testing Search Tools
 * Created by @davidelnunes on 06-02-2015.
 */
class TestSearchTools extends FlatSpec with Matchers{

  val toolConfig: ToolConfig = ToolConfig("twitter-tools")
  val search: SearchTools = SearchTools(toolConfig,None)


  "Search Tools" should "be able to retrieve tweets by keyword" in{
    val memoryWriter = new MemoryTweetWriter
    val n:Int = 100
    val keyword = "hello"

    search.tweetsFromKeyword(keyword,n,memoryWriter)

    //we should have some list of tweets
    memoryWriter.tweets.get(keyword) should not be None

    val tweets = memoryWriter.tweets.get(keyword)
    tweets should be ('defined)
    tweets.get.size should be (n)

    //the search returns the tweets with the keyword ignoring the case
    //of the keyword characters
    for(tweet <- tweets.get){
      val (id,text) = tweet
      text should include regex ignoreCaseRegex(keyword)
    }
  }

  private def ignoreCaseRegex(keyWord: String) = {
    val regexStr: String = ""
    for{c <- keyWord.iterator}{
      regexStr + "["+c.toUpper+c.toLower+"]"
    }
    regexStr.r
  }

  "Search Tools" should "work with multiple tweet writers" in {
    val cw1 = new ConsoleTweetWriter
    val cw2 = new ConsoleTweetWriter

    val n:Int = 10
    val keyword = "hello"

    search.tweetsFromKeyword(keyword,n,cw1,cw2)
    //Each prints the same 10 tweets
    cw1.printed should be (n)
    cw2.printed should be (n)
  }


}
