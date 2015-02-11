package cli

import org.scalatest.{FlatSpec, Matchers}

/**
 * Created by Davide on 06/02/2015.
 */
class TestProgress extends FlatSpec with Matchers{
  "Progress Bar" should "work" in {
    val cli:CLI = new TTConsole

    println(cli.state.toString)
    cli.state should be (Downloading)

    cli.progressBar(0.5)
    cli.progressBar(0.9)

    cli.waitNotify(2)
    cli.state should be (Waiting)

    cli.progressBar(0.5)
    cli.progressBar(1.0)

    cli.state should be (Downloading)
    println(cli.state.toString)


    cli.progressBar(1.0)
    cli.state should be (Done)

  }

}
