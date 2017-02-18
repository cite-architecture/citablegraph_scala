import edu.furman.folio.citablegraph._
import edu.holycross.shot.cite._
import scala.io.Source
import java.io._

/* Generate a Treebank XML file from a series of range URNs */


// CTS Text in tabular format:
val f = "/Users/cblackwell/Dropbox/CITE/scala/citableGraph/src/test/resources/Allen-Iliad.txt"

// URN for the resulting collection of syntactical tokens
val cus = "urn:cite2:fufolio:syntaxToken.v1:0012_001_pers_"

// Where the output will be directed.
val outDir = "Syntax_Files"

// Arethusa login information
val username= "Christopher Blackwell"
val email = "cwblackwell@gmail.com"
val perseidsId = "http://data.perseus.org/sosol/users/Christopher%20Blackwell"
val st = edu.furman.folio.citablegraph.util.SyntaxTokenizer("filesystem",f,cus)

// CTS URNs. Each range is a sentence.
val ua = Array(
	CtsUrn("urn:cts:greekLit:tlg0012.tlg001.fuPers.syntaxToken:16.83.1-16.87.6"),
	CtsUrn("urn:cts:greekLit:tlg0012.tlg001.fuPers.syntaxToken:24.3.6-24.11.6")
)

val tb = edu.furman.folio.citablegraph.util.Treebanker(st, username, email, perseidsId, ua, outDir )

println(s"\nWill write Treebank XML file to: ${outDir}")

tb.writeTreebank

println("Done.")
