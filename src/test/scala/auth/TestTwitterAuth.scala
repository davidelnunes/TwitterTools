package auth

import org.scalatest.{FlatSpec, Matchers}
import twitter4j.{Query, QueryResult, RateLimitStatus, TwitterFactory}

/**
 * The last test is volatile, depends on the API but the
 * authentication should be able to create application-like
 * twitter instances and not user-like authentications (the limits are
 * higher)
 *
 * Created by @davidelnunes on 05-02-2015.
 */
class TestTwitterAuth extends FlatSpec with Matchers{

  val twitterAuth: ToolConfig = ToolConfig("twitter-tools")

  "TwitterAuth" should "read the API keys from a configuration file" in {
    twitterAuth.apiConfig.get(ToolConfig.ConsumerKeyK) should be ('defined)
    twitterAuth.apiConfig.get(ToolConfig.ConsumerSecretK) should be ('defined)
    twitterAuth.apiConfig.get(ToolConfig.AccessKeyK) should be ('defined)
    twitterAuth.apiConfig.get(ToolConfig.AccessSecretK) should be ('defined)
  }

  "The authentication object" should "be able to retrieve a token" in{
    val token = twitterAuth.oauth2Token
    token should be ('defined)
  }

  "The authentication object" should "provide a configuration for the application" in {
    val cfg = twitterAuth.twitterCfg
    val tf = new TwitterFactory(cfg)
    val twitter = tf.getInstance()
  }


  "Twitter Client" should "should be authenticated as an application and give you appropriate limits" in {
    val cfg = twitterAuth.twitterCfg
    val tf = new TwitterFactory(cfg)
    val twitter = tf.getInstance()

    val status: RateLimitStatus = twitter.getRateLimitStatus().get("/search/tweets")

    //current API says that
    status.getLimit should be (450)
  }

  "One Query" should "decrese the remaining queries by 1" in {

    val cfg = twitterAuth.twitterCfg
    val tf = new TwitterFactory(cfg)
    val twitter = tf.getInstance()

    val query: Query = new Query("lang:en AND "+"\"hello world\"")
    query.setCount(100)

    val status1: RateLimitStatus = twitter.getRateLimitStatus().get("/search/tweets")

    val result:QueryResult = twitter.search(query)

    result.getCount should be (100)

    val status2: RateLimitStatus = twitter.getRateLimitStatus().get("/search/tweets")

    //current API says that
    status2.getRemaining should be < (status1.getRemaining)
  }


}
