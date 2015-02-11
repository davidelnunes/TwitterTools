package tools

import cli.CLI
import twitter4j.{RateLimitStatus, Twitter}

import scala.compat.Platform
import scala.concurrent.duration.{FiniteDuration, MILLISECONDS, SECONDS}


/**
 * Created by @davidelnunes on 06-02-2015.
 */
class TwitterTW(val iTime:Long, val rTime:Long, val rQueries:Int) {
  /**
   * Determines if we are still within the time window
   * We make a call to the api the first time we create the
   * TwitterWindow through the companion object. We use this value
   * to determine if we are still within the time window
   *
   * @return true if we need to wait
   */
  def withinWindow:Boolean = {(Platform.currentTime - iTime) / 1000 < rTime}

  def waitForWindow(listener: Option[CLI]): Unit = {
    val passed = FiniteDuration(Platform.currentTime - iTime,MILLISECONDS)
    if(withinWindow){
      val timeToWait = FiniteDuration(rTime-(passed.toSeconds) + 1,SECONDS)

      if(listener.isDefined) listener.get.waitNotify(timeToWait.toSeconds)

      var s = 0
      while(s < timeToWait.toSeconds){
        listener.get.progressBar(s/timeToWait.toSeconds.toDouble)
        s += 1
        Thread sleep (1000)
      }
      listener.get.progressBar(1.0)
    }
  }


}

/**
 * Companion object to create Twitter Time Window Instances
 */
object TwitterTW{
  def apply(twitter: Twitter): TwitterTW ={
    val status:RateLimitStatus = twitter.getRateLimitStatus().get("/search/tweets")
    val initialTime = Platform.currentTime
    val tRemaining: Long = status.getSecondsUntilReset
    val remainingQueries = status.getRemaining

    new TwitterTW(initialTime,tRemaining,remainingQueries)
  }
}
