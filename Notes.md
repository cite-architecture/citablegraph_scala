

## ScalaGraph stuff

:require graph-core_2.12-1.11.4.jar
import scalax.collection.Graph
import scalax.collection.edge.LDiEdge     // labeled directed edge

import scalax.collection.edge.Implicits._ // shortcuts

// Stuff so you can access labels
import scalax.collection.edge.LBase.LEdgeImplicits


# Try1


case class CiteNode(id: String, urn: String, nodeLabel: String)
case class CiteEdge(id: String, sourceurn: String, targeturn: String, relationurn: String, edgeLabel: String)
object CiteImplicit extends LEdgeImplicits[CiteEdge]; import CiteImplicit._

var tok1 = CiteNode("v1","urn:lex:menin", "μῆνιν")
var tok1 = CiteNode("v1","urn:lex:menin", "μῆνιν")

var rel1 = CiteEdge("e1","urn:aeide","urn:lex:thea","urn:relations.direct:sub")

val gu = Graph(
		LDiEdge(tok2, tok3)(rel1),
		LDiEdge(tok2, tok1)(rel2)
	)


# Accessing Stuff1

~~~ Scala

// cycling through nodes
gu.nodes.foreach( n => println(n.id) )

// cycling through edges
scala> gu.edges.foreach( e => println(e._1) )
CiteNode(v2,urn:aeide)
CiteNode(v2,urn:aeide)

scala> gu.edges.foreach( e => println(e._2) )
CiteNode(v3,urn:thea)
CiteNode(v1,urn:menin)

scala> gu.edges.foreach( e => println(e._2.id))
v3
v1

//Find node based on urn property
var foundIt = gu find (gu having (node = _.urn == "urn:aeide"))



~~~


#Misc

~~~ Scala

List(1,2,3,4,5).foldLeft("")(_ + _)

("" /: List(1,2,3,4,5))(_+_)

~~~

**Edge Factory**
"A"~+>"B"	LDiEdge("A","B")	labeled directed edge from "A" to "B"

## Steps

Required:

1. Citable tokens
	1. Citable objects for a DSE Graph
	1. Citable tokens for a text
1. Process graph .tsvs
	1. Syntax
		1.
	1. DSE
		1. Easy case


- Inputs
	- graphUtn \t srcUrn \t relUrn \t targetUrn
	- Test Data : syntax, dse
- Create EdgeCollection and VertexCollection

## Inputs

**Six Column Input** :

> `sourceUrn` \t `sourceLabel` \t `targetUrn` \t `targetLabel` \t `relationUrn` \t `relationLabel`

For Syntax:

	- The *source* is the *dependant*. We read it that way: "'Rage' *is the of* 'sing'."
