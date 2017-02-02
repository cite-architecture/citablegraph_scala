package edu.furman.folio.citablegraph
package util

import org.scalatest.{ FlatSpec, PrivateMethodTester }
import org.scalatest.Matchers._
import edu.holycross.shot.cite._
import scala.io.Source
import java.io._

/**
*/
class SyntaxWriterSpec extends FlatSpec with PrivateMethodTester {


	"The SyntaxWriter" should "have a constructor" in {
 			val f = "/Users/cblackwell/Dropbox/CITE/scala/citableGraph/src/test/resources/pericles-short-tab.txt"
			val cus = "urn:cite2:fufolio:syntaxToken.v1:0007_0012_"
			val outDir = "testOutput"
			val st = SyntaxTokenizer("filesystem",f,cus)
			val sw = SyntaxWriter(st)
			assert(sw.exists)
	}

	it should "have a private method to check for a specified output directory" in pending

	it should "be able to process a non-xml text to a collectionArray to a file" in {
 			val inputf = "src/test/resources/pericles-short-tab.txt"
			val cus = "urn:cite2:fufolio:syntaxToken.v1:0007_0012_"
			val outputfile = "testing_SyntaxWriter"
			val outputdir = "target/testOutput"
			val st = SyntaxTokenizer("filesystem",inputf,cus)
			val sw = SyntaxWriter(st)
			sw.writeCollection(outputfile, outputdir)
	}

	it should "be able to write a full token collection" in pending
	it should "be able to write a collection-inventory fragment for the token collection" in pending
	it should "be able to write an ORCA collection" in pending
	it should "be able to write a collection-inventory fragment for the ORCA collection" in pending
	it should "be able to write a 2-column CTS Analytical Exemplar" in pending
	it should "be able to write a TextInventory and CitationConfig fragment for the Analytical exemplar" in pending
	it should "be able to write an index file for exemplifies/exemplifiedBy" in pending
	it should "be able to write an index file for analyzes/analyzedBy" in pending
	it should "be able to write an index file for hasAnalysis/isAnalysisFor" in pending
	it should "be able to write an index file for hasSubref/isSubrefOf" in pending
	it should "assemble a complete build in a uniquely named directory" in pending

}
