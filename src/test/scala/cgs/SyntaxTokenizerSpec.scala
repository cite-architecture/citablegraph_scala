package edu.furman.folio.citablegraph
package util

import org.scalatest.{ FlatSpec, PrivateMethodTester }
import edu.holycross.shot.cite._
import scala.io.Source

/**
*/
class SyntaxTokenizerSpec extends FlatSpec with PrivateMethodTester {

	"The SyntaxTokenizer" should "have a constructor" in {
 			val f = "/Users/cblackwell/Dropbox/CITE/scala/citableGraph/src/test/resources/pericles-short-tab.txt"
			val cus = "urn:cite2:fufolio:syntaxToken.v1:0007_0012_"
			val outDir = "testOutput"
			val st = SyntaxTokenizer("filesystem",f,cus)
	}

	it should "produce 22 lines from the 'pericles-short-tab.txt' file" in {
 			val f = "/Users/cblackwell/Dropbox/CITE/scala/citableGraph/src/test/resources/pericles-short-tab.txt"
			val cus = "urn:cite2:fufolio:syntaxToken.v1:0007_0012_"
			val outDir = "testOutput"
			val st = SyntaxTokenizer("filesystem",f,cus)
			assert (st.collectionArray.size == 22)
	}

	it should "throw an exception of the 'source' paramter is not 'filesystem'" in {
 			val f = "/Users/cblackwell/Dropbox/CITE/scala/citableGraph/src/test/resources/pericles-short-tab.txt"
			val cus = "urn:cite2:fufolio:syntaxToken.v1:0007_0012_"
			val outDir = "testOutput"
			assertThrows[java.lang.IllegalArgumentException] {
				val st = SyntaxTokenizer("online",f,cus)
			}
	}

	it should "have a private method to create an array of field-records from a line" in {

			val s = """urn:cite2:fufolio:syntaxToken.v1:0007_0012_0	urn:cts:greekLit:tlg0007.tlg012.ziegler:0.title@ΠΕΡΙΚΛΗΣ[1]	urn:cite2:fufolio:tokenTypes.v1:word	ΠΕΡΙΚΛΗΣ	urn:cite2:fufolio:editorialStatus.v1:default	urn:cite2:fufolio:tokdiscourse.v1:direct"""

 			val f = "/Users/cblackwell/Dropbox/CITE/scala/citableGraph/src/test/resources/pericles-short-tab.txt"
			val cus = "urn:cite2:fufolio:syntaxToken.v1:0007_0012_"
			val outDir = "testOutput"
			val st = SyntaxTokenizer("filesystem",f,cus)

			val splitLine = PrivateMethod[Array[String]]('splitLine)
			val aa = st invokePrivate splitLine(s)
			//val result = st.splitLine(s)
			assert (aa.size == 6)

	}



	// Specific details (will parameterize)
	//val f = "/Users/cblackwell/Dropbox/CITE/scala/citableGraph/src/test/resources/pericles-tab.txt"
	//val f = "/Users/cblackwell/Dropbox/CITE/scala/citableGraph/src/test/resources/pericles-short-tab.txt"
	//val f = "/Users/cblackwell/Dropbox/CITE/scala/citableGraph/src/test/resources/pericles-notXml-tab.txt"
	//val collectionUrnString = "urn:cite2:fufolio:syntaxToken.v1:0007_0012_"


}
