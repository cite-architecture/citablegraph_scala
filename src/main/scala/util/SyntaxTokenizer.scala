package edu.furman.folio.citablegraph
package util

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import java.io._
import scala.io.Source
import scala.collection.mutable.ArrayBuffer

case class SyntaxTokenizer(collectionArray: Array[String]) {

	// for accessing records in a collectionArray line
	private val COLLURN = 0
	private val ANALYZEDTEXT = 1
	private val TOKENTYPE = 2
	private val DEFORMATION = 3
	private val EDSTATUS = 4
	private val DISCOURSE = 5
	private val EXEMPLARURN = 6

	// Outputs a four-column ORCA alignment file, for use with the CITE 'orca' package
  //	ORCA_URN \t AnalyzedText \t Analysis \t textDeformation
	def makeOrcaCollection(orcaUrn: String, includeHeader: Boolean = true): Array[String] = {
		//var tempArray =  scala.collection.mutable.ArrayBuffer[String]()
		val v1 = collectionArray.map( l => ( l.split("\t")(ANALYZEDTEXT), l.split("\t")(EXEMPLARURN),l.split("\t")(TOKENTYPE), l.split("\t")(DEFORMATION)))
		val v2 = v1.zipWithIndex.map(l => ( (l._1._1), (l._1._2), (l._1._3), (l._1._4), (s"${orcaUrn}${l._2}"), (s"${l._2}" ) ))
		val v3 = v2.map(l => s"${l._5}\t${l._1}\t${l._2}\t${l._3}\t${l._4}\t${l._6}" )
		if ( includeHeader == true){
		val vHead = Array("ORCA_URN\tAnalyzedText\tExemplarUrn\tAnalysis\tTextDeformation\tSequence")
			val v4 = vHead ++ v3
			v4
		} else {
			v3
		}
	}

	// Make array of strings, with a '#' delimiter: urn#text
	def makeCts2ColText: Array[String] = {
		val v1 = collectionArray.map( l => (s"""${l.split("\t")(EXEMPLARURN)}#${l.split("\t")(DEFORMATION)}"""))
		v1
	}

	/* PRIVATE METHODS */

	//split a collectionArray line into an array
	private def splitLine(l: String): Array[String] = {
		val fieldArray = l.split('\t')
		fieldArray
	}


} // END CLASS DEFINITION


// The tedious work of constructing below…

object SyntaxTokenizer {

		private val separator: String = "#"

		// Token types
		private val tokenTypeMap = Map(
				"word" -> "urn:cite2:fufolio:tokenTypes.v1:word",
				"multifunction" -> "urn:cite2:fufolio:tokenTypes.v1:multifunction",
				"punc" -> "urn:cite2:fufolio:tokenTypes.v1:punc",
				"other" -> "urn:cite2:fufolio:tokenTypes.v1:other"
		)
		// Editorial Status
		// 		A map of Tuple3: URN, prefix-text, postfix-text. Allows 'markup' of textDeformations
		private val edStatusMap = Map(
				"default" -> ("urn:cite2:fufolio:editorialStatus.v1:default","",""),
				"add" -> ("urn:cite2:fufolio:editorialStatus.v1:add","<",">"),
				"sic" -> ("urn:cite2:fufolio:editorialStatus.v1:sic","","[sic]"),
				"corr" -> ("urn:cite2:fufolio:editorialStatus.v1:corr","<",">"),
				"abbrev" -> ("urn:cite2:fufolio:editorialStatus.v1:abbrev","","…"),
				"expan" -> ("urn:cite2:fufolio:editorialStatus.v1:expan","(",")"),
				"del" -> ("urn:cite2:fufolio:editorialStatus.v1:del","{","}")
		)
		// Level of Discource
		private val discourseMap = Map(
				"direct" -> "urn:cite2:fufolio:tokdiscourse.v1:direct",
				"q" -> "urn:cite2:fufolio:discourse.v1:internalQuote",
				"quote" -> "urn:cite2:fufolio:discourse.v1:externalQuote"
		)

		private val multifunctionTokens = Map(
			"οὐδέ" -> ("οὐ","δέ"),
			"οὐδὲ" -> ("οὐ","δὲ"),
			"οὐδ'" -> ("οὐ","δ'"),
			"μηδέ" -> ("μή","δέ"),
			"μηδὲ" -> ("μή","δὲ"),
			"μηδ'" -> ("μή","δ'"),
			"εἴτε" -> ("εἴ","τε")
		)

