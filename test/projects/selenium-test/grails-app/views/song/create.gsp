<%@ page import="musicstore.Genre" %>
<%@ page import="musicstore.Song" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="layout" content="main"/>
		<g:set var="entityName" value="${message(code: 'song.label', default: 'Song')}"/>
		<title><g:message code="default.create.label" args="[entityName]"/></title>
		<g:javascript library="scriptaculous"/>
		<g:javascript>
			Event.observe(window, 'load', function(event) {
				new Ajax.Autocompleter('artist', 'artist_autocomplete_choices', '<g:createLink controller="artist" action="lookup"/>', {});
			});
		</g:javascript>
	</head>
	<body>
		<div class="nav">
			<span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
			<span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]"/></g:link></span>
		</div>
		<div class="body">
			<h1><g:message code="default.create.label" args="[entityName]"/></h1>
			<g:if test="${flash.message}">
				<div class="message">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${songInstance}">
				<div class="errors">
					<g:renderErrors bean="${songInstance}" as="list"/>
				</div>
			</g:hasErrors>
			<g:form action="save" method="post">
				<div class="dialog">
					<bean:input beanName="songInstance" property="title"/>
					<bean:customField beanName="songInstance" property="artist">
						<g:textField name="artist" value="${artistInstance?.artist?.name}"/>
						<div id="artist_autocomplete_choices" class="autocomplete"></div>
					</bean:customField>
					<bean:input beanName="songInstance" property="album"/>
					<bean:input beanName="songInstance" property="durationSeconds"/>
					<bean:select beanName="songInstance" property="genre" from="${Genre.values()}" optionValue="defaultMessage" noSelection="['':'']"/>
					<bean:checkBox beanName="songInstance" property="partOfCompilation" value="true"/>
				</div>
				<div class="buttons">
					<span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}"/></span>
				</div>
			</g:form>
		</div>
	</body>
</html>
