package edu.furman.folio.citablegraph

import org.scalatest.FlatSpec
import edu.holycross.shot.cite._
import scala.io.Source

/**
*/
class CGEdgeEquality extends FlatSpec {

		val label1 = "label1"
		val label2 = "label2"
		val cite2urn1 = Cite2Urn("urn:cite2:hmt:iliadTokens.v1:123")
		val cite2urn2 = Cite2Urn("urn:cite2:hmt:iliadTokens.v1:124")
		val ctsurn1 = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.tok:1.1.1")
		val ctsurn2 = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.tok:1.2.1")
		val relationurn1 = Cite2Urn("urn:cite2:hmt:syntaxRelations.v1:attribute")
		val relationurn2 = Cite2Urn("urn:cite2:hmt:syntaxRelations.v1:subject")
		val id1 = "e1"
		val id2 = "e1"

	"Two CGEdges" should "be equal if their sourceurn, targeturn, relationurn, and label match" in {
	  val cge1 = CGEdge(id1, ctsurn1, cite2urn1, relationurn1, label1)
	  val cge2 = CGEdge(id2, ctsurn1, cite2urn1, relationurn1, label1)
		assert (cge1 == cge2)
	}

	they should "be equal if their sourceurn, targeturn, and relationurn match" in {
	  val cge1 = CGEdge(id1, ctsurn1, cite2urn1, relationurn1, label1)
	  val cge2 = CGEdge(id2, ctsurn1, cite2urn1, relationurn1, label2)
		assert (cge1 == cge2)
	}

	they should " NOT be equal if their sourceurns differ" in {
	  val cge1 = CGEdge(id1, ctsurn1, cite2urn1, relationurn1, label1)
	  val cge2 = CGEdge(id2, ctsurn2, cite2urn1, relationurn1, label1)
		assert (cge1 != cge2)
	}

	they should " NOT be equal if their targeturns differ" in {
	  val cge1 = CGEdge(id1, ctsurn1, cite2urn1, relationurn1, label1)
	  val cge2 = CGEdge(id2, ctsurn1, cite2urn2, relationurn1, label1)
		assert (cge1 != cge2)
	}

	they should " NOT be equal if their relationurns differ" in {
	  val cge1 = CGEdge(id1, ctsurn1, cite2urn1, relationurn1, label1)
	  val cge2 = CGEdge(id2, ctsurn1, cite2urn1, relationurn2, label1)
		assert (cge1 != cge2)
	}




}
