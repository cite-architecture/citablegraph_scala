package edu.furman.folio.citablegraph
package util

// Use these for constructing unique URNs
import java.text.SimpleDateFormat
import java.util.Calendar

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import java.io._
import scala.io.Source
import scala.xml.XML
import scala.collection.mutable.ArrayBuffer

case class TreebankGraphWriter(urnStringMap: Map[String,String], tbFile: String, outputDir: String = "target/graphs") {
	val outputDirectory = outputDir

	// use real URN values
  val urnMap = urnStringMap.mapValues( new Cite2Urn(_))

	val exists = true

	val tbXml = XML.loadFile(tbFile)

	var graphList =  scala.collection.mutable.ArrayBuffer[String]()
	var vertexList =  scala.collection.mutable.ArrayBuffer[String]()
	var edgeList =  scala.collection.mutable.ArrayBuffer[String]()

	val posUrns = Source.fromFile("src/test/resources/partsOfSpeech.tsv").getLines.toVector.map(_.split("\t"))
	val relationUrns = Source.fromFile("src/test/resources/syntaxRelations.tsv").getLines.toVector.map(_.split("\t"))


	// If the urn map does not match the number of sentences, throw an error
	if ( (tbXml \\ "sentence").size != urnMap.size ){
		throw new Exception(s"TreebankGraphWriter: The number of items in the URN map (${urnMap.size}) must equal the number of sentences in the XML file (${(tbXml \\ "sentence").size})")
	}


	for ( s <- tbXml \\ "sentence" ){
		val thisId = (s \ "@id").toString
		val thisGraphUrn = urnMap(thisId)
		graphList += thisGraphUrn.toString

	  for ( w <- s \ "word" ){
			vertexList += makeVertexObject(w, thisGraphUrn, thisId)
			edgeList += (makeEdgeObject(s, w, thisGraphUrn, thisId))
		}

	}


def saveVertexCollection(includeHeader: Boolean = false) = {
		val directory = createDirectory
		val f = new File(s"${directory}/${getVerticesFilename}")
    val pw = new PrintWriter(f)
//     vertexUrn \t orcaUrn \t graphUrn \t sequence \t posUrn \t label

    if (includeHeader) pw.print("vertexUrn\torcaUrn\tgraphUrn\tsequence\tposUrn\tlabel\n")
		for (l <- vertexList if l.size > 0) { pw.append(l); pw.append("\n") }
		pw.close()
}

def saveEdgeCollection(includeHeader: Boolean = false) = {
		val directory = createDirectory
		val f = new File(s"${directory}/${getEdgesFilename}")
    val pw = new PrintWriter(f)
    if (includeHeader) pw.print("edgeUrn\tgraphUrn\tsourceUrn\trelationUrn\ttargetUrn\tlabel\n")
		for (l <- edgeList if l.size > 0) { pw.append(l); pw.append("\n") }
		pw.close()
}

def saveGraphCollection = {
		val directory = createDirectory
		val f = new File(s"${directory}/${getGraphsFilename}")
    val pw = new PrintWriter(f)
		for (l <- graphList) { pw.append(l); pw.append("\n") }
		pw.close()
}


def getEdgesFilename: String = {
		val testDateFormat1 = new SimpleDateFormat("yyyyMMddhhmmss")
		val today = Calendar.getInstance.getTime
		val uniqueString = testDateFormat1.format(today)
		val fn = s"edges${uniqueString}.tsv"
		fn
}

def getVerticesFilename: String = {
		val testDateFormat1 = new SimpleDateFormat("yyyyMMddhhmmss")
		val today = Calendar.getInstance.getTime
		val uniqueString = testDateFormat1.format(today)
		val fn = s"vertices${uniqueString}.tsv"
		fn
}

def getGraphsFilename: String = {
		val testDateFormat1 = new SimpleDateFormat("yyyyMMddhhmmss")
		val today = Calendar.getInstance.getTime
		val uniqueString = testDateFormat1.format(today)
		val fn = s"graphs${uniqueString}.txt"
		fn
}


// Make edge collection-object
//		edgeUrn \t graphUrn \t sourceUrn \t relationUrn \t targetUrn \t label
def makeEdgeObject(sentenceEntry: scala.xml.Node, wordEntry: scala.xml.Node, graphUrn: Cite2Urn, sentenceId: String): String = {
	val graphUrnString = graphUrn.toString

	// get target URN
	val targetUrn = (wordEntry \ "@orcaUrn" ).toString

	// get source URN, which is the @head, identified by ID, so we have to look for it
	val sourceId = (wordEntry \ "@head" ).toString
	var sourceUrn = ""
	if (sourceId == "0"){
		sourceUrn = graphUrn.toString
	} else {
		sourceUrn = (getXmlElementByAttribute(sentenceEntry,"id",sourceId) \ "@orcaUrn").toString
	}

	// Find URN for relation
	val relationLabel = (wordEntry \ "@relation" ).toString
	val relationUrn = findRelationUrn(relationLabel)

	// Make Edge URN
	val seq = (wordEntry \ "@id" ).toString
	val edgeUrn = makeEdgeUrn(seq, sentenceId)

	// assemble
	val edgeObjectString = s"${edgeUrn}\t${graphUrn}\t${sourceUrn}\t${relationUrn}\t${targetUrn}\t${relationLabel}"

	var allThere = true
	for (t <- edgeObjectString.split("\t")){
		if (t.size < 1) allThere = false
	}

	if (allThere){
		edgeObjectString
	} else {
		val notAllThere = ""
		notAllThere
	}

}

// Make vertex collection-object
//     vertexUrn \t orcaUrn \t graphUrn \t sequence \t posUrn \t label
def makeVertexObject(wordEntry: scala.xml.Node, graphUrn: Cite2Urn, sentenceId: String): String  = {

	var allThere = true

	val seq = (wordEntry \ "@id" ).toString
	if (seq == "") allThere = false;

	val graphUrnString = graphUrn.toString
	val orcaUrn = (wordEntry \ "@orcaUrn" ).toString
	if (orcaUrn == "") allThere = false;

	val posUrn = findPosUrn( (wordEntry \ "@postag" ).toString )
	if (posUrn == "") allThere = false;

	val label = (wordEntry \ "@form").toString
	if (label == "") allThere = false;

	val vertexUrn = makeVertexUrn(seq, sentenceId)
	if (vertexUrn == "") allThere = false;
	if (allThere){
		val vertexObjectEntry = s"${vertexUrn}\t${orcaUrn}\t${graphUrn}\t${seq}\t${posUrn}\t${label}"
		vertexObjectEntry
	} else {
		val vertexObjectEntry = ""
		vertexObjectEntry
	}


}

// Make unique vertex URN
def makeVertexUrn(wordId: String, sentenceId: String): String = {
		val testDateFormat1 = new SimpleDateFormat("yyyyMMddhhmmssSS")
		val today = Calendar.getInstance.getTime
		val uniqueString = testDateFormat1.format(today)
		val objectId = s"${uniqueString}_${sentenceId}_${wordId}"
		val urnPrefix = "urn:cite2:fufolio:vertices.v1:"
		val realUrn = Cite2Urn(s"${urnPrefix}${objectId}")
		realUrn.toString
}

// Make unique vertex URN
def makeEdgeUrn(wordId: String, sentenceId: String): String = {
		val testDateFormat1 = new SimpleDateFormat("yyyyMMddhhmmssSS")
		val today = Calendar.getInstance.getTime
		val uniqueString = testDateFormat1.format(today)
		val objectId = s"${uniqueString}_${sentenceId}_${wordId}"
		val urnPrefix = "urn:cite2:fufolio:edges.v1:"
		val realUrn = Cite2Urn(s"${urnPrefix}${objectId}")
		realUrn.toString
}

def findPosUrn(posString: String): String = {
	val posUrnMatch = posUrns.filter(_(1) == posString)
	var posUrnStr: String = ""
	if (posUrnMatch.size > 0){
		posUrnStr = posUrnMatch(0)(0)
		try {
				val posUrn = Cite2Urn(posUrnStr)
			} catch {
				case _ : Exception =>
					throw new Exception("TreebankGraphWriter: Could not find part of speech URN matching '${posString}'")
			}
	}
	posUrnStr

}

def findRelationUrn(relationLabel: String): String = {
	val relUrnMatch = relationUrns.filter(_(1) == relationLabel)
	var relUrnStr = ""
	if (relUrnMatch.size > 0){
		relUrnStr = relUrnMatch(0)(0)
		try {
				val relUrn = Cite2Urn(relUrnStr)
			} catch {
				case _ : Exception =>
					throw new Exception("TreebankGraphWriter: Could not find relation URN matching '${relationLabel}'")
			}
		}
	relUrnStr
}



/*
	def writeTreebank = {
		val v = generateXml
		val directory = createDirectory
		val f = new File(s"${directory}/${getSyntaxFilename}")
    val pw = new PrintWriter(f)
    pw.write(v)
    pw.close
	}
	*/

private def getXmlElementByAttribute(e: scala.xml.Node, att: String, value: String) = {
    def filterAtribute(node: scala.xml.Node, att: String, value: String) =  (node \ ("@" + att)).text == value
    e \\ "_" filter { n=> filterAtribute(n, att, value)}
}


	/* Private Functions */

	private def createDirectory: String = {
		val s = "graphFiles"
		val p = s"${outputDirectory}"
		val dir = checkDirectory(p)
		p
	}

	private def checkDirectory(d: String): java.io.File = {
		val dir: File = new File(d)
		if ( dir.exists() != true ){
			dir.mkdirs()
			dir
		} else {
			dir
		}
	}


}
