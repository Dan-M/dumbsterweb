/**
 *
 */
package com.danm.dumpsterweb {
  package snippet {
    
  import net.liftweb.http.{RequestVar,SHtml,S,StatefulSnippet}
  import net.liftweb.util.{Helpers,Mailer}
  import Helpers._
  import Mailer._
  import scala.xml.NodeSeq
  import scala.collection.immutable.List
  
    /**
     * @author dm
     *
     */
    class TestMail extends StatefulSnippet {
    	
      object from extends RequestVar("from")
      object to extends RequestVar("to")
      object subject extends RequestVar("subject")
      object body extends RequestVar("body")
      
      def dispatch = {
        case "send" => send _
      }
      
      
      def send(xhtml : NodeSeq) : NodeSeq = {
        def processEntrySend() = 
          if (from == null || to == null) S.error("From and to must be provided") else sendToDumbster
        
        bind("mail", xhtml,
            "from" -> SHtml.email(from.is,from(_)),
            "to" -> SHtml.email(to.is,to(_)),
            "subject" -> SHtml.text(subject.is,subject(_)),
            "body" -> SHtml.textarea(body.is,body(_)),
            "send" -> SHtml.submit("Send", processEntrySend)
        )
      }
      
      
      
      def sendToDumbster() = {
        //scala mailer will use localhost if nothing else has been specified
        
        sendMail(From(from.toString),
            Subject(subject.toString),
            To(to.toString),
            PlainMailBodyType(body.toString()))
            
        redirectTo("mails")
      }
      
      
    }
  }
}