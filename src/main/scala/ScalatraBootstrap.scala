import com.compchallenge.record._
import org.scalatra._
import org.slf4j.LoggerFactory
import com.typesafe.config.ConfigFactory
import java.io.File
import java.util.concurrent.TimeUnit
import com.compchallenge.record.bootstrap.BootstrapConfigHelper
import com.compchallenge.record.bootstrap.BootstrapConfigHelper._
import com.compchallenge.record.flyway.FlywayMigration
import javax.servlet.ServletContext

import com.compchallenge.record.service.PingServlet

class ScalatraBootstrap extends LifeCycle with BootstrapConfigHelper {
  val logger = LoggerFactory.getLogger(getClass)
  val baseConf = ConfigFactory.load()
  val fileLocation = "override/local.properties"
  val overrideConf =  ConfigFactory.parseFile(new File(fileLocation))
  val conf = ConfigFactory.load(overrideConf).withFallback(baseConf)
  val dbUrl = getFromSysOrConf(jdbcUrlKey, conf)
  val dbUser = getFromSysOrConf(dbUserKey, conf)
  val dbPassword = getFromSysOrConf(dbPasswordKey, conf)

  FlywayMigration.migrate(dbUrl, dbUser, dbPassword)

  override def init(context: ServletContext) {
    context.mount(new PingServlet, "/*")
  }
}
