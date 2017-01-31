package edu.furman.folio.citablegraph

import org.scalatest.FlatSpec
import edu.holycross.shot.cite._
import scala.io.Source

/**
*/
class CGVertexEquality extends FlatSpec {

		val label1 = "label1"
		val label2 = "label2"
		val cite2urn1 = Cite2Urn("urn:cite2:hmt:iliadTokens.v1:123")
		val cite2urn2 = Cite2Urn("urn:cite2:hmt:iliadTokens.v1:124")
		val ctsurn1 = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.tok:1.1.1")
		val ctsurn2 = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.tok:1.2.1")
		val id1 = "e1"
		val id2 = "e1"

	"Two CGVertices" should "be equal if their dataurns match" in {
	  val cgv1 = CGVertex(id1, ctsurn1, label1)
	  val cgv2 = CGVertex(id2, ctsurn1, label2)
		assert (cgv1 == cgv2)
	}

	they should "be equal if all their properties match" in {
	  val cgv1 = CGVertex(id1, ctsurn1, label1)
	  val cgv2 = CGVertex(id1, ctsurn1, label1)
		assert (cgv1 == cgv2)
	}

	they should "NOT be equal if all their dataurns don't match" in {
	  val cgv1 = CGVertex(id1, ctsurn1, label1)
	  val cgv2 = CGVertex(id1, ctsurn2, label1)
		assert (cgv1 != cgv2)
	}



}
