package edu.furman.folio.citablegraph
package util

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import java.io._
import scala.io.Source
import scala.collection.mutable.ArrayBuffer

case class SyntaxWriter(st: SyntaxTokenizer) {

	val exists = true

	// all-purpose method for outputting an array of Strings to a file
    def writeTextFile(a: Array[String], f: File) {
      val pw = new PrintWriter(f)
      for (l <- a) { pw.write(l); pw.write("\n") }
      pw.close
    }


	def writeOrcaFile(filename: String, orcaUrn: String, directory: String){
			val v = st.makeOrcaCollection(orcaUrn)
		   val d: java.io.File = checkDirectory(directory)
		   val fn = s"""Orca_${filename}.tsv"""
			val f = new File(s"${directory}/${fn}")
			writeTextFile(v, f)
	}

	def writeCts2Col(filename: String, directory: String) = {
		val v = st.makeCts2ColText
		val d: java.io.File = checkDirectory(directory)
		val fn = s"""CTS-2col-${filename}.txt"""
		val f = new File(s"${directory}/${fn}")
		writeTextFile(v, f)
	}

	// writes collectionArray to a file
	def writeCollection(filename: String, directory: String) {
		val d: java.io.File = checkDirectory(directory)
		val f = new File(s"${directory}/TokenCollection-${filename}.tsv")
		var tempArray =  scala.collection.mutable.ArrayBuffer[String]()
		tempArray += "Urn\tAnalyzedText\tTokenType\tTextDeformation\tEditorialStatus\tDiscourseLevel\tExemplarUrn\tSequence"
		for (i <- 0 until st.collectionArray.size){
			tempArray += s"${st.collectionArray(i)}\t${i}"
		}
		writeTextFile(tempArray.toArray, f)
	}

	/* PRIVATE METHODS */

	private def checkDirectory(d: String): java.io.File = {
		val dir: File = new File(d)
		if ( dir.exists() != true ){
			dir.mkdir()
			dir
		} else {
			dir
		}
	}



}
