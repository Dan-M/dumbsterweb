package com.danm.dumpsterweb {
  package snippet {

    import _root_.scala.xml.{ NodeSeq, Text }
    import _root_.net.liftweb.util._
    import _root_.net.liftweb.common._
    import _root_.java.util.Date
    import _root_.com.dumbster.smtp.SimpleSmtpServer
    import Helpers._
    
    import lib._

    class Hello {
      
      // inject the server here to make sure it gets started 
      val smtpServer: Box[SimpleSmtpServer] = DependencyFactory.inject[SimpleSmtpServer] 
      val smtpport: Box[Int] = DependencyFactory.inject[Int]
      val smtphost : Box[String] = DependencyFactory.inject[String]

      def server = "#hostname *" replaceWith smtphost

      def port = "#port *" replaceWith smtpport.openTheBox.toString()

    }

  }
}
