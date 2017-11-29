package edu.furman.folio.citablegraph

import org.scalatest.FlatSpec
import edu.holycross.shot.cite._
import scala.io.Source

/**
*/
class CGEdgeSpec extends FlatSpec {

		val label = "a valid label"
		val cite2urn1 = Cite2Urn("urn:cite2:hmt:iliadTokens.v1:123")
		val cite2urn2 = Cite2Urn("urn:cite2:hmt:iliadTokens.v1:124")
		val ctsurn1 = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.tok:1.1.1")
		val ctsurn2 = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.tok:1.2.1")
		val relationurn = Cite2Urn("urn:cite2:hmt:syntaxRelations.v1:attribute")
		val notUrn = "notAUrn"
		val id = "e1"

	"A CGEdge" should "have a constructor" in {
	  val cge1 = CGEdge(id, cite2urn1, cite2urn2, relationurn, label)
	}

  it should "have an id" in {
		assertThrows[CGException] {
			val cge2 = CGEdge("", cite2urn1, cite2urn2, relationurn, label)
		}
	}

  it should "have a label" in {
		assertThrows[CGException] {
			val cge3 = CGEdge(id, cite2urn1, cite2urn2, relationurn, "")
		}
	}

  it should "have a sourceurn of type Cite2Urn or CtsUrn" in {
	  val cge4 = CGEdge(id, cite2urn1, cite2urn2, relationurn, label)
	  val cge5 = CGEdge(id, ctsurn1, cite2urn2, relationurn, label)
	}

  it should "have a targeturn of type Cite2Urn or CtsUrn" in {
	  val cge4 = CGEdge(id, ctsurn1, cite2urn2, relationurn, label)
	  val cge5 = CGEdge(id, ctsurn1, ctsurn2, relationurn, label)
	}

  it should "have a dataUrn of type Cite2Urn (only)" in {
	  val cge1 = CGEdge(id, ctsurn1, cite2urn2, relationurn, label)
	  assertDoesNotCompile("val cge2 = CGEdge(id, ctsurn1, cite2urn2, ctsurn2, label)")
	}

	it should "have a mutable 'selected' property of type Boolean" in {
	  val cge1 = CGEdge(id, ctsurn1, cite2urn2, relationurn, label)
		assert (cge1.selected == false)
		cge1.selected = true
		assert (cge1.selected)
		cge1.selected = false
		assert (cge1.selected == false)
	}

}
