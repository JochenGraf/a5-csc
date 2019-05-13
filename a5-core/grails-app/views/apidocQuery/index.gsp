<!DOCTYPE html>
<%@ page import="grails.converters.JSON" %>
<g:set var="apiQueryService" bean="apiQueryService"/>
<html>
<head>
    <meta name="layout" content="a5"/>
    <title>Query API</title>
</head>
<body>
<h1>Query API</h1>
<p class="lead">API for faceted querying of metadata documents.</p>
<p>Not a complete implementation of but conform to the following OASIS standards:</p>
<dl class="dl-horizontal">
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

<h2>Response Format</h2>
<% queryUrl = "$webRequest.baseUrl/api/query/a5-2?pretty&\$search=story&\$count&\$orderby=Title&\$skip=10&\$top=3&highlight&facets=Keywords,Country,ResourceType&autocomplete" %>
<% query = apiQueryService.query([repository: "a5-2", search: "story", count: true, orderby: "Title", skip: "10", top: "3", highlight: "true", facets: "Keywords,Country,ResourceType", autocomplete: "true"]) as JSON %>
<% query.prettyPrint = true %>
<p>Example: <a target="_blank" href="${queryUrl}">${queryUrl}</a></p>
<pre>${query}</pre>

<h2>Query Requests</h2>

<!-- $search -->
<h3>Fulltext Search <small>[$search]</small></h3>
<p><samp>GET {scheme}://{server}/api/query/{repository}?<strong>$search={querystring}</strong></samp></p>
<p>Matches a query string. Query strings follow the <a target="_blank" href="https://lucene.apache.org/core/2_9_4/queryparsersyntax.html">Lucene Syntax</a>.</p>
<apidoc:desctable param="\$search={querystring}">
    <tbody>
        <tr>
            <td><apidoc:link href="/api/query/a5-1?\$search=communication&pretty" pattern="\$search=communication"/></td>
            <td>Searches for the term <samp>"communication"</samp> in repository <samp>"a5-1"</samp>.</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/query/a5-2?\$search=*&pretty" pattern="\$search="/></td>
            <td><samp>"*"</samp> matches all fulltext terms contained in repository <samp>"a5-2"</samp>.</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/query/a5-1?\$search=audio OR video&pretty" pattern="\$search=audio OR video"/></td>
            <td>Matches all documents containing the fulltext terms <samp>"audio"</samp> <strong>or</strong> <samp>"video"</samp>.</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/query/a5-1?\$search=audio AND video&pretty" pattern="\$search=audio AND video"/></td>
            <td>Matches all documents containing the fulltext terms <samp>"audio"</samp> <strong>and</strong> <samp>"video"</samp>.</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/query/a5-2?\$search=audio NOT video&pretty" pattern="\$search=audio NOT video"/></td>
            <td>Returns all documents matching <samp>"audio"</samp>, <strong>but not</strong> <samp>"video"</samp>.</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/query/a5-2?\$search=bali*&pretty" pattern="\$search=bali*"/></td>
            <td>Matches all fulltext terms starting with <samp>"bali*"</samp>.</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/query/a5-2?\$search=*ish&pretty" pattern="\$search=*ish"/></td>
            <td>Matches all fulltext terms ending with <samp>"*ish"</samp>.</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/query/a5-2?\$search='traditional narrative'&pretty" pattern="\$search='traditional narrative'"/></td>
            <td>Matches all documents containing the phrase <samp>"traditional narrative"</samp>.</td>
        </tr>
    </tbody>
</apidoc:desctable>
<p>For more possibilities, see the official <a target="_blank" href="https://lucene.apache.org/core/2_9_4/queryparsersyntax.html">Lucene documentation</a>.</p>