		private val nonCharacters = """{※}⸖<>-—–—()[]"""

		private val delimiters : scala.util.matching.Regex = """([.,;"?·])""".r
		private val punctuation = """.,;\"'?·"""

		def apply(source: String, tabFile: String, collectionUrnString: String): SyntaxTokenizer = {

			//val tabFields = Source.fromFile(tabFile).getLines.toVector.map(_.split(separator))
			val tabFields = acquireTextAndCitations(source,tabFile)
			val stringPairs = for (tf <- tabFields if tf.size == 8) yield Map("urn" -> tf(0), "text" -> tf(5))

			// Set up for looping
			var thisEdUrn: String = ""
			var thisText: String = ""
			var nestedElements: String = ""

			var tempSyntaxArray =  scala.collection.mutable.ArrayBuffer[String]()

			for ( sp <- stringPairs){


				thisEdUrn = sp("urn")
				thisText = sp("text")

				nestedElements = ""

				// Begin the recursive process that (a) checks for XML, and (b) tokenizes
				var basicTuples =  scala.collection.mutable.ArrayBuffer[Tuple2[String, String]]()
				for (ot <- textSwitcher(thisText, nestedElements )) {
					basicTuples += ot
				}

				// Get analyzedTextUrns
				var analyzedTextUrns =  scala.collection.mutable.ArrayBuffer[String]()
				for (i <- 0 until basicTuples.size){
						val truncArray = for (e <- 0 to i) yield basicTuples(e)
						val ft = basicTuples(i)
						//println(s"${ft}")
						val matchers = truncArray.filter( _ match{ case ( ft._1 , _) => true case _ => false })
						val atIndex = matchers.size
						analyzedTextUrns += s"${thisEdUrn}@${ft._1}[${atIndex}]"
						// for (e <- 0 until 2) yield bt(e)
						// bt.filter(_  match{ case ("a",_) => true case _ => false } )
				}

				// Structure: (analyzedTextUrn ( string, element-String ))
				val newTuple3 = analyzedTextUrns.zip(basicTuples)

				//Last pass: analyze line for categories and multi-functionality
				for ( i <- 0 until newTuple3.size){
						for (e <- analyzeText(i, newTuple3(i), collectionUrnString)) tempSyntaxArray += e
				}

			}

			// return (finally!)
		  val collectionArray = tempSyntaxArray.toArray
			SyntaxTokenizer(collectionArray)
		}

	// the map is: text, nestedElements
	def textSwitcher(t:String, ne: String) : Array[Tuple2[String, String]] = {
		var ma =  scala.collection.mutable.ArrayBuffer[Tuple2[String, String]]()
	  try {
				val x = scala.xml.XML.loadString(t)
				for( nn <- processXmlText(x, ne)){
						ma += nn
				}
		} catch {
			case _: org.xml.sax.SAXParseException =>
				for( nn <- processText(t, ne)){
					ma += nn
				}
		}
		ma.toArray
	}


