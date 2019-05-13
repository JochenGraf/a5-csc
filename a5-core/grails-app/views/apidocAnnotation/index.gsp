<%@ page import="grails.plugins.rest.client.RestBuilder" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="a5"/>
    <title>Annotation API</title>
</head>
<body>
<h1>Annotation API</h1>
<p class="lead">API for presentation and discovery of media annotations.</p>
<p>The Annotation API is compatible with the following standards:</p>
<dl class="dl-horizontal">
    <dt><a target="_blank" href="https://www.w3.org/TR/annotation-model/">[Web Annotation Data Model]</a></dt>
    <dd>Web Annotation Data Model. Edited by Robert Sanderson, Paolo Ciccarese, and Benjamin Young.
    Copyright © 2017 W3C® (MIT, ERCIM, Keio, Beihang). W3C liability, trademark and document use rules apply.</dd>
    <dt><a target="_blank"
           href="http://iiif.io/api/presentation/2.1/">[IIIF Presentation API]</a></dt>
    <dd>IIIF Presentation API 2.1. Edited by Michael Appleby, Tom Crane, Robert Sanderson, Jon Stroop, and Simeon
    Warner. Copyright © 2012-2016 Editors and contributors. Published by the IIIF Consortium under the CC-BY license.</dd>
    <dt><a target="_blank"
           href="http://docs.oasis-open.org/odata/odata/v4.0/errata03/os/complete/part1-protocol/odata-v4.0-errata03-os-part1-protocol-complete.html">[OData-Part2]</a></dt>
    <dd>OData Version 4.0. Part 2: URL Conventions Plus Errata 03. Edited by Michael
        Pizzo, Ralf Handl, and Martin Zurmuehl. 02 June 2016. OASIS Standard incorporating Approved Errata 03.</dd>
    <dt><a target="_blank" href="http://docs.oasis-open.org/odata/odata-json-format/v4.0/errata03/os/odata-json-format-v4.0-errata03-os-complete.html">[OData-JSON-Format-v4.0]</a></dt>
    <dd>OData JSON Format Version 4.0 Plus Errata 03. Edited by Ralf Handl,
        Michael Pizzo, and Mark Biamonte. 02 June 2016. OASIS Standard incorporating Approved Errata 03.</dd>
</dl>
<h5>Contents</h5>
<ul id="toc" class="list-unstyled"></ul>

<h2>Presentation Requests</h2>
<p></p>

<!-- Manifest -->
<h3>Manifest Request <small>[sc:Manifest]</small></h3>
<p><samp>GET {scheme}://{server}/api/annotation/{repository}/<strong>Manifest({$identifier})</strong></samp></p>
<p>Represents the entry point for a annotated media object.</p>
<p>Compatible with: <a target="_blank" href="http://iiif.io/api/presentation/2.1/#manifest">http://iiif.io/api/presentation/2.1/#manifest</a></p>
<h4>Properties</h4>
<table class="table table-bordered table-striped">
    <tr>
        <td><samp>id</samp></td>
        <td>id of this manifest</td>
    </tr>
    <tr>
        <td><samp>label</samp></td>
        <td>label of this manifest</td>
    </tr>
    <tr>
        <td><samp>type</samp></td>
        <td>types that describe this manifest</td>
    </tr>
    <tr>
        <td><samp>members</samp></td>
        <td>list of canvas ids this manifest contains, expandable with "<samp>?$expand</samp>"</td>
    </tr>
    <tr>
        <td><samp>within</samp></td>
        <td>a reference to the parent repository object, expandable with "<samp>?$expand</samp>"</td>
    </tr>
</table>
<h4>Examples</h4>
<apidoc:desctable>
    <tbody>
        <tr>
            <td><apidoc:link href="/api/annotation/a5-1/Manifest(a5:test-eaf)?pretty" pattern="Manifest"/></td>
            <td>Basic example.</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/annotation/a5-1/Manifest(a5:test-eaf)?pretty&\$expand=members,within" pattern="expand=members,within"/></td>
            <td>Example with expanded "<samp>members</samp>" and "<samp>within</samp>" properties.</td>
        </tr>
    </tbody>
</apidoc:desctable>

