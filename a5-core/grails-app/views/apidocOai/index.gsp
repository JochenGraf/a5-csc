<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="a5"/>
    <title>OAI-PMH</title>
</head>
<body>
<h1>OAI-PMH</h1>
<p class="lead">Open Archives Initiative Protocol for Metadata Harvesting (OAI-PMH) is a low-barrier mechanism for
repository interoperability.</p>
<p>API implements the following standard:</p>
<dl class="dl-horizontal">
    <dt>
        <a target="_blank" href="https://www.openarchives.org/OAI/openarchivesprotocol.html">[OAI-PMH]</a>
    </dt>
    <dd>The Open Archives Initiative Protocol for Metadata Harvesting. Protocol Version 2.0 of 2002-06-14.</dd>
</dl>
<h5>Contents</h5>
<ul id="toc" class="list-unstyled"></ul>
<h2>Requests</h2>
<h3>GetRecord</h3>
<p><samp>GET {scheme}://{server}/api/oai/{repository}?<strong>verb=GetRecord&$identifier={identifier}&metadataPrefix={metadataPrefix}</strong></samp></p>
<p>Request is used to retrieve an individual metadata record from a repository.</p>
<apidoc:reference href="http://www.openarchives.org/OAI/openarchivesprotocol.html#GetRecord"/>
<p>Examples:</p>
<ul>
    <li><apidoc:link href="/api/oai/a5-1?verb=GetRecord&identifier=hdl:11341/00-0000-0000-0000-1BD6-5&metadataPrefix=oai_dc"/></li>
    <li><apidoc:link href="/api/oai/a5-2?verb=GetRecord&identifier=hdl:11341/00-0000-0000-0000-1BD6-5&metadataPrefix=oai_dc"/></li>
</ul>
<h3>Identify</h3>
<p><samp>GET {scheme}://{server}/api/oai/{repository}?<strong>verb=Identify</strong></samp></p>
<p>Request is used to retrieve information about a repository.</p>
<apidoc:reference href="http://www.openarchives.org/OAI/openarchivesprotocol.html#Identify"/>
<p>Examples:</p>
<ul>
    <li><apidoc:link href="/api/oai/a5-1?verb=Identify"/></li>
    <li><apidoc:link href="/api/oai/a5-2?verb=Identify"/></li>
</ul>
<h3>ListIdentifiers</h3>
<p><samp>GET {scheme}://{server}/api/oai/{repository}?<strong>verb=ListIdentifiers&metadataPrefix={metadataPrefix}</strong></samp></p>
<p>Request is an abbreviated form of ListRecords, retrieving only headers rather than records.</p>
<apidoc:reference href="http://www.openarchives.org/OAI/openarchivesprotocol.html#ListIdentifiers"/>
<p>Examples:</p>
<ul>
    <li><apidoc:link href="/api/oai/a5-1?verb=ListIdentifiers&metadataPrefix=oai_dc"/></li>
    <li><apidoc:link href="/api/oai/a5-2?verb=ListIdentifiers&metadataPrefix=oai_dc"/></li>
</ul>
<h3>ListMetadataFormats</h3>
<p><samp>GET {scheme}://{server}/api/oai/{repository}?<strong>verb=ListMetadataFormats</strong></samp></p>
<p>Request is used to retrieve the metadata formats available from a repository.</p>
<apidoc:reference href="http://www.openarchives.org/OAI/openarchivesprotocol.html#ListMetadataFormats"/>
<p>Examples:</p>
<ul>
    <li><apidoc:link href="/api/oai/a5-1?verb=ListMetadataFormats"/></li>
    <li><apidoc:link href="/api/oai/a5-2?verb=ListMetadataFormats"/></li>
</ul>
<h3>ListRecords</h3>
<p>Request is used to harvest records from a repository.</p>
<apidoc:reference href="http://www.openarchives.org/OAI/openarchivesprotocol.html#ListRecords"/>
<p>Examples:</p>
<ul>
    <li><apidoc:link href="/api/oai/a5-1?verb=ListRecords&metadataPrefix=oai_dc"/></li>
    <li><apidoc:link href="/api/oai/a5-2?verb=ListRecords&metadataPrefix=oai_dc"/></li>
</ul>
<h3>ListSets</h3>
<p>Request is used to retrieve the set structure of a repository, useful for selective harvesting.</p>
<apidoc:reference href="http://www.openarchives.org/OAI/openarchivesprotocol.html#ListSets"/>
<p>Not supported.</p>
</body>
</html>