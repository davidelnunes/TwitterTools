Twitter Tools 
=============
The original idea was to create a simple tool to download tweet data. This data is to be used to explore Natural Language Processing (NLP) tools and information retrieval techniques - such as Latent Semantic Analysis (LSA) and Random Indexing (RI). 

I made this library so that I could add more functionality later on if needed. For now, its only capabilities are: to retrieve tweets based on keyword search and retrieve tweets based on a file with one search term per line. 

The library is written in _scala_ and built using _sbt_. It wraps around [Twitter4J](http://twitter4j.org/en/) and provides a way to download tweets beyond the API limits: meaning that each time the search API resource limits are reached, we wait for the next time window.

Bare in mind that twitter _ToS_ prevents you from supplying any tweet dataset by the means of automatic download of a file. If you plan on distributing any dataset of tweets I would recommend distributing the data with reference to the Tweet IDs only - since tweet content belongs to twitter. 

#Command Line Interface#

I also packed everything in a self-contained [_Jar_ file](https://github.com/davidelnunes/TwitterTools/releases/download/v1.0/twitter-tools.jar) that can be used to run the tool from the command line.

The self-contained _Jar_ was compiled with Java 7.


##Download Tweets with a Keyword##

```bash
java -jar twitter-tools.jar get-keyword -k hello -n 10000 -c twitter-tools.conf
```

This will produce a file named `hello_tweets.csv` in the current directory. You also need to supply a configuration file (in this example: _twitter-tools.conf_). This contains the API keys and secrets along with other configurations. An example configuration file you can modify is supplied in the _resources_ folder (see [here](https://github.com/davidelnunes/TwitterTools/blob/master/src/main/resources/twitter-tools.conf).

##Download Tweets with Keyword File##

You can also download tweets by supplying a file with a set of terms (one per line) and evoking the following command. 

```bash
java -jar twitter-tools.jar get-keywords -f input_keywords.txt -n 10000 -c twitter-tools.conf
```

If the input file contains _n_ lines, this will create _n_ files as output. Each file name is prefixed with the keyword used and contains _-n_ tweets. 

#Development Status & Pull Requests#
I'm not actively supporting this. I'm just modifying it as needed to fix potential bugs or add functionality according to my needs. If you find this useful and want to contribute with documentation, functionality, or anything else, feel free to contact me and create a pull request. 

#Licence#

###Twitter Tools###

* Copyright(C) 2015 [Davide Nunes](http://davidenunes.com)
* Authors: Davide Nunes - mail@davidelnunes.com

**Twitter Tools** is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

The **Twitter Tools** library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with **Twitter Tools** . If not, see [GPL 3.0](http://www.gnu.org/licenses/gpl-3.0.html).