	def analyzeText(i: Int, tt: Tuple2[String,Tuple2[String,String]], collectionUrnString: String): Array[String] = {
		  var returnArray =  scala.collection.mutable.ArrayBuffer[String]()
			var tokenType = ""
			var edStatus = ""
			var discourseLevel = ""
			var greekString = tt._2._1
			var greekStringPrefix = ""
			var greekStringAdfix = ""
			val elems = tt._2._2

			// Text-content cannot have a hyphen. Period.
			if (greekString.contains("-")) throw new Exception(s"Exception: text-content cannot have a hyphen: ${tt}")


			if (elems != ""){

			// Deal with discourse
				for (el <- elems.split(",")){
					val esm = discourseMap.get(el)
					esm match {
						case Some(dismatch) => discourseLevel = discourseMap(el)
						case None =>  discourseLevel = discourseMap("direct")
					}
				}

			// Deal with editorial status
				for (el <- elems.split(",")){
					val esm = edStatusMap.get(el)
					esm match {
						case Some(esmatch) => {
							edStatus = edStatusMap(el)._1
							greekStringPrefix += s"${edStatusMap(el)._2}"
							greekStringAdfix +=  s"${edStatusMap(el)._3}"
							//greekString = s"${edStatusMap(el)._2}${greekString}${edStatusMap(el)._3}"
						}
						case None =>  edStatus = edStatusMap("default")._1
					}
				}
			}

			// Check to see if the string is a multifunction word
			val mf = multifunctionTokens.get(greekString)
			mf match {
				case Some(mfmatch) =>  {
					val defText1 = mfmatch._1
					val defText2 = mfmatch._2

					val aaUrn1 = makeExemplarUrn(tt._1, s"${i+1}a")
					val aaUrn2 = makeExemplarUrn(tt._1, s"${i+1}b")

					tokenType = tokenTypeMap("multifunction")
					// Make 2 strings 【】
						val curns1 = makeCollectionUrn(collectionUrnString,aaUrn1,i)
						val s1 = s"${curns1}\t${tt._1}\t${tokenType}\t${greekStringPrefix}【${defText1}】${defText2}${greekStringAdfix}\t${edStatus}\t${discourseLevel}\t${aaUrn1}"
						returnArray += s1

// WE NEED TO COUNT BETTER!

						val curns2 = makeCollectionUrn(collectionUrnString,aaUrn2,i)
						val s2 = s"${curns2}\t${tt._1}\t${tokenType}\t${greekStringPrefix}${defText1}【${defText2}】${greekStringAdfix}\t${edStatus}\t${discourseLevel}\t${aaUrn2}"
						returnArray += s2
				}
				case None => {
						tokenType = tokenTypeMap("word")
						val aaUrn = makeExemplarUrn(tt._1, s"${i+1}")
						val curns = makeCollectionUrn(collectionUrnString,aaUrn,i)
						// test for punctation
						for (c <- punctuation) if (c.toString == greekString){
							tokenType = tokenTypeMap("punc")
							//if (greekString == "\""){ greekString = "(quote)" }
						}
						// test for token-type 'other'
						for (cOther <- nonCharacters; cTest <- greekString) if (cOther == cTest) tokenType = tokenTypeMap("other")
						// Make 1 string
						// ORCA_URN	AnalyzedText	Analysis	textDeformation editorialStatus	discourceLevel
						val s = s"${curns}\t${tt._1}\t${tokenType}\t${greekStringPrefix}${greekString}${greekStringAdfix}\t${edStatus}\t${discourseLevel}\t${aaUrn}"
						returnArray += s
				}
			}

			returnArray.toArray
	}

	def processText(t:String, ne: String) : Array[Tuple2[String, String]] = {
		var ma =  scala.collection.mutable.ArrayBuffer[Tuple2[String, String]]()
		val tokenizedPunc = delimiters.replaceAllIn(t," $1 ")
		for ( tok <- tokenizedPunc.split("\\s+")){
			if ( tok != ""){
				val myTuple = (tok,ne)
				ma += myTuple
			}
		}
		ma.toArray
	}

	def processXmlText(xmln: scala.xml.Node, ne: String): Array[Tuple2[String, String]] = {
		var ma =  scala.collection.mutable.ArrayBuffer[Tuple2[String, String]]()
		val newNE = if (ne =="") s"${xmln.label}" else s"${ne},${xmln.label}"
		for( nn <- xmln.child){
			nn.label match {
				case "#PCDATA" => {
					for( nn <- processText(nn.text, newNE)){
						ma += nn
					}
				}
				case _ => {
					for( nn2 <- processXmlText(nn,newNE)){
						ma += nn2
					}
				}
			}
		}
		ma.toArray
	}


 def acquireTextAndCitations(source: String, fileLoc: String) = {
		 try {
		 source match {
			 case "filesystem" => {
				val tabFields = Source.fromFile(fileLoc).getLines.toVector.map(_.split(separator))
		    tabFields
			 }
			 case "URL" => {
				 val tabFields = Source.fromURL(fileLoc).getLines.toVector.map(_.split(separator))
				 tabFields
			 }
			 case _ => throw new IllegalArgumentException(s"Parameter 'source' must be 'filesystem' or 'url'. (${source})")
		 }
	 } catch {
		 case e: Throwable => throw new Exception(s"${e}")
	 }
 }

 def makeCollectionUrn(collUrn: String, aaUrn: String, i: Integer): String = {
	 val turn = CtsUrn(aaUrn)
	 val citationPart = turn.passageComponent.replaceAll("\\.","_")
	 val returnUrnString = s"${collUrn}${citationPart}_${i}"
	 returnUrnString

 }

 def makeExemplarUrn(urnStr: String, citationValue: String):String = {
	 val edUrn = CtsUrn(urnStr)
	 val woPassage = edUrn.dropPassage.toString.dropRight(1)
	 val passageComp = edUrn.passageComponent.split("@")(0)
	 var exemplarUrnString= s"${woPassage}.syntaxToken:${passageComp}.${citationValue}"
	 exemplarUrnString
 }

}
