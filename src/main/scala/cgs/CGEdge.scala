package edu.furman.folio.citablegraph

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.io.Source
import scala.collection.mutable.ArrayBuffer

case class CGEdge(id: String, sourceurn: Urn, targeturn: Urn, relationurn: Cite2Urn, label: String)
{ override def equals(other: Any) = other match {
    case that: CGEdge => ( (that.sourceurn.toString == this.sourceurn.toString) &&
                         (that.targeturn.toString == this.targeturn.toString) &&
                         (that.relationurn.toString == this.relationurn.toString) )
    case _            => false
}

	var selected: Boolean = false

	require(label.size > 0, throw CGException("CGEdge: Label cannot be empty.") )
	require(id.size > 0, throw CGException("CGEdge: ID cannot be empty.") )




}