<!-- $filter -->
<h3>Keyword Filtering <small>[$filter]</small></h3>
<p><samp>GET {scheme}://{server}/api/query/{repository}?<strong>$filter={field1 (eq|ne|gt|ge|lt|le) "term1" (and|or|not) field2 ...}</strong></samp></p>
<p>In contrast to "<samp>$search</samp>", "<samp>$filter</samp>" evaluates exact matches on field values.</p>
<ul>
    <li>A filter is a combination of a field name, a boolean operator "<samp>(eq|ne|gt|ge|lt|le)</samp>", and a term.</li>
    <li>Any number of filters can be combined with the "<samp>and</samp>", "<samp>or</samp>", "<samp>not</samp>" operators
        "<samp>(and|or|not)</samp>".</li>
    <li>Boolean operators are:
        <dl class="dl-horizontal">
            <dt>eq</dt>
            <dd>equals</dd>
            <dt>ne</dt>
            <dd>not equals</dd>
            <dt>gt</dt>
            <dd>greater than</dd>
            <dt>ge</dt>
            <dd>greater than or equals</dd>
            <dt>lt</dt>
            <dd>lesser than</dd>
            <dt>le</dt>
            <dd>lesser than or equals</dd>
        </dl>
    </li>
    <li>Filters can be grouped with parentheses.</li>
    <li>If a term contains spaces, the term must be enclosed in single quotes '<samp>term with spaces</samp>' or double quotes
        "<samp>term with spaces</samp>". Quotes can be dropped otherwise.</li>
    <li>Since the "<samp>+</samp>" sign has a special meaning for URLs, plus signs must be encoded as "<samp>%2B</samp>" when
        used in a term, e.g., "<samp>contentType eq 'text/eaf%2Bxml'</samp>" searches for "<samp>contentType eq 'text/eaf+xml'</samp>".
        All other characters do not need special encoding.</li>
</ul>
<apidoc:desctable param="\$filter">
    <tbody>
    <tr>
        <td><apidoc:link href="/api/query/a5-1?pretty&\$filter=id eq hdl:11341/00-0000-0000-0000-1BD6-5"/></td>
        <td>Returns document with field "<samp>id</samp>" equals to "<samp>hdl:11341/00-0000-0000-0000-1BD6-5</samp>". No quotes are used here to enclose the term.</td>
    </tr>
    <tr>
        <td><apidoc:link href="/api/query/a5-1?pretty&\$filter=Format eq 'text/x-eaf%2Bxml'"/></td>
        <td>Returns all documents with field "<samp>Format</samp>" equals to "<samp>text/x-eaf+xml</samp>". Note that the plus sign "<samp>+</samp>"
            has to be encoded as "<samp>%2B</samp>".</td>
    </tr>
    <tr>
        <td><apidoc:link href="/api/query/a5-1?pretty&\$filter=Format ne 'text/x-eaf%2Bxml'"/></td>
        <td>Returns all documents with field "<samp>Format</samp>" <strong>not</strong> equals to "<samp>text/x-eaf+xml</samp>". Note that the plus sign "<samp>+</samp>"
            has to be encoded as "<samp>%2B</samp>".</td>
    </tr>
    <tr>
        <td><apidoc:link href="/api/query/a5-1?pretty&\$filter=Format ne text/x-eaf%2B or Format ne audio/x-wav"/></td>
        <td>Returns all documents with field "<samp>Format</samp>" <strong>not</strong> equals to "<samp>text/x-eaf+xml</samp>" or <strong>not</strong>
            equals to "<samp>audio/x-wav</samp>.</td>
    </tr>
    <tr>
        <td><apidoc:link href="/api/query/a5-2?pretty&\$filter=(ObjectLanguage eq Bali or ObjectLanguage eq German) and ResourceType eq audio"/></td>
        <td>Returns all documents with object language equals to "<samp>Bali</samp>" or "<samp>German</samp>" and resource type equals to "<samp>audio</samp>".</td>
    </tr>
    </tbody>
</apidoc:desctable>

<!-- $count -->
<h3>Counting Hits <small>[$count]</small></h3>
<p><samp>GET {scheme}://{server}/api/query/{repository}?<strong>$count</strong></samp></p>
<p>Whether to include number of hits in the query result.</p>
<apidoc:desctable param="\$count">
    <tbody>
        <tr>
            <td><apidoc:link href="/api/query/a5-1?\$search=communication&\$count&pretty" pattern="\$count"/></td>
            <td>Searches for "<samp>communication</samp>" in repositoy named <samp>"a5-1"</samp> and includes
                the number of hits in field "<samp>@odata.count</samp>".</td>
        </tr>
    </tbody>
</apidoc:desctable>

