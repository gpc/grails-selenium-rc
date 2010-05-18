<html>
	<head>
		<title><g:layoutTitle default="Grails"/></title>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}"/>
		<link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon"/>
		<g:layoutHead/>
		<g:javascript library="application"/>
	</head>
	<body>
		<div id="spinner" class="spinner" style="display:none;">
			<img src="${resource(dir: 'images', file: 'spinner.gif')}" alt="Spinner"/>
		</div>
		<div id="grailsLogo" class="logo"><a href="http://grails.org"><img src="${resource(dir: 'images', file: 'grails_logo.png')}" alt="Grails" border="0"/></a></div>
		<div class="loginInfo">
			<g:isLoggedIn><g:message code="label.logged.in.user" args="[loggedInUserInfo(field: 'username')]" default="Logged in as {0}"/></g:isLoggedIn>
			<g:isNotLoggedIn><g:message code="label.not.logged.in" default="Not logged in"/></g:isNotLoggedIn>
		</div>
		<g:layoutBody/>
	</body>
</html>