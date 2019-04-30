import com.compchallenge.record._
import org.scalatra._
import org.slf4j.LoggerFactory
import com.typesafe.config.ConfigFactory
import java.io.File
import java.util.concurrent.TimeUnit
import com.mchange.v2.c3p0.ComboPooledDataSource
import com.compchallenge.record.bootstrap.BootstrapConfigHelper
import com.compchallenge.record.bootstrap.BootstrapConfigHelper._
import com.compchallenge.record.flyway.FlywayMigration
import com.compchallenge.record.database.tablemapping.H2TablesWithDb
import com.compchallenge.record.dependency.impl.RecordApiDBImpl
import com.compchallenge.record.handler.RecordRequestHandler

import javax.servlet.ServletContext

import com.compchallenge.record.service.PingServlet
import com.compchallenge.record.service.RecordServlet

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Bootstraps Scalatra servlets
  */
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

  val cpds = new ComboPooledDataSource
  val env = getFromSysOrConf(scalatraEnvKey, conf)
  cpds.setMaxPoolSize(conf.getInt("c3p0.maxPoolSize"))
  cpds.setJdbcUrl(dbUrl)
  cpds.setUser(dbUser)
  cpds.setPassword(dbPassword)
  val tablesWithDb = new H2TablesWithDb(cpds)

  logger.info("Created c3p0 connection pool and db")

  override def init(context: ServletContext) {
    val recordApi = new RecordApiDBImpl(tablesWithDb)
    val recordHandler = new RecordRequestHandler(recordApi)

    context.mount(new RecordServlet(recordHandler), "/*")
    context.mount(new PingServlet, "/*")
  }

  override def destroy(context: ServletContext) {
    super.destroy(context)
    closeDbConnection()
  }
  
  private def closeDbConnection() {
    logger.info("Closing c3po connection pool and slick resources")
    tablesWithDb.db.close()
    cpds.close()
  }
}
