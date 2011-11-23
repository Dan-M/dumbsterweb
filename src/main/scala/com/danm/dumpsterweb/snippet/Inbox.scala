package com.danm.dumpsterweb {
  package snippet {

    import scala.collection.JavaConverters._

    import _root_.scala.xml.{ NodeSeq, Text }
    import _root_.scala.collection.mutable.ListBuffer
    import _root_.net.liftweb.util._
    import _root_.net.liftweb.common._
    import _root_.java.util.Date
    import _root_.com.dumbster.smtp.SimpleSmtpServer
    import _root_.com.dumbster.smtp.SmtpMessage
    import net.liftweb.widgets.tablesorter.TableSorter
    import Helpers._
    import lib._

    class Inbox {
      
      // inject the server here to make sure it gets started 
      val smtpServer: Box[SimpleSmtpServer] = DependencyFactory.inject[SimpleSmtpServer]

      def render(xhtml: NodeSeq): NodeSeq = {
        TableSorter("inbox")
      }

      def mails(xhtml: NodeSeq): NodeSeq = {
        val mailList = smtpServer.openTheBox.getReceivedEmail().asScala.toList

        var view = new ListBuffer[InboxView]()
        mailList.foreach { mail =>
          val smtpMsg = mail.asInstanceOf[SmtpMessage]
          view = view :+ new InboxView(smtpMsg.getHeaderValue("From"),
            smtpMsg.getHeaderValue("To"),
            smtpMsg.getHeaderValue("Subject"),
            smtpMsg.getBody())
        }

        view.flatMap(v =>
          bind("mails", xhtml,
            "from" -> v.from,
            "to" -> v.to,
            "subject" -> v.subject,
            "body" -> v.body))
      }
    }

    class InboxView(f: String, t: String, s: String, b: String) {
      def from = f
      def to = t
      def subject = s
      def body = b
    }
  }
}