/**
 *
 */
package com.danm.dumpsterweb.lib

import javax.servlet.{ServletContextListener,ServletContextEvent}
import com.dumbster.smtp.SimpleSmtpServer
import net.liftweb.common.Box
import net.liftweb.common.Full

/**
 * @author dm
 *
 */
class ContextShutdownHook extends ServletContextListener {

  val smtpServer: Box[SimpleSmtpServer] = DependencyFactory.inject[SimpleSmtpServer]
  
  
  def contextInitialized(sce: ServletContextEvent): Unit = {}

  def contextDestroyed(sce: ServletContextEvent): Unit = {
    println("Stopping SmtpServer context...")
    smtpServer match {
      case Full(smtpServer) => if (!smtpServer.isStopped()) smtpServer.stop()
      case _ => Nil
    }
  }
}