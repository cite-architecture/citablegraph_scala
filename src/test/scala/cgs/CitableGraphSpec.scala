package edu.furman.folio.citablegraph

import org.scalatest.FlatSpec
import edu.holycross.shot.cite._
import scala.io.Source

/**
*/
class CitableGraphSpec extends FlatSpec {

  "A Citable Graph " should "have a URN" in pending
  it should "have a label" in pending
  it should "have a Vector of CGEdge objects" in pending
  it should "have a Vector of CGVertex objects" in pending

  it should "offer a constructor signature for instantiating a corpus from a delimited text file" in pending

	it should "prune duplicate edges during construction" in pending


	it should "enforce uniqueness of id-values for CGEge and CGVertex objects" in pending
	it should "enforce that all edges have source- and target-urns present in the vertex vector" in pending

	it should "offer a method returning the number of vertices" in pending
	it should "offer a method returning the number of edges" in pending
	it should "offer a method returning orphaned vertices (not present in any edge)" in pending
	it should "offer a method returning TRUE if a URN is represented in the graph" in pending
	it should "offer a method identifying whether all vertices represent the same text or object" in pending

	it should "construct successfully with empty edge and vertex vectors" in pending







}
