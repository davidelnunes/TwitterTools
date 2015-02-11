package auth

import com.typesafe.config._
import twitter4j.TwitterFactory
import twitter4j.auth.OAuth2Token
import twitter4j.conf.Configuration


/**
 * A class that provides a [[Configuration]] object
 * from the twitter4j library.
 *
 * The configuration is built-based on a configuration file.
 * We use the typesafe configuration library.
 *
 * To build the configuration files you should use the
 * companion object for [[ToolConfig]] unless you provide
 * the access and consumer keys / secrets using the keys
 * accessible as:
 *
 * [[ToolConfig.AccessKeyK]]
 * [[ToolConfig.AccessSecretK]]
 * [[ToolConfig.ConsumerKeyK]]
 * [[ToolConfig.ConsumerSecretK]]
 *
 * @see [[Config]]
 */
class ToolConfig(val apiConfig: Map[String,String], val toolParams: Map[String,String]){
  lazy val twitterCfg: Configuration = getConfiguration(oauth2Token)

  /**
   * Returns either a user configuration or an application configuration
   * depending on the success of the oauth2 token retrieval
   * @param token
   * @return
   */
  def getConfiguration(token: Option[OAuth2Token]): Configuration ={
    val cb = new twitter4j.conf.ConfigurationBuilder()

    cb.setApplicationOnlyAuthEnabled(true)
    cb.setOAuthConsumerKey(apiConfig.get(ToolConfig.ConsumerKeyK).get)
    cb.setOAuthConsumerSecret(apiConfig.get(ToolConfig.ConsumerSecretK).get)

    if(token.isDefined){
      cb.setOAuth2TokenType(token.get.getTokenType)
      cb.setOAuth2AccessToken(token.get.getAccessToken)
    }

    cb.build
  }

  def oauth2Token: Option[OAuth2Token] = {
    var token: Option[OAuth2Token] = None

    try {
      token = Some(new TwitterFactory(getConfiguration(None)).getInstance().getOAuth2Token)
    }catch{
      case ex: Exception => {println("Could not retrieve the Oauth2 Token: "+ex.getMessage)}
    }
    return token
  }
}


object ToolConfig {
  val ConsumerKeyK = "consumer_key"
  val ConsumerSecretK = "consumer_secret"
  val AccessKeyK = "access_key"
  val AccessSecretK = "access_secret"

  val SearchLangK = "lang"



  def apply(config: Config): ToolConfig = {
    config.checkValid(ConfigFactory.defaultReference(), "twitter-tools")

    val configAPI = config.getConfig("twitter-tools.api")

    //api keys
    var apiConfig :Map[String, String] = Map()
    apiConfig += (ConsumerKeyK-> configAPI.getString(ConsumerKeyK))
    apiConfig += (ConsumerSecretK-> configAPI.getString(ConsumerSecretK))
    apiConfig += (AccessKeyK-> configAPI.getString(AccessKeyK))
    apiConfig += (AccessSecretK-> configAPI.getString(AccessSecretK))

    //search tools
    var toolParams: Map[String, String] = Map()

    try {
      val searchConfig = config.getConfig("twitter-tools.search")
      toolParams += (SearchLangK -> searchConfig.getString(SearchLangK))
    }catch{
      case e: Exception => {}
    }

    new ToolConfig(apiConfig,toolParams)
  }

  /**
   * Creates a configuration object based on the name
   * of the file (without extension).
   *
   * @param configPath the path to the file
   * @return an authentication object
   */
  def apply(configPath: String): ToolConfig ={
    val apiConfig: Config = ConfigFactory.load(configPath)
    this(apiConfig)
  }
}