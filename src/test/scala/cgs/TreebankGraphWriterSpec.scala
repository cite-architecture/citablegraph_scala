package edu.furman.folio.citablegraph
package util

import org.scalatest.{ FlatSpec, PrivateMethodTester }
import org.scalatest.Matchers._
import edu.holycross.shot.cite._
import scala.io.Source
import java.io._

/**
*/
class TreebankGraphWriterSpec extends FlatSpec with PrivateMethodTester {


	"The TreebankGraphWriter" should "have a constructor" in {
			val urnMap = Map(
				"1" -> "urn:cite2:test:tbgraphTest.v1:1",
				"2" -> "urn:cite2:test:tbgraphTest.v1:2"
			)

			val tbPath = "src/test/resources/Treebanks/2SentenceTest.xml"
			val outDir = "target/graphs"

			val tbgw = TreebankGraphWriter(urnMap, tbPath, outDir)
			assert(tbgw.exists)
	}

	it should "remap the urnMap to use real Cite2Urns" in {
			val urnMap = Map(
				"1" -> "urn:cite2:test:tbgraphTest.v1:1",
				"2" -> "urn:cite2:test:tbgraphTest.v1:2"
			)

			val tbPath = "src/test/resources/Treebanks/2SentenceTest.xml"
			val outDir = "target/graphs"

			val tbgw = TreebankGraphWriter(urnMap, tbPath, outDir)
			tbgw.urnMap.values.foreach( u => assert ( u.getClass.toString == "class edu.holycross.shot.cite.Cite2Urn"))
	}

	it should "be able to find a URN matching a part-of-speech string" in {
		val urnMap = Map(
			"1" -> "urn:cite2:test:tbgraphTest.v1:1",
			"2" -> "urn:cite2:test:tbgraphTest.v1:2"
		)

		val tbPath = "src/test/resources/Treebanks/2SentenceTest.xml"
		val outDir = "target/graphs"

		val tbgw = TreebankGraphWriter(urnMap, tbPath, outDir)

		val posUrn = tbgw.findPosUrn("--p---fa-")
		assert(posUrn == "urn:cite2:fufolio:partsOfSpeech.v1:3")

	}

	it should "throw an exception if the number of items in the urnMap does not match the number of sentences in the XML file" in {
		val urnMap = Map(
			"1" -> "urn:cite2:test:tbgraphTest.v1:1"
		)

		val tbPath = "src/test/resources/Treebanks/2SentenceTest.xml"
		val outDir = "target/graphs"
		assertThrows[Exception] {
			val tbgw = TreebankGraphWriter(urnMap, tbPath, outDir)
		}
	}

	it should "output a Vertex list" in {
		val urnMap = Map(
			"1" -> "urn:cite2:test:tbgraphTest.v1:1",
			"2" -> "urn:cite2:test:tbgraphTest.v1:2"
		)

		val tbPath = "src/test/resources/Treebanks/2SentenceTest.xml"
		val outDir = "target/graphs"
		val tbgw = TreebankGraphWriter(urnMap, tbPath, outDir)
		tbgw.saveVertexCollection(true)
	}

	it should "output an edge list" in {
		val urnMap = Map(
			"1" -> "urn:cite2:test:tbgraphTest.v1:1",
			"2" -> "urn:cite2:test:tbgraphTest.v1:2"
		)

		val tbPath = "src/test/resources/Treebanks/2SentenceTest.xml"
		val outDir = "target/graphs"
		val tbgw = TreebankGraphWriter(urnMap, tbPath, outDir)
		tbgw.saveEdgeCollection(true)
	}

	it should "output a graph list" in {
		val urnMap = Map(
			"1" -> "urn:cite2:test:tbgraphTest.v1:1",
			"2" -> "urn:cite2:test:tbgraphTest.v1:2"
		)

		val tbPath = "src/test/resources/Treebanks/2SentenceTest.xml"
		val outDir = "target/graphs"
		val tbgw = TreebankGraphWriter(urnMap, tbPath, outDir)
		tbgw.saveGraphCollection
	}


}