<!-- Canvas -->
<h3>Canvas Request <small>[sc:Canvas]</small></h3>
<p><samp>GET {scheme}://{server}/api/annotation/{repository}/<strong>Canvas({$identifier})</strong></samp></p>
<p>Represents a frame of reference for content resources such as annotation layers and media objects.</p>
<p>Compatible with: <a target="_blank" href="http://iiif.io/api/presentation/2.1/#canvas">http://iiif.io/api/presentation/2.1/#canvas</a></p>
<h4>Properties</h4>
<table class="table table-bordered table-striped">
    <tr>
        <td><samp>id</samp></td>
        <td>id of this canvas</td>
    </tr>
    <tr>
        <td><samp>label</samp></td>
        <td>label of this canvas</td>
    </tr>
    <tr>
        <td><samp>type</samp></td>
        <td>types that describe this canvas</td>
    </tr>
    <tr>
        <td><samp>media</samp></td>
        <td>list of ids of media objects associated with this canvas, expandable with "<samp>?$expand</samp>"</td>
    </tr>
    <tr>
        <td><samp>otherContent</samp></td>
        <td>list of ids of content resources such as annotation layers associated with this canvas</td>
    </tr>
</table>
<h4>Examples</h4>
<apidoc:desctable>
    <tbody>
        <tr>
            <td><apidoc:link href="/api/annotation/a5-1/Canvas(a5:test-eaf/canvas/0)?pretty" pattern="Canvas"/></td>
            <td>Basic example.</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/annotation/a5-1/Canvas(a5:test-eaf/canvas/0)?pretty&\$expand=media" pattern="Canvas"/></td>
            <td>Example with expanded "<samp>media</samp>" property.</td>
        </tr>
    </tbody>
</apidoc:desctable>

<!-- AnnotationLayer -->
<h3>AnnotationLayer Request <small>[sc:AnnotationLayer]</small></h3>
<p><samp>GET {scheme}://{server}/api/annotation/{repository}/<strong>Layer({$identifier})</strong></samp></p>
<p>Represents an ordered sequence of annotations belonging together, e.g., a transcription.</p>
<p>Compatible with: <a target="_blank" href="http://iiif.io/api/presentation/2.1/#layer">http://iiif.io/api/presentation/2.1/#layer</a></p>
<h4>Properties</h4>
<table class="table">
    <tr>
        <td><samp>id</samp></td>
        <td>id of this annotation layer</td>
    </tr>
    <tr>
        <td><samp>label</samp></td>
        <td>label for this annotation layer</td>
    </tr>
    <tr>
        <td><samp>type</samp></td>
        <td>types that describe this annotation layer</td>
    </tr>
    <tr>
        <td><samp>total</samp></td>
        <td>total number of resources contained in this layer</td>
    </tr>
    <tr>
        <td><samp>hasParent</samp></td>
        <td></td>
    </tr>
    <tr>
        <td><samp>isParentOf</samp></td>
        <td></td>
    </tr>
    <tr>
        <td><samp>resources</samp></td>
        <td>ordered sequence of annotation resources</td>
    </tr>
</table>
<h4>Examples</h4>
<apidoc:desctable>
    <tbody>
        <tr>
            <td><apidoc:link href="/api/annotation/a5-1/Layer(a5:test-eaf/annotationlayer/0)?pretty"/></td>
            <td>Basic example.</td>
        </tr>
    </tbody>
</apidoc:desctable>

<!-- Annotation -->
<h3>Annotation Request <small>[oa:Annotation]</small></h3>
<p><samp>GET {scheme}://{server}/api/annotation/{repository}/<strong>Annotation({$identifier})</strong></samp></p>
<p>Represents a media annotation.</p>
<p>Compatible with: <a target="_blank" href="https://www.w3.org/TR/annotation-model/#annotations">https://www.w3.org/TR/annotation-model/#annotations</a></p>
<h4>Properties</h4>
<table class="table table-bordered table-striped">
    <tr>
        <td><samp>id</samp></td>
        <td>id of this annotation</td>
    </tr>
    <tr>
        <td><samp>label</samp></td>
        <td>label of this annotation</td>
    </tr>
    <tr>
        <td><samp>type</samp></td>
        <td>types that describe this annotation</td>
    </tr>
    <tr>
        <td><samp>bodyValue</samp></td>
        <td>the string value of this annotation</td>
    </tr>
    <tr>
        <td><samp>target</samp></td>
        <td>id of the target canvas this annotation is associated with</td>
    </tr>
    <tr>
        <td><samp>selector</samp></td>
        <td>fragment selector representing a reference to parts of the target canvas, see <apidoc:link href="/apidoc/media#Media_Manipulation_Request"/></td>
    </tr>
