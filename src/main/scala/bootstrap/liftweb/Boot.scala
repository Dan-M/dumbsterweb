package bootstrap.liftweb

import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.provider._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import com.danm.dumpsterweb.comet._
import net.liftweb.widgets.tablesorter.TableSorter
import com.dumbster.smtp.SimpleSmtpServer
import com.danm.dumpsterweb.lib.DependencyFactory

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {

  var smtpServer = DependencyFactory.inject[SimpleSmtpServer]

  def boot {
    // where to search snippet
    LiftRules.addToPackages("com.danm.dumpsterweb")

    // Build SiteMap
    def sitemap() = SiteMap(
      Menu("Home") / "index",
      Menu("Test") / "test",
      Menu("Mails") / "mails")
    LiftRules.setSiteMapFunc(() => sitemap())

    
    //Comet widget that shows actual mail count below the menu
    LiftRules.cometCreation.append {
      case CometCreationInfo("mailcount",
        name,
        defaultXml,
        attributes,
        session) =>
        new MailCount(session, Full("MailCount"),
          name, defaultXml, attributes)

    }

    //Sorting the inbox table
    TableSorter.init()
    
    /*
     * Show the spinny image when an Ajax call starts
     */
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    /*
     * Make the spinny image go away when it ends
     */
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    LiftRules.early.append(makeUtf8)

    //LiftRules.unloadHooks +=  stopDumbster _
    
  }

  
  def stopDumbster() : Unit = { smtpServer.openTheBox.stop() }
  
  /**
   * Force the request to be UTF-8
   */
  private def makeUtf8(req: HTTPRequest) {
    req.setCharacterEncoding("UTF-8")
  }
}
