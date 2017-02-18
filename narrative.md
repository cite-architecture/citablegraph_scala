# Citable Graphs

This **citableGraph** library allows identification and retrieval of scholarly graphs and their constituent parts by means of machine-actionable. It is part of the CITE Architecture.

## Using the Citable Graph Library

Typical pattern of usage in Scala: import the library

    import edu.furman.folio.citableGraph._


## Naming

The library is **citableGraph**. It is abbreivated **CGrS** (Citable Graph Services), with the 'r' added to make it visually distinct from **CTS** (Canonical Text Services).

## Graphs and their Citations

Graphs are captured in the **CGrs Data Model** and cited by **CITE2 URNs**. The CitableGraph library defines an *extension* to the CITE2 URN, in the form of a '@'-delimited annotation attached to the URN, that allows identification of parts of a graph:

- `urn:cite2:ns:collection.version:graph1@v1,v2,e1` : CITE2 URN with subreference specifically identifying two vertices (`v1`,`v2`) and one edge (`e1`).
- `urn:cite2:ns:collection.version:graph1@v1-v4` : CITE2 URN with subreference specifically identifying all paths between two vertices (`v1`,`v4`).

A scholarly graph must be a **Directed Graph**, consisting of triples: two citable scholarly objects as vertices, a citable, asserted relationship as an edge. **CGrS** assumes that any scholarly graph may be a **Directed Multigraph** (**Quiver**), in which any two vertices *may* be joined by more than one edge.

## ScalaGraph

- For now, we are limiting CGrS Graphs to Labeled, Directed Edges (LDiEdge, "A" ~+> "B"). We are excluding *hyperedges* and *weighted edges*.


## Data Model

Citable Graph Services (CGrS) separates concerns by means of its data model. The **CGrS Service** manages interaction with users and other online services; **CGrS Collections** are CITE Collections. The CITE Collection Protocol allows *extensions*, providing special methods for interacting with known kinds of data. For example, CITE Collections can describe collections of images, providing properties for identifiers (Cite2Urn), labels, and statements of rights. But because images are a specific and undertood kind of data, the `hmt:CiteImage` extension provides methods like `GetBinaryImage` that apply only to data of this kind. The **CGrS Object**, a member of the CGrS Collection, is a CITE Collection Object, but is also identifies a **Citable Graph** object, a special data type for which the `fu:CitableGraph` extension offers methods specific to graphs.

- **CGrS Service** : The CGrS Service manages REST requests and responses to and from itself, and optionally connections to online CITE Services for resolving CTS and CITE2 urns. It also provides serializations of graphed data in various formats. The Service works on a…

	- **CGrS GraphCorpus** : A CGS GraphCorpus offers methods that extract, compare, and aggregate data across one or more…

		- **CGrS Collections** : A CGS Collection is a CITE Collection extended by the 	`fu:CitableGraph` extension. As a CITE Collection, a CGS Collection is a versioned, unordered collection of Objects, each identified by a Cite2 URN. Each of these is a…

			- **CGrS Object** : A CGrS Object is a CITE Collection Object. As such, its required properties are: `urn` (which must be a version-level URN), `label`, `description`. By virtue of the `fu:CitableGraph` Extension, it identifies a…

			- **CitebleGraph** : A CitableGraph is an abstract data object, that has the `urn`, `label`, and `description` properties from the *CGrS Object*, but additionally an Array of **CGVertex** objects, and an Array of **CGEdge** objects:

				- **CGVertex**: A CGVertex is an abstract data object consisting of properties: `id` (string), `label` (string), `dataurn` (a CTS or CITE2 URN), `selected` (boolean).

				- **CGEdge**: A CGEdge is an abstract data object consisting of properties: `id` (string), `label` (string), `sourceurn` (a CTS or CITE2 URN), `targeturn` (a CTS or CITE2 URN), `relationurn` (a CITE2 URN), `selected` (boolean).

## Utilities

- **SyntaxTokenizer** A utility class that takes, as input, a CTS text in a tabular format (see [CITE Archive Manager](https://github.com/cite-architecture/cite-archive-manager)).  It offers methods for producing a citable tokenization of the text specifically for syntactical analysis.

- **SyntaxWriter** A utility class, depending on **SyntaxTokenizer**, that can write as tabular data the CITE collections, indices, and *analytical exemplars* necessary for working with tokenized texts in the CITE Architecture.

- **Treebanker** A utility class, depending on **SyntaxTokenizer**, that generates XML files that can be loaded into the [Perseids Treebank Editor](http://sosol.perseids.org/sosol/) for creating syntactic graphs of texts.

- **TreebankGraphWriter** A utility class that parses XML files capturing syntactic analyses by means of the [Perseids Treebank Editor](http://sosol.perseids.org/sosol/) and outputs tabular expressions of these analysis as citable vertices and edges.

## Build Citable Graph Collections

**1. read a delimited text file into an CitableGraph object**

    `val cgraph = CitableGraph("src/test/resources/syntax.tsv")`

    [TBD]

## Service-like aliases

Providing named functions like `findInGraph` and `getPaths` or `findOrphans`, `getSubgraph`… [TBD]
