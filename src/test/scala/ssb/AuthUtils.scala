package ssb

import java.io.FileInputStream

import com.google.auth.oauth2.GoogleCredentials

object AuthUtils {
  def getAccessToken: String = {
    val home = System.getProperty("user.home")
    val credentials = GoogleCredentials
      .fromStream(new FileInputStream(s"$home/.config/gcloud/application_default_credentials.json"))
      .createScoped("https://www.googleapis.com/auth/bigquery")
    credentials.refreshIfExpired()
    credentials.getAccessToken.getTokenValue
  }
}
