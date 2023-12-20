package playground

import akka.actor.{Actor, ActorSystem, Props}

object akkaModel extends App{

  //part 1 - actor system
  val actorSystem = ActorSystem("firstActorSystem")
  println(actorSystem.name)

  //part 2 - creating actors
  class WordCounter extends Actor {
    //internal data
    var totalWords = 0

    //behavior of actor
    //receive method is a partial function
    def receive: PartialFunction[Any, Unit] = {
      case message:String => {
        println(s"[message counter] I have received: $message")
        totalWords += message.split(" ").length
      }
      case anything => println(s"[word counter] I cannot read this: ${anything.toString}")
    }
  }

  //part 3 - instantiate our actor
  //val actorCounter = new WordCounter //it does not work

  val wordCounter = actorSystem.actorOf(Props[WordCounter], "WordCounter")
  val anotherWordCounter = actorSystem.actorOf(Props[WordCounter], "anotherWordCounter")

  //part 4 - communicate with actor
  wordCounter ! "I am learning Akka, and its pretty damn cool!"
  anotherWordCounter ! "I am sending another message!"
  //asynchronous

  object Person {
    def props(name:String) = Props(new Person(name))
  }

  class Person(name:String) extends Actor {
    def receive = {
      case "hi" => println(s"Hi, my name is $name")
      case _ =>
    }
  }

  val person = actorSystem.actorOf(Person.props("Pedro"))
  person ! "hi"


}
