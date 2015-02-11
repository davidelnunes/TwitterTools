package cli

/**
 * Created by @davidelnunes on 06-02-2015.
 */
trait CLI {
  def waitNotify(seconds: Long): Unit

  /**
   * If you supply 1.0 the bar turns to 100% and
   * resets.
   * @param progress
   */
  def progressBar(progress:Double)

  def tweetNotify(numRetrieved: Long, total: Long)

  def state: State
}

sealed abstract class State

case object Waiting extends State{
  override def toString = "waiting"
}
case object Downloading extends State{
  override def toString = "downloading"
}
case object Done extends State{
  override def toString = "done"
}
