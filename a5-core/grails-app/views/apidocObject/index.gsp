<!DOCTYPE html>
<%@ page import="grails.converters.JSON" %>
<g:set var="apiObjectService" bean="apiObjectService"/>
<html>
<head>
    <meta name="layout" content="a5"/>
    <title>Object API</title>
</head>
<body>
<h1>Object API</h1>
<p class="lead">API for retrieving technical and structural information about data items.</p>
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
<h3>Object Entity</h3>
<% objectUrl = "/api/object/a5-1/Object(hdl:11341/00-0000-0000-0000-1BD6-5)?pretty" %>
<% object = apiObjectService.object([repository: "a5-1", identifier: "hdl:11341/00-0000-0000-0000-1BD6-5"]) as JSON %>
<% object.prettyPrint = true %>
<p>Example: <apidoc:link href="${objectUrl}"/></p>
<pre>${object}</pre>
<ul>
    <li>Fields starting with a "<samp>@</samp>" sign are synthetic fields that cannot be searched, filtered, etc.</li>
    <li>Fields starting with "<samp>@a5.*</samp>" are a5 specific synthetic fields.</li>
    <li>"<samp>@a5.expandable</samp>" contains a list of field names that can be expanded with the "<samp>$expand</samp>" parameter.</li>
    <li>All others are content fields that can be filtered, sorted, etc.</li>
</ul>
<h3>Object Collection</h3>
<% objectsUrl = "/api/object/a5-1/Objects?pretty" %>
<% objects = apiObjectService.objects([repository: "a5-1"]) as JSON %>
<% objects.prettyPrint = true %>
<p>Example: <apidoc:link href="${objectsUrl}"/></p>
<pre>${objects}</pre>
<ul>
    <li>Fields starting with a "<samp>@</samp>" sign are synthetic fields that cannot be searched, filtered, etc.</li>
    <li>Fields starting with "<samp>@a5.*</samp>" are a5 specific synthetic fields.</li>
    <li>All others are content fields that can be filtered, sorted, etc.</li>
    <li>The "<samp>value</samp>" field contains the array of objects.</li>
</ul>

<h2>Requests</h2>
<!-- Object -->
<h3>Object Request</h3>
<p><samp>GET {scheme}://{server}/api/object/{repository}/<strong>Object({$identifier})</strong></samp></p>
<p>Returns a single object.</p>
<p>Example: <apidoc:link href="/api/object/a5-1/Object(hdl:11341/00-0000-0000-0000-1BD6-5)?pretty"/></p>

<!-- $expand -->
<h4>Expanding Related Objects [$expand]</h4>
<p><samp>GET {scheme}://{server}/api/object/{repository}/Object({$identifier})?<strong>$expand=field1,field2</strong></samp></p>
<p>Expands related objects.</p>
<apidoc:desctable param="\$expand=field1,field2">
    <tbody>
        <tr>
            <td><apidoc:link href="/api/object/a5-1/Object(hdl:11341/00-0000-0000-0000-1BD6-5)?pretty&\$expand=parentOf"/></td>
            <td>Replaces all ids found in field "<samp>parentOf</samp>" with the expanded object.</td>
        </tr>
    </tbody>
</apidoc:desctable>

<!-- Objects -->
<h3>Object Collection Request</h3>
<p><samp>GET {scheme}://{server}/api/object/{repository}/<strong>Objects</strong></samp></p>
<p>Returns a collection of objects.</p>
<p>Example: <apidoc:link href="/api/object/a5-1/Objects?pretty"/></p>
<p>By default the first 10 objects are returned. A <samp>@odata.nextLink</samp> property appears if more objects
    are available.</p>

<!-- $filter -->
<h4>Filtering Objects <small>[$filter]</small></h4>
<p><samp>GET {scheme}://{server}/api/object/{repository}/Objects?<strong>$filter={field1 (eq|ne|gt|ge|lt|le) 'term1'
    (and|or|not) field2 (eq|ne|gt|ge|lt|le) 'term2' ...}</strong></samp></p>
<p>Filters objects according to field values.</p>
<ul>
    <li>Filters evaluate exact matches on field values, i.e., "<samp>a term</samp>" does not match "<samp>A Term</samp>".</li>
    <li>A filter is a combination of a field name, a boolean operator "<samp>(eq|ne|gt|ge|lt|le)</samp>", and a term.</li>
    <li>Any number of filters can be combined with the "<samp>and</samp>", "<samp>or</samp>", "<samp>not</samp>" operators
        "<samp>(and|or|not)</samp>".</li>
    <li>(Fields starting with a "<samp>@</samp>" sign are synthetic fields that cannot be used for filtering.)</li>
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
<apidoc:desctable param="\$filter={field1 (eq|ne|gt|ge|lt|le) 'term1' (and|or|not) field2 (eq|ne|gt|ge|lt|le) 'term2' ...}">
    <tbody>
        <tr>
            <td><apidoc:link href="/api/object/a5-1/Objects?pretty&\$filter=id eq hdl:11341/00-0000-0000-0000-1BD6-5"/></td>
            <td>Returns object with id "<samp>hdl:11341/00-0000-0000-0000-1BD6-5</samp>". No quotes are used here to enclose the term.</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/object/a5-1/Objects?pretty&\$filter=contentType eq 'text/eaf%2Bxml'"/></td>
            <td>Returns all objects with content type "<samp>text/eaf+xml</samp>". Note that the plus sign "<samp>+</samp>"
                has to be encoded as "<samp>%2B</samp>".</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/object/a5-1/Objects?pretty&\$filter=parentOf eq 'hdl:11341/00-0000-0000-0000-1BD6-5'"/></td>
            <td>Returns the parent of object "<samp>hdl:11341/00-0000-0000-0000-1BD6-5</samp>". The term is enclosed in
                single quotes here.</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/object/a5-1/Objects?pretty&\$filter=fileCreated lt 2016-10-24T11:31:09.342655Z and contentType eq video/mp4"/></td>
            <td>Returns all objects with files created before "<samp>2016-10-24T11:31:09.342655Z</samp>" and content type "<samp>video/mp4</samp>".</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/object/a5-1/Objects?pretty&\$filter=(contentType eq text/eaf%2Bxml or contentType eq audio/x-wav) and fileExists eq true"/></td>
            <td>Returns all objects with content type equals "<samp>text/eaf+xml</samp>" or "<samp>audio/x-wav</samp>" that property
                "<samp>fileExists</samp>" equals to "<samp>true</samp>".</td>
        </tr>
    </tbody>
