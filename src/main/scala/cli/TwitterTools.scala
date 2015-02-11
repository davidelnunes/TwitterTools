package cli

import java.io.File

import auth.ToolConfig
import io.CsvTweetWriter
import org.rogach.scallop._
import tools.SearchTools


class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
  val cfg = opt[String](descr = "twitter-tools configuration file with api keys, search language, etc",
    required = true)

  validate(cfg) { filename =>
    val inFile = new File(filename)
    if (!inFile.exists()) {
      Left("Please supply a configuration file")
    } else {
      Right(Unit)
    }
  }

  val get = new Subcommand("get-keyword") {
    val k = opt[String](name = "keyword",
      descr = "Fetches tweets based on a given keyword",
      required = true,
      validate = (_.length > 0))

    val n = opt[Int](name = "n",
      descr = "Number of tweets per keyword",
      required = true,
      validate = (_ > 0))

    val out = opt[String](descr = "Base name of the output file for the tweets. All the files will end up with this name / extension",
      default = Some("tweets.csv"),
      validate = (_.length > 0))

    val c = cfg
  }
  val file = new Subcommand("get-keywords") {
    val f = opt[String](name = "file",
      descr = "Name of the file with a list of keywords",
      required = true)

    validate(f) { filename =>
      val inFile = new File(filename)
      if (!inFile.exists()) {
        Left("Please supply a keyword file")
      } else {
        Right(Unit)
      }
    }

    val n = opt[Int](descr = "Number of tweets per keyword",
      required = true,
      validate = (_ > 0))

    val out = opt[String](descr = "Base name of the output file for the tweets. All the files will end up with this name / extension",
      default = Some("tweets.csv"),
      validate = (_.length > 0))

    val c = cfg

  }
}

/**
 * Twitter Tools Runner
 * Created by @davidelnunes on 10-02-2015.
 */
object TwitterTools {
  def main(args: Array[String]): Unit ={
    val conf = new Conf(args)

    val toolConfig: ToolConfig = ToolConfig(conf.cfg())
    //loggger, progress bars etc
    val textLogger = new TTConsole
    val tw = new CsvTweetWriter(conf.get.out())
    val search: SearchTools = SearchTools(toolConfig,Some(textLogger))

    if(conf.file.f.isSupplied){
      println("Fetching tweets based on keywords from "+conf.file.f())
      search.tweetsFromKeywords(conf.file.f(),conf.file.n(),tw)
    }

    if(conf.get.k.isSupplied){
      println("Fetching tweets based on the keyword "+conf.get.k())
      search.tweetsFromKeyword(conf.get.k(),conf.get.n(),tw)
    }


  }
}