<!-- $orderby -->
<h3>Sorting Hits <small>[$orderby]</small></h3>
<p><samp>GET {scheme}://{server}/api/query/{repository}?<strong>$orderby={field1 (asc|desc),field2 (asc|desc),...}</strong></samp></p>
<p>Sorts results according to specific fields in ascending or descending order.</p>
<apidoc:desctable param="\$orderby">
    <tbody>
        <tr>
            <td><apidoc:link href="/api/query/a5-2?\$search=*&\$orderby=ProjectDisplayName&pretty" pattern="\$orderby=ProjectDisplayName"/></td>
            <td>Returns all documents of repository <samp>"a5-2"</samp> ordered by field <samp>"ProjectDisplayName"</samp> in <strong>default</strong> ascending order.</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/query/a5-2?\$search=*&\$orderby=ProjectDisplayName asc&pretty" pattern="\$orderby=ProjectDisplayName asc"/></td>
            <td>Returns all documents of repository <samp>"a5-2"</samp> ordered by field <samp>"ProjectDisplayName"</samp> in <strong>asc</strong>ending order.</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/query/a5-2?\$search=*&\$orderby=ProjectDisplayName desc,Country asc&pretty" pattern="\$orderby=ProjectDisplayName desc,Country asc"/></td>
            <td>Returns all documents of repository <samp>"a5-2"</samp> primarily ordered by field <samp>"ProjectDisplayName desc"</samp> (descending) and secondarily ordered by
                field <samp>"Country asc"</samp> (ascending).</td>
        </tr>
    </tbody>
</apidoc:desctable>

<!-- $skip, $top-->
<h3>Navigating Hits <small>[$skip,$top]</small></h3>
<p><samp>GET {scheme}://{server}/api/query/{repository}?<strong>$skip={integer}&amp;$top={integer}</strong></samp></p>
<p>Allows navigation of search results.</p>
<apidoc:desctable param="\$skip,\$top">
    <tbody>
        <tr>
            <td><apidoc:link href="/api/query/a5-1?\$search=*&\$skip=10&\$top=30&pretty" pattern="skip=10&top=30"/></td>
            <td>Example matches all documents contained in repository <samp>"a5-1"</samp>. The first <samp>10</samp> results are
                skipped (<samp>skip=10</samp>). The number of results is <samp>30</samp> (<samp>top=30</samp>).</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/query/a5-2?\$search=communication&\$skip=5&\$top=1&pretty" pattern="skip=5&top=1"/></td>
            <td>Example matches the term <samp>"communication"</samp> against repository <samp>a5-2</samp>. The first
                <samp>5</samp> results are skipped (<samp>skip=5</samp>). Only <samp>1</samp> item is returned (<samp>top=1</samp>).</td>
        </tr>
    </tbody>
</apidoc:desctable>

<!-- $select -->
<h3>Result Serialization <small>[$select]</small></h3>
<p><samp>GET {scheme}://{server}/api/query/{repository}?<strong>$select={field1,field2,field3,...}</strong></samp></p>
<p>Defines which fields to include in the JSON result.</p>
<apidoc:desctable param="\$select">
    <tbody>
        <tr>
            <td><apidoc:link href="/api/query/a5-2?\$search=*&\$select=id,ProjectDisplayName&pretty" pattern=""/></td>
            <td>Returns all documents of repository "<samp>a5-2</samp>", but only fields "<samp>id</samp>", and "<samp>ProjectDisplayName</samp>".</td>
        </tr>
    </tbody>
</apidoc:desctable>

<!-- field -->
<h3>Searching Selected Fulltext Fields <small>[fields]</small></h3>
<p><samp>GET {scheme}://{server}/api/query/{repository}?<strong>fields={field1,field2,field3,...}</strong></samp></p>
<p>Evaluates a fulltext search on selected fields only.</p>
<apidoc:desctable param="${par.fields.name}">
    <tbody>
    <tr>
        <td><apidoc:link href="/api/query/a5-2?\$search=monolog*&fields=Keywords&pretty" pattern="fields=Keywords"/></td>
        <td>Searches for <samp>"monolog*"</samp>, but only in field <samp>"Keywords"</samp>.</td>
    </tr>
    <tr>
        <td><apidoc:link href="/api/query/a5-2?\$search=communication&fields=Title,Description&pretty" pattern="fields=Title,Description"/></td>
        <td>Searches for <samp>"communication"</samp>, but only in fields <samp>"Title"</samp> and <samp>"Description"</samp>.</td>
    </tr>
    </tbody>
</apidoc:desctable>

