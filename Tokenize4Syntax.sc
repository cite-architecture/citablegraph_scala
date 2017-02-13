import edu.furman.folio.citablegraph._
import edu.holycross.shot.cite._
import scala.io.Source
import java.io._

/* Generate a Treebank XML file from a series of range URNs */


// CTS Text in tabular format:
val f = "/Users/cblackwell/Dropbox/CITE/scala/citableGraph/src/test/resources/pericles-tab.txt"

// Where the output will be directed.
val outDir = "Tokenizations"

// Array of paths to CTS texts in tabular format
val tabs = Array( "src/test/resources/herodotus-tab.txt", "src/test/resources/Allen-Iliad.txt", "src/test/resources/pericles-tab.txt" )

println(s"\nWill write CTS, CITE Collection, and CITE Index files to: ${outDir}")

for ( t <- tabs) {
		println(s"Tokenizing: ${t}")
		val st = edu.furman.folio.citablegraph.util.SyntaxTokenizer("filesystem",t)
		val sw = edu.furman.folio.citablegraph.util.SyntaxWriter(st,outDir)
		sw.writeAllData
	}
)


println("Done.")
