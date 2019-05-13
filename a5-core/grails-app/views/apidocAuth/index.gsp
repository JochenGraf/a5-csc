<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="a5"/>
    <title>Auth API</title>
</head>

<body>
<h1>Auth API</h1>
<h5>Contents</h5>
<ul id="toc" class="list-unstyled"></ul>

<h2>Media Access</h2>

<p>The implementation of authorization restricts the access to all resources per default.</p>

<p>The access rights are read from the table access_rights of database groupDataSource</p>

<p>If a resource has an entry with username=open, then everyone has the permissions associated with this resource.</p>

<p>Otherwise, permissions are obtained from the bitmask ACL value.</p>

<p>These permissions are READ, WRITE, CREATE, DELETE, ADMINISTER.</p>

<p><strong>Users can only SEE objects if their <i>username</i> is associated with a READ permission</strong></p>

<p><strong>Users can only MANIPULATE objects if their <i>username</i> is associated with a WRITE permission</strong></p>

<p>If an unauthenticated user tries to access a non-public resource, he receives a 401 status response.</p>

<p>If an authenticated user tries to access a resource without authorization, he receives a 403 status response.</p>

<h2>Custom KA3 Access Rights</h2>

<p><samp>GET {scheme}://{server}/{contextPath}/auth/isOwner?username={username}&pid={identifier}</samp></p>

<h3>Verification methods</h3>

<p>Returns the Access rights of a user associated with an identifier.</p>
<apidoc:desctable>
    <tbody>
    <tr>
        <td><apidoc:link href="/auth/isConsumer?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="isConsumer"/></td>
        <td>Returns true if the  <strong>user</strong> <i>gtest@uni-koeln.de</i> is a CONSUMER (can Read AND Write (manipulate)/acl Flag 1 and 2 true) object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>".
        </td>
    </tr>
    <tr>
        <td><apidoc:link href="/auth/isManager?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="isManager"/></td>
        <td>Returns true if the  <strong>user</strong> <i>gtest@uni-koeln.de</i> is a MANAGER (can Create and Delete/acl Flag 8 and 4 are true) of object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>".
        </td>
    </tr>
    <tr>
        <td><apidoc:link href="/auth/isOwner?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="isOwner"/></td>
        <td>Returns true if the  <strong>user</strong> <i>gtest@uni-koeln.de</i> OWNS (can Administer/acl Flag 16 is true) object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>".
        </td>
    </tr>
    </tbody>
</apidoc:desctable>

<h3>Granting Methods</h3>

<p>Grants Access rights profiles regarding an identifier to a user.</p>

<h4>(requires object administration rights)</h4>
<apidoc:desctable>
    <tbody>
    <tr>
        <td><apidoc:link href="/auth/addConsumer?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="addConsumer"/></td>
        <td>Grants the  <strong>user</strong> <i>gtest@uni-koeln.de</i> CONSUMER (Read and Write (manipulate)/acl Flag 1 and 2 are true) permissions for the object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>".
            <br/><strong>(requires Manager rights)</strong></td>
    </tr>
    <tr>
        <td><apidoc:link href="/auth/addManager?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="addManager"/></td>
        <td>Grants the  <strong>user</strong> <i>gtest@uni-koeln.de</i> MANAGER (Create and Delete/acl Flag 8 and 4 are true) permissions for the object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>".
            <br/><strong>(requires Owner rights)</strong></td>
    </tr>
    <tr>
        <td><apidoc:link href="/auth/addOwner?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="addOwner"/></td>
        <td>Assigns <strong>user</strong> <i>gtest@uni-koeln.de</i> the OWNERSHIP of object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>".
            <br/><strong>(requires ROLE_SAML_ADMIN)</strong>
            <br/><strong>(Only one user may administer each object. Throws error if the object already has an owner)</strong>
        </td>
    </tr>
    </tbody>
</apidoc:desctable>