</table>
<h4>Examples</h4>
<apidoc:desctable>
    <tbody>
        <tr>
            <td><apidoc:link href="/api/annotation/a5-1/Annotation(a5:test-eaf/annotation/ann8)?pretty"/></td>
            <td>Basic example.</td>
        </tr>
    </tbody>
</apidoc:desctable>

<h2>Disovery Requests</h2>

<!-- $search -->
<h3>Fulltext Search <small>[$search]</small></h3>
<p><samp>GET {scheme}://{server}/api/annotation/{repository}/(Layers|Annotations)?<strong>$search={querystring}</strong></samp></p>
<p>Matches a query string against annotation values. The result is a collection of annotation layers or a collection of annotations.</p>
<p>Query strings follow the <a target="_blank" href="https://lucene.apache.org/core/2_9_4/queryparsersyntax.html">Lucene Syntax</a>.</p>
<apidoc:desctable>
    <tbody>
    <tr>
        <td><apidoc:link href="/api/annotation/a5-1/Layers?\$search=tendas&pretty" pattern="\$search=tendas"/></td>
        <td>Searches for the term <samp>"tendas"</samp> in the annotations of repository <samp>"a5-1"</samp> and
        returns a collection of matched annotation layers.</td>
    </tr>
    <tr>
        <td><apidoc:link href="/api/annotation/a5-1/Annotations?\$search=tendas&pretty" pattern="\$search=tendas"/></td>
        <td>Searches for the term <samp>"tendas"</samp> in the annotations of repository <samp>"a5-1"</samp> and
        returns a collection of matched annotations.</td>
    </tr>
    </tbody>
</apidoc:desctable>

<!-- $filter -->
<h3>Keyword filtering <small>[$filter]</small></h3>
<p><samp>GET {scheme}://{server}/api/annotation/{repository}/(Layers|Annotations)?$filter={field1 (eq|ne|gt|ge|lt|le) "term1" (and|or|not) field2 ...}</samp>
<p>Filters annotations or annotation layers according to field values.</p>
<apidoc:desctable>
    <tbody>
    <tr>
        <td><apidoc:link href="/api/annotation/a5-1/Annotations?pretty&\$filter=target eq a5:Gutob_Komplas/canvas/0"/></td>
        <td>Example returns collection of annotations of repository "<samp>a5-1</samp>" associated with canvas "<samp>a5:Gutob_Komplas/canvas/0</samp>"</td>
    </tr>
    <tr>
        <td><apidoc:link href="/api/annotation/a5-1/Layers?pretty&\$filter=type eq eaf:TIER@Symbolic_Subdivision"/></td>
        <td>Example returns collection of annotation layers of repository "<samp>a5-1</samp>" with type "<samp>eaf:TIER@Symbolic_Subdivision</samp>"</td>
    </tr>
    </tbody>
</apidoc:desctable>

<!-- $count -->
<h3>Counting Hits <small>[$count]</small></h3>
<p><samp>GET {scheme}://{server}/api/annotation/{repository}/(Layers|Annotations)?<strong>$count</strong></samp></p>
<p>Whether to include number of hits in the query result.</p>
<apidoc:desctable>
    <tbody>
    <tr>
        <td><apidoc:link href="/api/annotation/a5-1/Layers?\$count&pretty" pattern="\$count"/></td>
        <td>Includes number of matched annotation layers.</td>
    </tr>
    <tr>
        <td><apidoc:link href="/api/annotation/a5-1/Annotations?\$count&pretty" pattern="\$count"/></td>
        <td>Includes number of matched annotations.</td>
    </tr>
    </tbody>
</apidoc:desctable>

<!-- $skip, $top -->
<h3>Navigating Hits <small>[$skip, $top]</small></h3>
<p><samp>GET {scheme}://{server}/api/annotation/{repository}/(Layers|Annotations)?<strong>$skip={integer}&$top={integer}</strong></samp></p>
<p>Allows navigation of search results.</p>
<apidoc:desctable>
    <tbody>
    <tr>
        <td><apidoc:link href="/api/annotation/a5-1/Layers?\$skip=10&\$top=3&pretty"/></td>
        <td>Example returns 3 annotation layers, skipping 10 layers.</td>
    </tr>
    <tr>
        <td><apidoc:link href="/api/annotation/a5-1/Annotations?\$skip=1000&\$top=100&pretty"/></td>
        <td>Example returns 100 annotations, skipping 1000 annotations.</td>
    </tr>
    </tbody>
</apidoc:desctable>
</body>
</html>