</apidoc:desctable>

<!-- $sort -->
<h4>Sorting Objects <small>[$sort]</small></h4>
<p><samp>GET {scheme}://{server}/api/object/{repository}/Objects?<strong>$orderby=field1 (asc|desc),field2 (asc|desc),..</strong></samp></p>
<p>Sorts objects according to selected field values.</p>
<apidoc:desctable param="\$orderby=field1 (asc|desc),field2 (asc|desc),...">
    <tbody>
        <tr>
            <td><apidoc:link href="/api/object/a5-1/Objects?pretty&\$orderby=fileUpdated asc"/></td>
            <td>Returns all objects of repository "<samp>a5-1</samp>" sorted by field "<samp>fileUpdated</samp>" in ascending order.</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/object/a5-1/Objects?pretty&\$orderby=fileCreated desc"/></td>
            <td>Returns all objects of repository "<samp>a5-1</samp>" sorted by field "<samp>fileCreated</samp>" in descending order.</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/object/a5-1/Objects?pretty&\$orderby=fileUpdated asc,fileCreated desc"/></td>
            <td>Returns all objects of repository "<samp>a5-1</samp>" primarily sorted by field "<samp>fileUpdated</samp>" in ascending order
                and secondarily sorted by field "<samp>fileCreated</samp>" in descending order.</td>
        </tr>
    </tbody>
</apidoc:desctable>

<!-- $skip,$top-->
<h4>Paging <small>[$skip&amp;$top]</small></h4>
<p><samp>GET {scheme}://{server}/api/object/{repository}/Objects?<strong>$skip={integer}&$top={integer}</strong></samp></p>
<p>Allows navigation inside a object collection.</p>
<apidoc:desctable param="\$skip={integer}&\$top={integer}">
    <tbody>
        <tr>
            <td><apidoc:link href="/api/object/a5-1/Objects?pretty&\$skip=10&\$top=10"/></td>
            <td>Returns objects 10 to 20.</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/object/a5-1/Objects?pretty&\$skip=20&\$top=1"/></td>
            <td>Returns the 20th object.</td>
        </tr>
    </tbody>
</apidoc:desctable>

<!-- $count -->
<h4>Counting Objects <small>[$count]</small></h4>
<p><samp>GET {scheme}://{server}/api/object/{repository}/Objects?<strong>$count</strong></samp></p>
<p>If <var>true</var> includes number of hits of a object collection request. Default is <var>true</var>.</p>
<apidoc:desctable param="\$count">
    <tbody>
        <tr>
            <td><apidoc:link href="/api/object/a5-1/Objects?pretty&\$count"/></td>
            <td>A "<samp>@odata.count</samp>" parameter appears if "<samp>$count</samp>" is set.</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/object/a5-1/Objects?pretty&\$count&\$top=0"/></td>
            <td>A typical usage is to combine "<samp>$count</samp>" with the "<samp>$top</samp>" parameter set to "<samp>0</samp>".</td>
        </tr>
    </tbody>
</apidoc:desctable>

<!-- $select -->
<h4>Result Serialization <small>[$select]</small></h4>
<p><samp>GET {scheme}://{server}/api/object/{repository}/Objects?<strong>$select={field1,field2,field3,..}</strong></samp></p>
<p>Defines which fields to include in the JSON result.</p>
<apidoc:desctable param="\$select={field1,field2,field3,..}">
    <tbody>
        <tr>
            <td><apidoc:link href="/api/object/a5-1/Objects?pretty&\$select=fileCreated,fileExists"/></td>
            <td>Returns all objects of repository <samp>a5-1</samp> only showing fields <samp>fileCreated</samp> and <samp>fileExists</samp>.</td>
        </tr>
    </tbody>
</apidoc:desctable>

<!-- pretty -->
<h4>Pretty Printing <small>[pretty]</small></h4>
<p><samp>GET {scheme}://{server}/api/object/{repository}/Objects?<strong>pretty</strong></samp></p>
<p>Whether to return the JSON response pretty printed.</p>
<apidoc:desctable param="pretty">
    <tbody>
        <tr>
            <td><apidoc:link href="/api/object/a5-1/Objects"/></td>
            <td>Prints any JSON response in compact form in format "<samp>application/json</samp>" for production use.</td>
        </tr>
        <tr>
            <td><apidoc:link href="/api/object/a5-1/Objects?pretty" pattern="pretty"/></td>
            <td>Prints any JSON response in human readable form in format "<samp>text/plain</samp>" for debbuging only.</td>
        </tr>
    </tbody>
</apidoc:desctable>
</body>
</html>