<apidoc:desctable>
    <tbody>
    <tr>
        <td><apidoc:link href="/auth/publish?pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="publish"/></td>
        <td>Makes the object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp> PUBLIC, accessible to everyone, even without authentification. USE WITH CARE".
            <br/><strong>(requires Manager rights)</strong></td>
    </tr>
    <tr>
        <td><apidoc:link href="/auth/unpublish?pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="unpublish"/></td>
        <td>Makes the object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp> inaccessible to everyone without authentification and explicit access (default state)".
            <br/><strong>(requires Manager rights)</strong></td>
    </tr>
    </tbody>
</apidoc:desctable>

<h3>Revoking Methods</h3>

<p>Revokes Access rights profiles regarding an identifier to a user.</p>

<h4>(requires object administration rights)</h4>
<apidoc:desctable>
    <tbody>
    <tr>
        <td><apidoc:link href="/auth/removeConsumer?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="removeConsumer"/></td>
        <td>Revokes the  <strong>user</strong> <i>gtest@uni-koeln.de</i> CONSUMER (Read and Write (manipulate)/acl Flag 1 and 2 are set to false) permissions for the object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>".
            <br/><strong>(requires Manager rights)</strong></td>
    </tr>
    <tr>
        <td><apidoc:link href="/auth/removeManager?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="removeManager"/></td>
        <td>Revokes the  <strong>user</strong> <i>gtest@uni-koeln.de</i> MANAGER (Create and Delete/acl Flag 8 and 4 are set to false) permissions for the object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>".
            <br/><strong>(requires Owner rights)</strong></td>
    </tr>
    <tr>
        <td><apidoc:link href="/auth/removeOwner?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="removeOwner"/></td>
        <td>Assigns <strong>user</strong> <i>gtest@uni-koeln.de</i> the OWNERSHIP of object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>".
            <br/><strong>(requires ROLE_SAML_ADMIN)</strong>
            <br/><strong>(Only one user may administer each object. Throws error if the object already has an owner)</strong>
        </td>
    </tr>
    </tbody>
</apidoc:desctable>

<h2>Individual Access Rights Administration</h2>

<h3>Verification methods</h3>

<p><samp>GET {scheme}://{server}/{contextPath}/auth/canRead?username={username}&pid={identifier}</samp></p>

<p>Returns the Access rights of a user associated with an identifier.</p>

<p>The bitmask permissions are: read (bit 0), write (bit 1), create (bit 2), delete (bit 3) and administer (bit 4)</p>
<apidoc:desctable>
    <tbody>
    <tr>
        <td><apidoc:link href="/auth/canRead?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="canRead"/></td>
        <td>Returns true if the  <strong>user</strong> <i>gtest@uni-koeln.de</i> can <i>READ</i> (acl Flag 1/bit 0 is true) object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>".
        </td>
    </tr>
    <tr>
        <td><apidoc:link href="/auth/canWrite?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="canWrite"/></td>
        <td>Returns true if the <strong>user</strong> <i>gtest@uni-koeln.de</i> can <i>WRITE</i> (acl Flag 2/bit 1 is true) object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>".
        </td>
    </tr>
    <tr>
        <td><apidoc:link href="/auth/canCreate?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="canCreate"/></td>
        <td>Returns true if the  <strong>user</strong> <i>gtest@uni-koeln.de</i> can <i>CREATE</i> (acl Flag 4/bit 2 is true) object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>".
        </td>
    </tr>
    <tr>
        <td><apidoc:link href="/auth/canDelete?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="canDelete"/></td>
        <td>Returns true if the  <strong>user</strong> <i>gtest@uni-koeln.de</i> can <i>DELETE</i> (acl Flag 8/bit 3 is true) object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>".
        </td>
    </tr>
    <tr>
        <td><apidoc:link href="/auth/canAdminister?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="canAdminister"/></td>
        <td>Returns true if the  <strong>user</strong> <i>gtest@uni-koeln.de</i> can <i>ADMINISTER</i> (acl Flag 16/bit 4 is true) object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>".
        </td>
    </tr>
    </tbody>
