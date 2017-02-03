package edu.furman.folio.citablegraph
package util

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import java.io._
import scala.io.Source
import scala.collection.mutable.ArrayBuffer

case class SyntaxWriter(st: SyntaxTokenizer, outputDir: String = "target/tokenizations") {
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


	// all-purpose method for outputting an array of Strings to the filesystem
    def writeArrayToFile(a: Array[String], f: File) {
      val pw = new PrintWriter(f)
      for (l <- a) { pw.write(l); pw.write("\n") }
      pw.close
    }

	// all-purpose method for outputting an array of Strings to the screen
    def writeArrayViaHttp(a: Array[String]) {
			throw new Exception("Writing files via HTTP not implemented yet.")
    }

	// Method for directing output
	def directOutput(a: Array[String], fn: String){
		writeArrayViaHttp(a)
	}

	// Method for directing output
	def directOutput(a: Array[String], f: File){
		writeArrayToFile(a, f)
	}

	// Do all builds at once!
	def writeAllData = {

		writeOrcaFile
		writeCts2Col
		writeCollection
		writeIndicesToFile
		writeCollectionInventory
		writeOrcaCollectionInventory
		writeCtsInventory
		writeIndexInventory

	}

	def writeOrcaFile = {
		val ourn = getOrcaUrn
		val v = st.makeOrcaCollection(ourn)
		val directory = createDirectory
		val f = new File(s"${directory}/${getOrcaFilename}")
		writeArrayToFile(v, f)
	}

	def writeCts2Col = {
		val v = st.makeCts2ColText
		val directory = createDirectory
		val f = new File(s"${directory}/${getCTSFilename}")
		writeArrayToFile(v, f)
	}

	// writes collectionArray to a file
	def writeCollection = {
		val directory = createDirectory
		val f = new File(s"${directory}/${getCollectionFilename}")
		var tempArray =  scala.collection.mutable.ArrayBuffer[String]()
		tempArray += "Urn\tAnalyzedText\tTokenType\tTextDeformation\tEditorialStatus\tDiscourseLevel\tExemplarUrn\tSequence"
		for (i <- 0 until st.collectionArray.size){
			tempArray += s"${st.collectionArray(i)}\t${i}"
		}
		writeArrayToFile(tempArray.toArray, f)
	}

		// write indices to files
		def writeIndicesToFile = {
			val ourn = getOrcaUrn
			val oc = st.makeOrcaCollection(ourn, false)
			val directory = createDirectory

			val idxExemplifies = oc.map( l => ( s"${l.split("\t")(2)}\t${l.split("\t")(1)}" ))
			val f1 = new File(s"${directory}/${getIndexFilenames("exempIndex")}")
			writeArrayToFile(idxExemplifies, f1)

			val idxAnalyzes = oc.map( l => ( s"${l.split("\t")(0)}\t${l.split("\t")(1)}" ))
			val f2 = new File(s"${directory}/${getIndexFilenames("analyzesIndex")}")
			writeArrayToFile(idxAnalyzes, f2)

			val idxAnalysis = oc.map( l => ( s"${l.split("\t")(0)}\t${l.split("\t")(3)}" ))
			val f3 = new File(s"${directory}/${getIndexFilenames("analysisIndex")}")
			writeArrayToFile(idxAnalysis, f3)

			val idxSubref = oc.map{ l =>
				val noSubRef = CtsUrn(l.split("\t")(1)).dropSubref.toString
				s"${noSubRef}\t${l.split("\t")(1)}"
			}
			val f4 = new File(s"${directory}/${getIndexFilenames("subrefIndex")}")
			writeArrayToFile(idxSubref, f4)

		}

	def writeCollectionInventory = {
			val x = s"""
<!-- Syntax tokens from ${getCtsUrn} -->
<citeCollection canonicalId="Urn" label="TextDeformation" urn="${getCollectionUrn}">
  <namespaceMapping abbr="***ADD NAMESPACE ABBR***" uri="***ADD NS URI***"/>

  <dc:title xmlns="http://purl.org/dc/elements/1.1/">Syntax tokenization of ${getCtsUrn}</dc:title>
  <description xmlns="http://purl.org/dc/elements/1.1/">Syntax tokenization with categorized tokens, analyzed from ${getCtsUrn}</description>
  <rights xmlns="http://purl.org/dc/elements/1.1/"> All data in this collection are available
      under the terms of the Creative Commons Attribution-Non-Commercial 3.0 Unported License,
      http://creativecommons.org/licenses/by-nc/3.0/deed.en_US</rights>

  <orderedBy property="Sequence"/>
  <source type="file" value="${getCollectionFilename}"/>

  <citeProperty name="Urn" label="The URN for this token" type="cite2urn"></citeProperty>
  <citeProperty name="AnalyzedText" label="CTS Urn with string alignment" type="ctsurn"></citeProperty>
  <citeProperty name="TokenType" label="Token type" type="cite2urn"></citeProperty>
  <citeProperty name="TextDeformation" label="Presentational String" type="string"></citeProperty>
  <citeProperty name="EditorialStatus" label="Editorial Status" type="cite2urn"/>
  <citeProperty name="DiscourseLevel" label="Discourse Level" type="cite2urn"/>
  <citeProperty name="Sequence" label="Sequence" type="number"></citeProperty>
  <citeProperty name="ExemplarUrn" label="CTS Urn for derived analytical exemplar" type="ctsurn"></citeProperty>

</citeCollection>
			"""
			val v = Array(x)
			val directory = createDirectory
			val f = new File(s"${directory}/FOR-COLLECTION-INVENTORY_Tokens.xml")
			writeArrayToFile(v, f)

	}

	def writeOrcaCollectionInventory = {
			val x = s"""
<!-- ORCA alignment of Syntax tokens (${getCollectionUrn}) with ${getCtsUrn} -->
<citeCollection canonicalId="ORCA_URN" label="TextDeformation" urn="${getOrcaUrn}">
		<namespaceMapping abbr="***ADD NAMESPACE ABBR***" uri="***ADD NS URI***"/>
		<extendedBy extension="cite:ORCA"/>

		<dc:title xmlns="http://purl.org/dc/elements/1.1/">ORCA Alignment of  ${getCollectionUrn} with ${getCtsUrn} </dc:title>

		<description xmlns="http://purl.org/dc/elements/1.1/">ORCA Alignment of Syntax tokens ${getCollectionUrn} with ${getCtsUrn}</description>
		<rights xmlns="http://purl.org/dc/elements/1.1/"> All data in this collection are available
				under the terms of the Creative Commons Attribution-Non-Commercial 3.0 Unported License,
				http://creativecommons.org/licenses/by-nc/3.0/deed.en_US</rights>

		<orderedBy property="Sequence"/>
		<source type="file" value="${getOrcaFilename}"/>

		<citeProperty name="ORCA_URN" label="The URN for this token" type="cite2urn"></citeProperty>
		<citeProperty name="AnalyzedText" label="CTS Urn with string alignment" type="ctsurn"></citeProperty>
		<citeProperty name="Analysis" label="Token Type" type="cite2urn"></citeProperty>
		<citeProperty name="TextDeformation" label="Presentational String" type="string"></citeProperty>
		<citeProperty name="Sequence" label="Sequence" type="number"></citeProperty>
		<citeProperty name="ExemplarUrn" label="CTS Urn for derived analytical exemplar" type="ctsurn"></citeProperty>

</citeCollection>
			"""
			val v = Array(x)
			val directory = createDirectory
			val f = new File(s"${directory}/FOR-COLLECTION-INVENTORY_orca.xml")
			writeArrayToFile(v, f)

	}

	def writeCtsInventory = {
			val x = s"""
<inventoryFragments>

	<! -- For TextInventory: insert in Edition element for '${getCtsUrn}' -->
	<exemplar urn="${getCtsExemplarUrn}">
		 <label xml:lang="eng">Syntax Tokens</label>
		 <description xml:lang="eng">Syntactically Significant Tokens</description>
		 <online/>
	</exemplar>

	<! -- For CitationConfig -->
	<online urn="${getCtsExemplarUrn}" type="2col" docname="${getCTSFilename}" nodeformat="text">
     <citationScheme>
         <!-- You need to add this yourself! -->
     </citationScheme>
	</online>


</inventoryFragments>
			"""
			val v = Array(x)
			val directory = createDirectory
			val f = new File(s"${directory}/FOR-CTS-INVENTORY-AND-CITATION-CONFIG.xml")
			writeArrayToFile(v, f)

	}


	def writeIndexInventory = {
		  val nameMap = getIndexFilenames
			val x = s"""
<inventoryFragments>

	<! -- For Index Inventory -->
	<index verb="orca:exemplifies" inverse="orca:exemplifiedBy">
        <source type="file" value="${nameMap("exempIndex")}"></source>
  </index>
	<index verb="orca:analyzes" inverse="orca:analyzedBy">
        <source type="file" value="${nameMap("analyzesIndex")}"></source>
  </index>
	<index verb="orca:hasAnalysis" inverse="orca:analysisFor">
        <source type="file" value="${nameMap("analysisIndex")}"></source>
  </index>
	<index verb="cts:hasSubref" inverse="cts:isSubrefOf">
        <source type="file" value="${nameMap("subrefIndex")}"></source>
  </index>

</inventoryFragments>
			"""
			val v = Array(x)
			val directory = createDirectory
			val f = new File(s"${directory}/FOR-INDEX INVENTORY.xml")
			writeArrayToFile(v, f)

	}


	/* PRIVATE METHODS */

	private def getOrcaFilename = {
		val fn = s"ORCA-${getBaseFileName}.txt"
		fn
	}

	private def getCollectionFilename = {
		val fn = s"TokenCollection-${getBaseFileName}.tsv"
		fn
	}

	private def getCTSFilename = {
		val fn = s"CTS2Col-SynTokExemplar-${getBaseFileName}.txt"
		fn
	}

	private def getIndexFilenames = {
		val fnn = Map(
				"exempIndex" -> s"INDEX-exemplifies-SynTok-${getBaseFileName}.tsv",
				"analyzesIndex" -> s"INDEX-analyzes-SynTok-${getBaseFileName}.tsv",
				"analysisIndex" -> s"INDEX-hasAnalysis-SynTok-${getBaseFileName}.tsv",
				"subrefIndex" -> s"INDEX-hasSubref-SynTok-${getBaseFileName}.tsv"
		)
		fnn
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
