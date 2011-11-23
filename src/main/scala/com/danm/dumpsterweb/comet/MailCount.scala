package com.danm.dumpsterweb {
  package comet {

    
    
    import _root_.com.dumbster.smtp.SimpleSmtpServer
    import lib.DependencyFactory
    
    import _root_.net.liftweb._
    import http._
    import common._
    import util._
    import Helpers._
    import js._
    import JsCmds._
    import _root_.scala.xml.{ Text, NodeSeq }
    

    class MailCount(initSession: LiftSession,
      initType: Box[String],
      initName: Box[String],
      initDefaultXml: NodeSeq,
      initAttributes: Map[String, String]) extends CometActor {
      // schedule a ping every 10 seconds so we redraw
      ActorPing.schedule(this, Tick, 10 seconds)

      val smtpServer: Box[SimpleSmtpServer] = DependencyFactory.inject[SimpleSmtpServer]
      
      smtpServer.map(_.getReceivedEmailSize().toString()).openOr() -> "-1"
      
      def render = "#mailcount *" replaceWith smtpServer.map(_.getReceivedEmailSize()).openTheBox.toString()

      override def lowPriority = {
        case Tick =>
          partialUpdate(SetHtml("clock_time", Text(timeNow.toString)))
          ActorPing.schedule(this, Tick, 5 seconds)
      }
      initCometActor(initSession, initType, initName, initDefaultXml, initAttributes)
    }

    case object Tick
  }
}