</apidoc:desctable>

<h3>Granting Methods</h3>
<p>Grants the user specific object permissions.</p>
<h4>(requires object administration rights)</h4>
<apidoc:desctable>
    <tr>
        <td><apidoc:link href="/auth/allowRead?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="allowRead"/></td>
        <td>Grants <strong>user</strong> <i>gtest@uni-koeln.de</i> permission to READ the object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>".
            <br/>Returns <strong>true</strong> if the entry was successfully added</td>
    </tr>
    <tr>
        <td><apidoc:link href="/auth/allowWrite?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="allowWrite"/></td>
        <td>Grants <strong>user</strong> <i>gtest@uni-koeln.de</i> permission to WRITE (modify/manipulate) the object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>".
            <br/>Returns <strong>true</strong> if the entry was successfully added</td>
    </tr>
    <tr>
        <td><apidoc:link href="/auth/allowCreate?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="allowCreate"/></td>
        <td>Grants <strong>user</strong> <i>gtest@uni-koeln.de</i> permission to CREATE objetcts with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>".
            <br/>Returns <strong>true</strong> if the entry was successfully added</td>
    </tr>
    <tr>
        <td><apidoc:link href="/auth/allowDelete?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="allowDelete"/></td>
        <td>Grants <strong>user</strong> <i>gtest@uni-koeln.de</i> permission to DELETE the object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>".
            <br/>Returns <strong>true</strong> if the entry was successfully added</td>
    </tr>
    <tr>
        <td><apidoc:link href="/auth/allowAdminister?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="allowAdminister"/></td>
        <td>Grants <strong>user</strong> <i>gtest@uni-koeln.de</i> permission to ADMINISTER the object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>"
            <br/><strong>(requires ROLE_SAML_ADMIN)</strong>.
            <br/>Returns <strong>true</strong> if the entry was successfully added.
            <br/><strong>(Only one user may administer each object. Throws error if the object already has an administrator)</strong>
        </td>
    </tr>

</apidoc:desctable>

<h3>Revoking Methods</h3>
<p>Revokes the user specific object permissions.</p>
<h4>(requires object administration rights)</h4>
<apidoc:desctable>
    <tbody>
    <tr>
        <td><apidoc:link href="/auth/denyAll?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="denyAll"/></td>
        <td>Revokes <strong>user</strong> <i>gtest@uni-koeln.de</i> ALL permissions for the object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>".
            <br/>Returns <strong>true</strong> if the entry was successfully added</td>
    </tr>    <tr>
        <td><apidoc:link href="/auth/denyRead?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="denyRead"/></td>
        <td>Revokes <strong>user</strong> <i>gtest@uni-koeln.de</i> the permission to READ the object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>".
            <br/>Returns <strong>true</strong> if the entry was successfully added</td>
    </tr>
    <tr>
        <td><apidoc:link href="/auth/denyWrite?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="denyWrite"/></td>
        <td>Revokes <strong>user</strong> <i>gtest@uni-koeln.de</i> the permission to WRITE (modify/manipulate) the object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>".
            <br/>Returns <strong>true</strong> if the entry was successfully added</td>
    </tr>
    <tr>
        <td><apidoc:link href="/auth/denyCreate?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="denyCreate"/></td>
        <td>Revokes <strong>user</strong> <i>gtest@uni-koeln.de</i> the permission to CREATE objetcts with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>".
            <br/>Returns <strong>true</strong> if the entry was successfully added</td>
    </tr>
    <tr>
        <td><apidoc:link href="/auth/denyDelete?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="denyDelete"/></td>
        <td>Revokes <strong>user</strong> <i>gtest@uni-koeln.de</i> the permission to DELETE the object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>".
            <br/>Returns <strong>true</strong> if the entry was successfully added</td>
    </tr>
    <tr>
        <td><apidoc:link href="/auth/denyAdminister?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="denyAdminister"/></td>
        <td>Revokes <strong>user</strong> <i>gtest@uni-koeln.de</i> the permission to ADMINISTER the object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>"
            <br/><strong>(requires ROLE_SAML_ADMIN)</strong>.
            <br/>Returns <strong>true</strong> if the entry was successfully added</td>
    </tr>

    </tbody>
