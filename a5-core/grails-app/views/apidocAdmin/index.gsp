<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="a5"/>
    <title>Admin API</title>
</head>
<body>
<h1>Admin API</h1>
<h5>Contents</h5>
<ul id="toc" class="list-unstyled"></ul>
<%--
<h2>Institution Administration</h2>
<h3>Institution CRUD Requests</h3>
<p><samp>@Secured("ROLE_ADMIN")</samp></p>
<div class="table-responsive">
    <table class="table table-striped">
        <thead>
        <tr>
            <th>Method</th>
            <th>URL</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>CREATE</td>
            <td><samp>{scheme}://{server}/api/admin/institution</samp></td>
            <td></td>
        </tr>
        <tr>
            <td>GET</td>
            <td><samp>{scheme}://{server}/api/admin/institution/{id}</samp></td>
            <td></td>
        </tr>
        <tr>
            <td>UPDATE</td>
            <td><samp>{scheme}://{server}/api/admin/institution/{id}</samp></td>
            <td></td>
        </tr>
        <tr>
            <td>DELETE</td>
            <td><samp>{scheme}://{server}/api/admin/institution/{id}</samp></td>
            <td></td>
        </tr>
        </tbody>
    </table>
</div>
<h2>Repository Administration</h2>
<h3>Repository CRUD Requests</h3>
<h3>Repository Configuration Requests</h3>
<p><samp>GET {scheme}://{server}/api/admin/{repository}/config</samp></p>
<p>Returns or updates a repositories' base configuration.</p>
<apidoc:desctable>
    <tbody>
    <tr>
        <td><apidoc:link href="/api/admin/a5-1/config?pretty" pattern="a5-1"/></td>
        <td>Returns the index configuration of repository "<samp>a5-1</samp>".</td>
    </tr>
    <tr>
        <td><apidoc:link href="/api/admin/a5-2/config?pretty" pattern="a5-2"/></td>
        <td>Returns the index configuration of repository "<samp>a5-1</samp>".</td>
    </tr>
    </tbody>
</apidoc:desctable>
--%>
<h2>Index Administration</h2>
<h3>Index Configuration Request</h3>
<p><samp>GET {scheme}://{server}/api/admin/{repository}/index/config</samp></p>
<p>Returns the index configuration of a repository.</p>
<apidoc:link href="/api/admin/a5-1/index/config"/>
<h3>Reindex Request</h3>
<p><samp>GET {scheme}://{server}/api/admin/{repository}/reindex</samp></p>
<p>Drops and rebuilds the <strong>Object Index</strong>, <strong>Query Index</strong> and <strong>Annotation Index</strong> of a repository.</p>
<apidoc:link href="/api/admin/a5-1/reindex"/>
<h3>Reindex Log Request</h3>
<p><samp>GET {scheme}://{server}/api/admin/{repository}/reindex/log</samp></p>
<p>Shows logging information of the last reindex.</p>
<apidoc:link href="/api/admin/a5-1/reindex/log"/>
<h3>Reindex Debug Request</h3>
<p><samp>GET {scheme}://{server}/api/admin/{repository}/mapping/query/{prefix}/{identifier}</samp></p>
<p>Utility request for debugging the query index with one document. Useful when .</p>
<apidoc:link href="/api/admin/a5-1/index/query/hdl:11341/00-0000-0000-0000-1BF6-F?pretty"/>

</body>
</html>