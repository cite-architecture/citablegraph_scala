package edu.furman.folio.citablegraph
package util

import org.scalatest.{ FlatSpec, PrivateMethodTester }
import org.scalatest.Matchers._
import edu.holycross.shot.cite._
import scala.io.Source
import java.io._

/**
*/
class TreebankerSpec extends FlatSpec with PrivateMethodTester {


	"The Treebanker" should "have a constructor" in {
 			val f = "/Users/cblackwell/Dropbox/CITE/scala/citableGraph/src/test/resources/pericles-short-tab.txt"
			val cus = "urn:cite2:fufolio:syntaxToken.v1:0007_0012_"
			val outDir = "testOutput"
			val username= "Christopher Blackwell"
			val email = "cwblackwell@gmail.com"
			val perseidsId = "http://data.perseus.org/sosol/users/Christopher%20Blackwell"
			val st = SyntaxTokenizer("filesystem",f,cus)
			val ua = Array(
				CtsUrn("urn:cts:greekLit:group.work.ed.ex:1-5"),
				CtsUrn("urn:cts:greekLit:group.work.ed.ex:6-10")
			)
			val tb = Treebanker(st, username, email, perseidsId, ua )
			assert(tb.exists)
	}

	it should "be able to report the index, in a 'SyntaxToken' string, of the beginning and end of a range" in {

 			val f = "/Users/cblackwell/Dropbox/CITE/scala/citableGraph/src/test/resources/pericles-short-tab.txt"
			val cus = "urn:cite2:fufolio:syntaxToken.v1:0007_0012_"
			val outDir = "testOutput"
			val username= "Christopher Blackwell"
			val email = "cwblackwell@gmail.com"
			val perseidsId = "http://data.perseus.org/sosol/users/Christopher%20Blackwell"
			val st = SyntaxTokenizer("filesystem",f,cus)
			val ua = Array(
				CtsUrn("urn:cts:greekLit:tlg0007.tlg012.shortText.syntaxToken:1.2.1-1.2.6a")
			)
			val tb = Treebanker(st, username, email, perseidsId, ua )

			val testRange = CtsUrn("urn:cts:greekLit:tlg0007.tlg012.shortTest.syntaxToken:1.2.1-1.2.6a")

		  val vstring = st.collectionArray.map( l => l ).toVector
			assert (tb.rangeBeginIndex(testRange, vstring) == 4)
			assert (tb.rangeEndIndex(testRange, vstring) == 10)
	}

	it should "be able to slice out a Vector of citable nodes, based on a range-URN" in {

 			val f = "/Users/cblackwell/Dropbox/CITE/scala/citableGraph/src/test/resources/pericles-short-tab.txt"
			val cus = "urn:cite2:fufolio:syntaxToken.v1:0007_0012_"
			val outDir = "testOutput"
			val username= "Christopher Blackwell"
			val email = "cwblackwell@gmail.com"
			val perseidsId = "http://data.perseus.org/sosol/users/Christopher%20Blackwell"
			val st = SyntaxTokenizer("filesystem",f,cus)
			val ua = Array(
				CtsUrn("urn:cts:greekLit:tlg0007.tlg012.shortText.syntaxToken:1.2.1-1.2.6a")
			)
			val tb = Treebanker(st, username, email, perseidsId, ua )

			val testRange = CtsUrn("urn:cts:greekLit:tlg0007.tlg012.shortTest.syntaxToken:1.2.1-1.2.6a")

		  val vstring = st.collectionArray.map( l => l ).toVector

			assert (tb.sliceRange(testRange, vstring).size == 7)

	}

	it should "export a working treebank file given an array of URNs" in {

 			val f = "/Users/cblackwell/Dropbox/CITE/scala/citableGraph/src/test/resources/pericles-tab.txt"
			val cus = "urn:cite2:fufolio:syntaxToken.v1:0007_0012_"
			val outDir = "testOutput"
			val username= "Christopher Blackwell"
			val email = "cwblackwell@gmail.com"
			val perseidsId = "http://data.perseus.org/sosol/users/Christopher%20Blackwell"
			val st = SyntaxTokenizer("filesystem",f,cus)
			val ua = Array(
				CtsUrn("urn:cts:greekLit:tlg0007.tlg012.ziegler.syntaxToken:1.1.1-1.1.49"),
			CtsUrn("urn:cts:greekLit:tlg0007.tlg012.ziegler.syntaxToken:1.2.1-1.2.41"),
			CtsUrn("urn:cts:greekLit:tlg0007.tlg012.ziegler.syntaxToken:1.2.42-1.2.108"),
			CtsUrn("urn:cts:greekLit:tlg0007.tlg012.ziegler.syntaxToken:1.3.1-1.3.34"),
			CtsUrn("urn:cts:greekLit:tlg0007.tlg012.ziegler.syntaxToken:1.4.1-1.4.22"),
			CtsUrn("urn:cts:greekLit:tlg0007.tlg012.ziegler.syntaxToken:1.4.23-1.4.70")
			)
			val tb = Treebanker(st, username, email, perseidsId, ua )

			tb.writeTreebank

	}

}
