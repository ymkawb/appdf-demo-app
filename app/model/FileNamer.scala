package model

import java.util.Random

object FileNamer {

  private lazy val random = new Random
  
  def nextFileName() : String = random.nextInt(1024).toString
}