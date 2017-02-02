package edu.furman.folio.citablegraph
package util

import org.scalatest.{ FlatSpec, PrivateMethodTester }
import org.scalatest.Matchers._
import edu.holycross.shot.cite._
import scala.io.Source
import java.io._

/**
*/
class SyntaxTokenizerSpec extends FlatSpec with PrivateMethodTester {

	"The SyntaxTokenizer" should "have a constructor" in {
		val f = "src/test/resources/pericles-short-tab.txt"
		val cus = "urn:cite2:fufolio:syntaxToken.v1:0007_0012_"
		val outDir = "testOutput"
		val st = SyntaxTokenizer("filesystem",f,cus)
	}

	it should "produce 21 records from the 'pericles-short-tab.txt' file" in {
		val f = "src/test/resources/pericles-short-tab.txt"
		val cus = "urn:cite2:fufolio:syntaxToken.v1:0007_0012_"
		val outDir = "testOutput"
		val st = SyntaxTokenizer("filesystem",f,cus)
		assert (st.collectionArray.size == 21)
	}

	it should "throw an exception if the 'source' paramter is not 'filesystem' or 'url'" in {
		val f = "src/test/resources/pericles-short-tab.txt"
		val cus = "urn:cite2:fufolio:syntaxToken.v1:0007_0012_"
		val outDir = "testOutput"
		//assertThrows[java.lang.IllegalArgumentException] {
		assertThrows[Exception] {
			val st = SyntaxTokenizer("online",f,cus)
		}
	}

	it should "be able to read files from a file:///… URL" in {
		val f = "file:////Users/cblackwell/Dropbox/CITE/scala/citableGraph/src/test/resources/pericles-short-tab.txt"
		val cus = "urn:cite2:fufolio:syntaxToken.v1:0007_0012_"
		val outDir = "testOutput"
		val st = SyntaxTokenizer("URL",f,cus)
		assert (st.collectionArray.size == 21)
	}

	it should "throw a sensible exception if a file can't be read" in {
		val f = "file:///scala/citableGraph/src/test/resources/pericles-short-tab.txt"
		val cus = "urn:cite2:fufolio:syntaxToken.v1:0007_0012_"
		val outDir = "testOutput"
		assertThrows[Exception]{
			val st = SyntaxTokenizer("URL",f,cus)
		}
	}

	it should "be able to read files from an http://… URL" in {
		val f = "https://raw.githubusercontent.com/cite-architecture/citablegraph_scala/master/src/test/resources/pericles-short-tab.txt"
		val cus = "urn:cite2:fufolio:syntaxToken.v1:0007_0012_"
		val outDir = "testOutput"
		val st = SyntaxTokenizer("URL",f,cus)
		assert (st.collectionArray.size == 21)
	}

it should "have a private method to create an array of field-records from a line" in {
	val s = """urn:cite2:fufolio:syntaxToken.v1:0007_0012_0	urn:cts:greekLit:tlg0007.tlg012.ziegler:0.title@ΠΕΡΙΚΛΗΣ[1]	urn:cite2:fufolio:tokenTypes.v1:word	ΠΕΡΙΚΛΗΣ	urn:cite2:fufolio:editorialStatus.v1:default	urn:cite2:fufolio:tokdiscourse.v1:direct"""

	val f = "src/test/resources/pericles-short-tab.txt"
	val cus = "urn:cite2:fufolio:syntaxToken.v1:0007_0012_"
	val outDir = "testOutput"
	val st = SyntaxTokenizer("filesystem",f,cus)

	val splitLine = PrivateMethod[Array[String]]('splitLine)
	val aa = st invokePrivate splitLine(s)
	//val result = st.splitLine(s)
	assert (aa.size == 6)
}


it should "be able to produce a 4-column ORCA alignment" in {
	val inputf = "src/test/resources/pericles-short-tab.txt"
	val cus = "ur:cite2:fufolio:syntaxToken-shortTest.v1:"
	val oUrn = "urn:cite2:fufolio:stOrca.v1:0007_0012_"
	val st = SyntaxTokenizer("filesystem",inputf,cus)
	val orca = st.makeOrcaCollection(oUrn)
	assert ( orca(21).split("\t")(4) == "τὸ" )
}

it should "be able to process a lengthy file, like Plutarch's Pericles" in {
	val inputf = "src/test/resources/pericles-tab.txt"
	val cus = "urn:cite2:fufolio:syntaxToken0007_0012.v1:"
	val outputfile = "0007_0012_ziegler"
	val outputdir = "target/testOutput"
	val st = SyntaxTokenizer("filesystem",inputf,cus)
	val testStart = st.collectionArray(0).split("\t")(3)
	val lastLine = st.collectionArray.size - 2
	val ll = st.collectionArray(lastLine).split("\t")
	val penultToken = ll(3)
	assert (testStart == "ΠΕΡΙΚΛΗΣ")
	assert (penultToken == "γενέσθαι")
}

it should "be able to process a REALLY lengthy file, like Herodotus" in {
	val inputf = "src/test/resources/herodotus-tab.txt"
	val cus = "urn:cite2:fufolio:syntaxToken0016_0001.v1:"
	val outputfile = "0016_0001_perseus"
	val outputdir = "target/testOutput"
	val st = SyntaxTokenizer("filesystem",inputf,cus)
	val testStart = st.collectionArray(0).split("\t")(3)
	val lastLine = st.collectionArray.size - 2
	val ll = st.collectionArray(lastLine).split("\t")
	val penultToken = ll(3)
	assert (testStart == "Ἡροδότου")
	assert (penultToken == "δουλεύειν")
}

it should "be able to catch tokens of type 'other'" in {
	val inputf = "src/test/resources/Allen-Iliad.txt"
	val cus = "urn:cite2:fufolio:syntaxToken0012_0001.v1:"
	val outputfile = "0012_0001_allen"
	val outputdir = "target/testOutput"
	val st = SyntaxTokenizer("filesystem",inputf,cus)
	val testToken = st.collectionArray(5).split("\t")(2)
	assert (testToken == "urn:cite2:fufolio:tokenTypes.v1:other")
}

// Specific details (will parameterize)
//val f = "/Users/cblackwell/Dropbox/CITE/scala/citableGraph/src/test/resources/pericles-tab.txt"
//val f = "/Users/cblackwell/Dropbox/CITE/scala/citableGraph/src/test/resources/pericles-short-tab.txt"
//val f = "/Users/cblackwell/Dropbox/CITE/scala/citableGraph/src/test/resources/pericles-notXml-tab.txt"
//val collectionUrnString = "urn:cite2:fufolio:syntaxToken.v1:0007_0012_"


}
