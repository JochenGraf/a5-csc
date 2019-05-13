<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="a5"/>
		<title>Login</title>
	</head>
	<body>
        <div class="container">
<sec:ifNotLoggedIn>
    <div class="panel panel-default">
                <div class="panel-heading">
                    <h2>Please login</h2>
                </div>
                <div class="panel-body">
                    <form class="form-horizontal" action="${request.contextPath}/login/authenticate?lang=en"
                            method="POST" id="loginForm">
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="username">KA3 Account Name:</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="username" id="username"
                                        placeholder="KA3 Account Name">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="password">Password:</label>
                            <div class="col-sm-10">
                                <input type="password" class="form-control" name="password" id="password"
                                        placeholder="Password">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <input class="btn btn-primary" type="submit" id="submit" value="Sign in">
                            </div>
                        </div>
                    </form>
                    <div>
                            %{--<g:button class="btn btn-primary" value="Shibboleth Sign-On"/>--}%
                        <g:link uri="/samlLogin" style="align-self: right" class="btn btn-success">Uzk/other Institutional Sign in (SAML/Shibboleth)</g:link>
                    </div>
                </div>
                <div class="panel-footer">
                    <g:if test="${flash.message}">
                        <div class="login_message text-danger">${flash.message}</div>
                    </g:if>
                </div>
            </div>
</sec:ifNotLoggedIn>

<sec:ifLoggedIn>
    <meta http-equiv="refresh" content="1;URL=${request.contextPath}/index"/>
</sec:ifLoggedIn>

        </div>
	</body>
</html>