package io

import java.io.File

import org.scalatest.{FlatSpec, Matchers}
import tools.SearchTools

import scala.io.Source


/**
 * Created by @davidelnunes on 09-02-2015.
 */
class TestKWFromFile extends FlatSpec with Matchers{
  "SearchTools" should "be able to create files based on a keywords file" in {

    val search = SearchTools("twitter-tools",None)
    val basename = "tweets.csv"

    val kwFile = "keywords"

    val outputName = (kw:String,base:String) => kw+"_"+base
    val csvWriter = new CsvTweetWriter(basename)


    val is = getClass().getClassLoader.getResourceAsStream(kwFile)
    val kwF = Source.fromInputStream(is)
    kwF should not be (null)

    search.tweetsFromKeywords(kwF,10,csvWriter)
    csvWriter.finish()


    kwF.getLines foreach {
      keyword => {
        val outFile = new File(outputName(keyword,basename))
        outFile should (exist)
        outFile.delete
        outFile should not (exist)

      }

    }
    kwF.close()
  }
}
