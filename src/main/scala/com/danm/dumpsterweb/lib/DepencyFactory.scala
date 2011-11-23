package com.danm.dumpsterweb {
  package lib {

    import net.liftweb._
    import http._
    import util._
    import common._
    import _root_.java.util.Date
    import com.dumbster.smtp.SimpleSmtpServer
    /**
     * A factory for generating new instances of Date.  You can create
     * factories for each kind of thing you want to vend in your application.
     * An example is a payment gateway.  You can change the default implementation,
     * or override the default implementation on a session, request or current call
     * stack basis.
     */
    object DependencyFactory extends Factory {
      
      val smtpPortVal = 25252
      val smtpHostVal = "localhost"
        
      implicit object time extends FactoryMaker(Helpers.now _)

      implicit object smtpport extends FactoryMaker(smtpPortVal)
      implicit object smtphost extends FactoryMaker(smtpHostVal)      

      implicit object smtpserver extends FactoryMaker(SimpleSmtpServer.start(smtpPortVal))

      
      /**
       * objects in Scala are lazily created.  The init()
       * method creates a List of all the objects.  This
       * results in all the objects getting initialized and
       * registering their types with the dependency injector
       */
      private def init() {
        
        System.setProperty("mail.smtp.host",smtpHostVal)
        System.setProperty("mail.smtp.port",smtpPortVal.toString())        
        List(time, smtpport, smtphost,smtpserver)
      }
      init()
    }

  }
}