</apidoc:desctable>

<h3>Set ACL directly (requires ROLE_SAML_ADMIN, sets ACL forcefully USE WITH CAUTION)</h3>
<p><samp>GET {scheme}://{server}/auth/setAcl?username={username}&pid={identifier}&acl={bitmask}</samp></p>

<p><samp>POST {scheme}://{server}/auth/setAcl username:{username}&pid:{identifier}&acl:{bitmask}</samp></p>

<p>Creates a new bitmask (acl value) associated to a user and an identifier. The bitmask permissions are: read (bit 0), write (bit 1), create (bit 2), delete (bit 3) and administer (bit 4)</p>


<apidoc:desctable>
    <tbody>
    <tr>
        <td><apidoc:link href="/auth/setAcl?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1&acl=31"
                         pattern="setAcl"/></td>
    <td>Grants <strong>user</strong> <i>gtest@uni-koeln.de</i> permission to READ, WRITE, CREATE, DELETE and ADMINISTER the object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>"
        <br/><strong>(requires ROLE_SAML_ADMIN)</strong>.
        <br/>Returns <strong>true</strong> if the entry was successfully added</td>
    </tr>
    <tr>
        <td><apidoc:link href="/auth/setAcl?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1&acl=1"
                         pattern="setAcl"/></td>
        <td>Grants <strong>user</strong> <i>gtest@uni-koeln.de</i> permission to READ, but not WRITE, CREATE, DELETE nor ADMINISTER the object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>"
            <br/><strong>(requires ROLE_SAML_ADMIN)</strong>.
            <br/>Returns <strong>true</strong> if the entry was successfully added</td>
    </tr>
    <tr>
        <td><apidoc:link href="/auth/setAcl?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1&acl=2"
                         pattern="setAcl"/></td>
        <td>Grants <strong>user</strong> <i>gtest@uni-koeln.de</i> permission to WRITE, but not READ, CREATE, DELETE nor ADMINISTER the object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>"
            <br/><strong>(requires ROLE_SAML_ADMIN)</strong>.
            <br/>Returns <strong>true</strong> if the entry was successfully added</td>
    </tr>
</apidoc:desctable>

<h3>Get ACL directly (requires ROLE_SAML_ADMIN, gets complete ACL mask as integer)</h3>
<p><samp>GET {scheme}://{server}/auth/getAcl?username={username}&pid={identifier}</samp></p>

<p><samp>POST {scheme}://{server}/auth/getAcl username:{username}&pid:{identifier}</samp></p>

<p>Retrieves bitmask (acl value) associated to a user and an identifier. The bitmask permissions are: read (bit 0), write (bit 1), create (bit 2), delete (bit 3) and administer (bit 4)</p>


<apidoc:desctable>
    <tbody>
    <tr>
        <td><apidoc:link href="/auth/getAcl?username=gtest@uni-koeln.de&pid=pid:12345/00-1234-5678-9A1B-CDEF-1"
                         pattern="getAcl"/></td>
    <td>Retrieves the access rights of the <strong>user</strong> <i>gtest@uni-koeln.de</i> to the object with ID"<samp>pid:12345/00-1234-5678-9A1B-CDEF-1</samp>"
        <br/><strong>(requires ROLE_SAML_ADMIN)</strong>.
        <br/>Returns an <strong>Integer</strong> between 0 and 31 representing the access rights if an ACL entry exists for the username and ID
    </td>
    </tr>
</apidoc:desctable>
</body>
</html>