import edu.furman.folio.citablegraph._
import edu.furman.folio.citablegraph.util._
import edu.holycross.shot.cite._
import scala.io.Source
import java.io._



/* Generate Graph,Edge, and Vertex collection data */

def graphIliad16a = {
	val urnMap = Map(
		"1" -> "urn:cite2:fufolio:hmtGraph.v1:Iliad16a"
	)

	val tbPath = "src/test/resources/Treebanks/Iliad16-A.xml"
	val outDir = "target/graphs/Iliad16a"
	val tbgw = TreebankGraphWriter(urnMap, tbPath, outDir)
	tbgw.saveEdgeCollection(true)
	tbgw.saveVertexCollection(true)
	tbgw.saveGraphCollection
}

def graphIliad16b = {
	val urnMap = Map(
		"1" -> "urn:cite2:fufolio:hmtGraph.v1:Iliad16b"
	)

	val tbPath = "src/test/resources/Treebanks/Iliad16-B.xml"
	val outDir = "target/graphs/Iliad16b"
	val tbgw = TreebankGraphWriter(urnMap, tbPath, outDir)
	tbgw.saveEdgeCollection(false)
	tbgw.saveVertexCollection(false)
	tbgw.saveGraphCollection
}

def graphIliad24Aristarchus = {
	val urnMap = Map(
		"1" -> "urn:cite2:fufolio:hmtGraph.v1:IliadAristarchus"
	)

	val tbPath = "src/test/resources/Treebanks/Iliad_24.3-24.11_Aristarchus.xml"
	val outDir = "target/graphs/Iliad24Aristarchus"
	val tbgw = TreebankGraphWriter(urnMap, tbPath, outDir)
	tbgw.saveEdgeCollection(false)
	tbgw.saveVertexCollection(false)
	tbgw.saveGraphCollection
}

def graphIliad24Nicanor = {
	val urnMap = Map(
		"1" -> "urn:cite2:fufolio:hmtGraph.v1:Iliad24Nicanor"
	)

	val tbPath = "src/test/resources/Treebanks/Iliad_24.3-24.11_Nicanor.xml"
	val outDir = "target/graphs/Iliad24Nicanor"
	val tbgw = TreebankGraphWriter(urnMap, tbPath, outDir)
	tbgw.saveEdgeCollection(false)
	tbgw.saveVertexCollection(false)
	tbgw.saveGraphCollection
}

def graphIliad24Default = {
	val urnMap = Map(
		"1" -> "urn:cite2:fufolio:hmtGraph.v1:Iliad24Default"
	)

	val tbPath = "src/test/resources/Treebanks/Iliad_24.3-24.11_Not_Nicanor.xml"
	val outDir = "target/graphs/Iliad24Default"
	val tbgw = TreebankGraphWriter(urnMap, tbPath, outDir)
	tbgw.saveEdgeCollection(false)
	tbgw.saveVertexCollection(false)
	tbgw.saveGraphCollection
}



graphIliad16a
graphIliad16b
graphIliad24Aristarchus
graphIliad24Nicanor
graphIliad24Default
