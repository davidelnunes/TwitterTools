package tools

import auth.ToolConfig
import cli.CLI
import io.TweetWriter
import twitter4j._

import scala.collection.JavaConverters._
import scala.io.Source


class SearchTools(config: ToolConfig,cli: Option[CLI]){
  private lazy val twitter = {
    val tf = new TwitterFactory(config.twitterCfg)
    tf.getInstance()
  }

  /**
   * Fetches a given number of tweets (at max) for each
   * @param keyword a keyword to be used in the query.
   */
  def tweetsFromKeyword(keyword: String, numTweets: Int,tws: TweetWriter*): Unit ={
    val query: Query = new Query(keyword)

    val lang = config.toolParams.get(ToolConfig.SearchLangK)
    if(lang.isDefined) query.setLang(lang.get)

    var totalQueries = numTweets / 100
    if (numTweets % 100 != 0){
      totalQueries +=1
    }

    //total tweets
    var tt = 0
    var lastID:Long = 0

    var window = TwitterTW(twitter)
    var queriesDone = 0

    for(nq <- (1 to totalQueries)) {
      var done = false
      while (!done) {
        try{//twitter might get overloaded
          //limit reached
          while (queriesDone == window.rQueries && window.rQueries == 0) {
            //wait 5 seconds window is resetting
            if(window.rTime < 0){
              Thread sleep (5000)
            }else{
              window.waitForWindow(cli)
            }
            //reset window
            window = TwitterTW(twitter)
            if (window.rQueries > 0)
              queriesDone = 0
          }

          //perform query nq -> number of tweets to retrieve = nt-tt
          val tweetsPerQuery = (numTweets - tt) min 100
          query.setCount(tweetsPerQuery)
          if (nq > 0) query.setMaxId(lastID)

          val results = twitter.search(query)
          lastID = results.getMaxId
          //convert to scala list
          val tweets = results.getTweets().asScala.toList
          writeTweets(tweets, keyword, tws)
          tt += tweetsPerQuery

          queriesDone += 1

          //update progress
          if (cli.isDefined) {
            if (cli.isDefined) cli.get.tweetNotify(tt, numTweets)
          }
          done = true

          //be nice to twitter done overrun them with requests
          Thread sleep (1000)
        }catch{
          case oc:TwitterException =>
            done = false
            if(oc.getMessage.contains("capacity")) {
              println("Twitter is over capacity, waiting for a bit")
              Thread sleep (10000)
            }
        }
      }
    }
  }

  /**
   * Fetches a certain number of tweets for each keyword in a given file. It assumes that the file
   * does not have a header and contains one keyword per line.
   *
   * @param kwFileName a file name where the keywords are stored
   * @param numTweets number of tweets to be retrieved per keyword
   * @param tws the tweet writers that will output the results
   */
  def tweetsFromKeywords(kwFileName: String, numTweets: Int,tws: TweetWriter*): Unit ={
    try{

      val is = getClass().getClassLoader.getResourceAsStream(kwFileName)

      val lines = Source.fromInputStream(is).getLines()
      lines foreach {line => tweetsFromKeyword(line,numTweets,tws:_*)}

    }catch{
      case ex: Exception => println("problems reading the file: "+kwFileName)
    }
  }


  private def writeTweets(tweets: List[Status],kw:String,tws: Seq[TweetWriter]): Unit = {
    for(tweet <- tweets){
      tws map {tw => tw.write(tweet.getId,tweet.getText,kw)}
    }
  }
}

/**
 * Creates a data set based on keywords or rebuilds
 * a data set based on tweet ids.
 * Created by @davidelnunes on 05-02-2015.
 */
object SearchTools {
  def apply(toolsCfg: ToolConfig,cli: Option[CLI]): SearchTools ={
    new SearchTools(toolsCfg,cli)
  }
  def apply(configFile: String, cli: Option[CLI]): SearchTools ={
    val config = ToolConfig(configFile)
    this(config,cli)
  }
}
