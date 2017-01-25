package edu.furman.folio.citablegraph

import org.scalatest.FlatSpec
import edu.holycross.shot.cite._
import scala.io.Source

/**
*/
class CGVertexSpec extends FlatSpec {


	"A CGVertex" should "have a constructor" in {

		val label = "a valid label"
		val urn = Cite2Urn("urn:cite2:hmt:coll.v1:obj")
		val notUrn = "notAUrn"
		val id = "v1"

		val cgv1 = CGVertex(id, urn, label)

    cgv1.label match {
      case "a valid label" => assert(true)
      case _ => fail("Should have created a label")
    }


	}

  "A CGVertex" should "have a label" in {

		val label = "a valid label"
		val urn = Cite2Urn("urn:cite2:hmt:coll.v1:obj")
		val id = "v1"

		val cgv1 = CGVertex(id, urn, label)

    cgv1.label match {
      case "a valid label" => assert(true)
      case _ => fail("Should have created a label")
    }

	}

	it should "have a non-empty label" in {
		val urn = Cite2Urn("urn:cite2:hmt:coll.v1:obj")

		val cgv1 = CGVertex("id", urn, "label")

		assertThrows[CGException] {
			val cgv2 = CGVertex("id", urn, "")
		}

	}

	it should "have a non-empty id" in {
		val urn = Cite2Urn("urn:cite2:hmt:coll.v1:obj")

		val cgv1 = CGVertex("id", urn, "label" )

		assertThrows[CGException] {
			val cgv2 = CGVertex("", urn, "label")
		}

	}

  it should "have a dataurn of type Cite2Urn or CtsUrn" in {

		val label = "a valid label"
		val cite2urn = Cite2Urn("urn:cite2:hmt:coll.v1:obj")
		val ctsurn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
		val id = "v1"

		val cgv1 = CGVertex("id", cite2urn, "label" )
		assert( cgv1.dataurn.toString ==  "urn:cite2:hmt:coll.v1:obj" )
		val cgv2 = CGVertex("id", ctsurn, "label" )
			assert( cgv2.dataurn.toString ==  "urn:cts:greekLit:tlg0012.tlg001:1.1" )


	}

	it should "have a mutable 'selected' property of type Boolean" in {
		val label = "a valid label"
		val urn = Cite2Urn("urn:cite2:hmt:coll.v1:obj")
		val id = "v1"

		val cgv1 = CGVertex(id, urn, label)

		assert( cgv1.selected != true )
		cgv1.selected = true
		assert(cgv1.selected)

		cgv1.selected = false
		assert( cgv1.selected != true )


	}

}
