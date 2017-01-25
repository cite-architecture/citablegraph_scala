package edu.furman.folio.citablegraph

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.io.Source
import scala.collection.mutable.ArrayBuffer

case class CGVertex(id: String, dataurn: Urn, label: String) {

	var selected:Boolean = false

	require(label.size > 0, throw CGException("CGVertex: Label cannot be empty.") )
	require(id.size > 0, throw CGException("CGVertex: ID cannot be empty.") )



}