<!-- highlight -->
<h3>Hit Highlighting <small>[highlight]</small></h3>
<p><samp>GET {scheme}://{server}/api/query/{repository}?<strong>highlight</strong></samp></p>
<p>If set, returns field matches marked with hit highlighting.</p>
<apidoc:desctable>
    <tbody>
        <tr>
            <td><apidoc:link href="/api/query/a5-1?pretty&\$search=communication&highlight" pattern="highlight"/></td>
            <td>Highlighted hits are shwon in field "<samp>@a5.highlight</samp>".</td>
        </tr>
    </tbody>
</apidoc:desctable>

<!-- facets -->
<h3>Facets Aggregation <small>[facets]</small></h3>
<p><samp>GET {scheme}://{server}/api/query/{repository}?<strong>facets={field1[:integer],field2[:integer],...}</strong></samp></p>
<p>Includes facets aggregations for selected fields of variable size. The facets aggregation is contained in
    field "<samp>@a5.facets</samp>" at the bottom of the JSON result.</p>
<apidoc:desctable param="facets">
    <tbody>
        <tr>
            <td><apidoc:link href="/api/query/a5-2?pretty&facets=ObjectLanguage" pattern="facets=ObjectLanguage"/></td>
            <td>Matches all documentes of repository "<samp>a5-2</samp>" and includes a faceted aggregation for
                field "<samp>ObjectLanguage</samp>". The <strong>default</strong> size of the aggregation is <samp>10</samp>.</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/query/a5-2?pretty&facets=ObjectLanguage:5" pattern="facets=ObjectLanguage:5"/></td>
            <td>Matches all documentes of repository "<samp>a5-2</samp>" and includes a faceted aggregation for
                field "<samp>ObjectLanguage</samp>". The size of the aggregation is set to <samp>5</samp>.</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/query/a5-2?pretty&facets=ObjectLanguage:10,Keywords:15" pattern="facets=ObjectLanguage:10,Keywords:15"/></td>
            <td>Matches all documentes of repository "<samp>a5-2</samp>" and includes facets aggregations for
                field "<samp>ObjectLanguage</samp>" and "<samp>Keywords</samp>". The sizes of the aggregations is set to
                <samp>10</samp> and <samp>15</samp>.</td>
        </tr>
    </tbody>
</apidoc:desctable>

<h3>Drilling Down with Facets <small>[drill]</small></h3>
<p><samp>GET {scheme}://{server}/api/query/{repository}?<strong>drill={field1 (eq|ne|gt|ge|lt|le) "term1" (and|or|not) field2 ...}</strong></samp></p>
<p>Drills down the search result according to selected facets. The syntax is the same as for the "<samp>$filter</samp>" option.</p>
<apidoc:desctable param="drill">
    <tbody>
        <tr>
            <td><apidoc:link href='/api/query/a5-2?pretty&\$search=story&facets=ObjectLanguage,Keywords&drill=ObjectLanguage eq "Bali" or ObjectLanguage eq "Vera%27a"' pattern=""/></td>
            <td>Includes facets aggregations for fields "<samp>ObjectLanguage</samp>" and "<samp>Keywords</samp>" and drills down the
                search result to "<samp>ObjectLanguage eq "Bali" or ObjectLanguage eq "Vera'a"</samp>".</td>
        </tr>
    </tbody>
</apidoc:desctable>

<h3>Query String Autocompletion <small>[autocomplete]</small></h3>
<p><samp>GET {scheme}://{server}/api/query/{repository}?<strong>autocomplete</strong></samp></p>
<p>If set, includes a autocompletion aggregation for the current query string.</p>
<apidoc:desctable param="drill">
    <tbody>
        <tr>
            <td><apidoc:link href="/api/query/a5-2?pretty&\$search=story&autocomplete" pattern=""/></td>
            <td>Includes a query string autocompletion aggregation in field "<samp>@a5.autocomplete</samp>" at the bottom of
                the JSON result.</td>
        </tr>
    </tbody>
</apidoc:desctable>

<!-- pretty -->
<h3>Pretty Printing Results <small>[pretty]</small></h3>
<apidoc:desctable param="pretty">
    <tbody>
        <tr>
            <td><apidoc:link href="/api/query/a5-1" pattern=""/></td>
            <td>Prints any JSON response in compact form in format "<samp>application/json</samp>" for production use.</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/query/a5-1?pretty" pattern="pretty"/></td>
            <td>Prints any JSON response in human readable form in format "<samp>text/plain</samp>" for debbuging only.</td>
        </tr>
    </tbody>
</apidoc:desctable>
</body>
</html>