import edu.furman.folio.citablegraph._
import edu.holycross.shot.cite._
import scala.io.Source
import java.io._

/* Generate a Treebank XML file from a series of range URNs */


// CTS Text in tabular format:
val f = "/Users/cblackwell/Dropbox/CITE/scala/citableGraph/src/test/resources/pericles-tab.txt"

// URN for the resulting collection of syntactical tokens
val cus = "urn:cite2:fufolio:syntaxToken.v1:0007_0012_"

// Where the output will be directed.
val outDir = "Syntax_Files"

// Arethusa login information
val username= "Christopher Blackwell"
val email = "cwblackwell@gmail.com"
val perseidsId = "http://data.perseus.org/sosol/users/Christopher%20Blackwell"
val st = edu.furman.folio.citablegraph.util.SyntaxTokenizer("filesystem",f,cus)

// CTS URNs. Each range is a sentence.
val ua = Array(
	CtsUrn("urn:cts:greekLit:tlg0007.tlg012.ziegler.syntaxToken:1.1.1-1.1.49"),
	CtsUrn("urn:cts:greekLit:tlg0007.tlg012.ziegler.syntaxToken:1.2.1-1.2.41"),
	CtsUrn("urn:cts:greekLit:tlg0007.tlg012.ziegler.syntaxToken:1.2.42-1.2.108"),
	CtsUrn("urn:cts:greekLit:tlg0007.tlg012.ziegler.syntaxToken:1.3.1-1.3.34"),
	CtsUrn("urn:cts:greekLit:tlg0007.tlg012.ziegler.syntaxToken:1.4.1-1.4.22"),
	CtsUrn("urn:cts:greekLit:tlg0007.tlg012.ziegler.syntaxToken:1.4.23-1.4.70")
)

val tb = edu.furman.folio.citablegraph.util.Treebanker(st, username, email, perseidsId, ua, outDir )

println(s"\nWill write Treebank XML file to: ${outDir}")

tb.writeTreebank

println("Done.")
