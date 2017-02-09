package edu.furman.folio.citablegraph
package util

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import java.io._
import scala.io.Source
import scala.collection.mutable.ArrayBuffer

case class Treebanker(st: SyntaxTokenizer, username: String, email: String, perseidsId: String, urns: Array[CtsUrn], outputDir: String = "target/tokenizations") {
	val outputDirectory = outputDir

	val exists = true

	// for accessing records in a collectionArray line
	private val COLLURN = 0
	private val ANALYZEDTEXT = 1
	private val TOKENTYPE = 2
	private val DEFORMATION = 3
	private val EDSTATUS = 4
	private val DISCOURSE = 5
	private val EXEMPLARURN = 6

	def writeTreebank = {
		val v = generateXml
		val directory = createDirectory
		val f = new File(s"${directory}/${getSyntaxFilename}")
    val pw = new PrintWriter(f)
    pw.write(v)
    pw.close
	}

	def generateXml:String = {
			var sentences = ""
			for (i <- 1 to urns.size){
				sentences += getSentence(urns(i-1), i)
			}
			val treebankXml = xmlFramework(username, email, perseidsId, sentences)
			treebankXml
	}


	def getSentence(urn: CtsUrn, id: Integer): String = {
		val vstring = st.collectionArray.map( l => l ).toVector
		val sentenceLines = sliceRange(urn, vstring)
		var xmlString = s"""<sentence id="${id}" document_id="" subdoc="" span="${urn}">\n"""
		for (i <-  0 until sentenceLines.size ){
				val wForm = sentenceLines(i).split("\t")(DEFORMATION)
				val orcaUrn = sentenceLines(i).split("\t")(COLLURN)
				val ctsUrn = sentenceLines(i).split("\t")(EXEMPLARURN)
				xmlString += s"""<word id="${i+1}" form="${wForm}" lemma="" postag="" relation="" head="" orcaUrn="${orcaUrn}"  />\n"""
				//xmlString += s"""<word id="${i+1}" form="${wForm}" lemma="" postag="" relation="" head=""  />\n"""
		}
		xmlString += "</sentence>\n"
		xmlString
	}

	def rangeBeginIndex(urn: CtsUrn, lines: Vector[String]): Integer = {
    def trimmed = urn.dropPassage
    val bgnRef = trimmed.toString + urn.rangeBeginParts(0)
    val psgUrn = CtsUrn(bgnRef)
		val justUrns = lines.map( l => l.split("\t")(EXEMPLARURN))
		val canonical = justUrns.filter( _ == psgUrn.toString)
		if (canonical.size != 1){
			throw CGException(s"${psgUrn} not found.")
		} else {
	    var beginIndex = justUrns.indexOf(canonical(0))
			beginIndex
		}
	}

	def rangeEndIndex(urn: CtsUrn, lines: Vector[String]): Integer = {
    def trimmed = urn.dropPassage
    val bgnRef = trimmed.toString + urn.rangeEndParts(0)
    val psgUrn = CtsUrn(bgnRef)
		val justUrns = lines.map( l => l.split("\t")(EXEMPLARURN))
		val canonical = justUrns.filter( _ == psgUrn.toString)
		if (canonical.size != 1){
			throw CGException(s"${psgUrn} not found.")
		} else {
	    var endIndex = justUrns.indexOf(canonical(0))
			endIndex
		}
	}

def sliceRange(u: CtsUrn, reff: Vector[String]) = {
    val i1 = rangeBeginIndex(u, reff)
    val i2 = rangeEndIndex(u, reff) + 1
    reff.slice(i1,i2)
  }

	/* PRIVATE METHODS */

	private def xmlFramework(userName: String, email: String, perseidsId: String, xmlBody: String) = {
		val xmlString = s"""
		<treebank xmlns:saxon='http://saxon.sf.net/' xml:lang='grc' format='aldt' direction='ltr' version='1.5'>
		<date></date>
		<annotator>
		<short>arethusa</short>
		<name>arethusa</name>
		<address/>
		<uri>http://github.com/latin-language-toolkit/arethusa</uri>
		</annotator>
		<annotator>
		<short>${userName}</short>
		<name>${userName}</name>
		<address>${email}</address>
		<uri>${perseidsId}</uri>
		</annotator>
		${xmlBody}
		</treebank>
	"""

	xmlString


	}

	private def getTreebankkFilename = {
		val fn = s"Treebankk-${getBaseFileName}.xml"
		fn
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

	private def createDirectory: String = {
		val s = getBaseFileName
		val p = s"${outputDirectory}/${s}"
		val dir = checkDirectory(p)
		p
	}

	private def getBaseFileName: String = {
		  val us = st.collectionArray(0).split("\t")(ANALYZEDTEXT)
			val urn = CtsUrn(us).dropPassage
			val basefilename = urn.toString.replaceAll("urn:cts:","").replaceAll(":","_").replaceAll("\\.","-").dropRight(1)
			basefilename
	}

private def getSyntaxFilename = {
		val fn = s"Syntax-${getBaseFileName}.xml"
		fn
	}

	private def getCtsUrn: String = {
		  val us = st.collectionArray(0).split("\t")(ANALYZEDTEXT)
			val urn = CtsUrn(us).dropPassage
			val urnString = urn.toString
			urnString
	}


	private def getCtsExemplarUrn: String = {
		  val us = st.collectionArray(0).split("\t")(EXEMPLARURN)
			val urn = CtsUrn(us).dropPassage
			val urnString = urn.toString
			urnString
	}


	private def getOrcaUrn: String = {
		  val us = st.collectionArray(0).split("\t")(COLLURN)
			val urn = Cite2Urn(us)
			val orcaurn = s"urn:cite2:${urn.namespace}:${urn.collection}_ORCA.${urn.version}:"

		  orcaurn
	}

	private def getCollectionUrn: String = {
		  val us = st.collectionArray(0).split("\t")(COLLURN)
			val urn = Cite2Urn(us)
			val collurn = s"urn:cite2:${urn.namespace}:${urn.collection}.${urn.version}:"

		  collurn
	}



}
