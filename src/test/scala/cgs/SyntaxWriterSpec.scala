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

	it should "have a private mathod for constructing a base filename from the CTS URN" in {
 			val f = "/Users/cblackwell/Dropbox/CITE/scala/citableGraph/src/test/resources/pericles-short-tab.txt"
			val cus = "urn:cite2:fufolio:syntaxToken.v1:0007_0012_"
			val outDir = "testOutput"
			val st = SyntaxTokenizer("filesystem",f,cus)
			val sw = SyntaxWriter(st)

			val getBaseFileName = PrivateMethod[String]('getBaseFileName)
			val aa = sw invokePrivate getBaseFileName()
			assert (aa == "greekLit_tlg0007-tlg012-shortTest")
	}

	it should "be able to write a full token collection" in {
 			val tabfile = "src/test/resources/pericles-short-tab.txt"
			val collectionurn = "urn:cite2:fufolio:short.v1:"
			val st = SyntaxTokenizer("filesystem",tabfile,collectionurn)
			val sw = SyntaxWriter(st)
			sw.writeCollection
	}

	it should "be able to write a collection-inventory fragment for the token collection" in {
 			val tabfile = "src/test/resources/pericles-short-tab.txt"
			val collectionurn = "urn:cite2:fufolio:short.v1:"
			val st = SyntaxTokenizer("filesystem",tabfile,collectionurn)
			val sw = SyntaxWriter(st)
			sw.writeCollectionInventory
	}

	it should "be able to write an ORCA collection" in {
 			val tabfile = "src/test/resources/pericles-short-tab.txt"
			val collectionurn = "urn:cite2:fufolio:short.v1:"
			val st = SyntaxTokenizer("filesystem",tabfile,collectionurn)
			val sw = SyntaxWriter(st)
			sw.writeOrcaFile
	}

	it should "be able to write a collection-inventory fragment for the ORCA collection" in {
 			val tabfile = "src/test/resources/pericles-short-tab.txt"
			val collectionurn = "urn:cite2:fufolio:short.v1:"
			val st = SyntaxTokenizer("filesystem",tabfile,collectionurn)
			val sw = SyntaxWriter(st)
			sw.writeOrcaCollectionInventory
	}

	it should "be able to write a 2-column CTS Analytical Exemplar" in {
 			val tabfile = "src/test/resources/pericles-short-tab.txt"
			val collectionurn = "urn:cite2:fufolio:short.v1:"
			val st = SyntaxTokenizer("filesystem",tabfile,collectionurn)
			val sw = SyntaxWriter(st)
			sw.writeCts2Col
	}

	it should "be able to write a TextInventory and CitationConfig fragment for the Analytical exemplar" in {
 			val tabfile = "src/test/resources/pericles-short-tab.txt"
			val collectionurn = "urn:cite2:fufolio:short.v1:"
			val st = SyntaxTokenizer("filesystem",tabfile,collectionurn)
			val sw = SyntaxWriter(st)
			sw.writeCtsInventory
	}

	it should "be able to write Index Inventory fragments" in {
 			val tabfile = "src/test/resources/pericles-short-tab.txt"
			val collectionurn = "urn:cite2:fufolio:short.v1:"
			val st = SyntaxTokenizer("filesystem",tabfile,collectionurn)
			val sw = SyntaxWriter(st)
			sw.writeIndexInventory
	}

	it should "be able to index files for exemplifies/exemplifiedBy, analyzes/analyzedBy, hasAnalysis/isAnalysisFor, and hasSubref/isSubrefOf" in {

 			val tabfile = "src/test/resources/pericles-short-tab.txt"
			val collectionurn = "urn:cite2:fufolio:short.v1:"
			val st = SyntaxTokenizer("filesystem",tabfile,collectionurn)
			val sw = SyntaxWriter(st)
			sw.writeIndicesToFile

	}

	it should "assemble a complete build in a uniquely named directory" in {
 			val tabfile = "src/test/resources/Allen-Iliad.txt"
			val collectionurn = "urn:cite2:fufolio:AllenIliad_SyntaxTokens.v1:"
			val st = SyntaxTokenizer("filesystem",tabfile,collectionurn)
			val sw = SyntaxWriter(st)
			sw.writeAllData
		}

		it should "assemble a complete build in of a long prose text with markup" in {
	 			val tabfile = "src/test/resources/pericles-tab.txt"
				val collectionurn = "urn:cite2:fufolio:PlutPericles_SyntaxTokens.v1:"
				val st = SyntaxTokenizer("filesystem",tabfile,collectionurn)
				val sw = SyntaxWriter(st)
				sw.writeAllData
			}

	it should "be able to build a really large file, like Herodotus" in {
 			val tabfile = "src/test/resources/herodotus-tab.txt"
			val collectionurn = "urn:cite2:fufolio:Hdt_SyntaxTokens.v1:"
			val st = SyntaxTokenizer("filesystem",tabfile,collectionurn)
			val sw = SyntaxWriter(st)
			sw.writeAllData
		}
}
