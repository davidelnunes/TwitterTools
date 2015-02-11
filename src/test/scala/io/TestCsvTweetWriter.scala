package io

import java.io.File

import org.scalatest.{FlatSpec, Matchers}
import tools.SearchTools


/**
 * Created by @davidelnunes on 09-02-2015.
 */
class TestCsvTweetWriter extends FlatSpec with Matchers{
  "SearchTools" should "be able to write 10 tweets to a file" in {

    val search = SearchTools("twitter-tools",None)
    val basename = "test.csv"
    val keyword = "hello"

    val outputName = (kw:String,base:String) => kw+"_"+base

    val csvWriter = new CsvTweetWriter(basename)
    val memoryWriter = new MemoryTweetWriter

    search.tweetsFromKeyword(keyword,10,csvWriter,memoryWriter)

    csvWriter.finish()
    memoryWriter.finish()


    //verify that the file exists
    val outputFile = new File(outputName(keyword,basename))
    outputFile should (exist)

    //read file
    val reader :Iterator[(Long,String)] = new CsvTweetReader(outputName(keyword,basename),true)

    //tweets from file
    val fromFile = (reader map {_ match{case (id,text) => id}}).toList

    val tweetsFromMemory = memoryWriter.tweets.get(keyword).get
    val fromMemory: List[Long] = tweetsFromMemory.unzip._1



    //we can store and retrieve the tweets correctly, the ids match
    fromMemory should have size fromMemory.size
    fromMemory should contain theSameElementsAs (fromFile)


    //clean file
    outputFile.delete
    outputFile should not (exist)
  }
}
