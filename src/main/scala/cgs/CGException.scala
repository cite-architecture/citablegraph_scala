package edu.furman.folio
package citablegraph {

  case class CGException(message: String = "", cause: Option[Throwable] = None) extends Exception(message) {
    cause.foreach(initCause)
  }

}
