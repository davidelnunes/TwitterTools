package cli

/**
 * Basic cli object that handles the feedback information
 * for the tweet retrieval.
 *
 * Created by @davidelnunes on 06-02-2015.
 */
class TTConsole extends CLI{
  val progressB = {(label:String,p:Double) => {
    print("\r")
    ConsoleProgress(label,40,None,None,None)(p)
    }
  }

  var state: State = Downloading

  override def waitNotify(seconds: Long): Unit = {
    println()
    println("waiting for "+seconds / 60 +"min "+seconds % 60 + "secs")
    state = Waiting
  }

  override def tweetNotify(numRetrieved: Long, total: Long): Unit = {
    println("Tweets Retrieved: ["+numRetrieved+"/"+total+"]")
  }

  /**
   * If you supply 1.0 the bar turns to 100% and
   * resets.
   * @param progress
   */
  override def progressBar(progress: Double): Unit = {
    progressB(state.toString,progress)

    if(progress == 1.0){
      state = state match {
        case Waiting => Downloading
        case Downloading => Done
        case _ => Done
      }

      state match {
        case Done => println("Finished")
        case _ => println()
      }
    }
  }



}
