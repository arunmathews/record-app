import com.compchallenge.record._
import org.scalatra._
import javax.servlet.ServletContext

import com.compchallenge.record.service.PingServlet

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new PingServlet, "/*")
  }
}
