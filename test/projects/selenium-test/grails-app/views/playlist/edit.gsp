
<%@ page import="grails.plugins.selenium.test.Genre; grails.plugins.selenium.test.Song; grails.plugins.selenium.test.Playlist" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'playlist.label', default: 'Playlist')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${playlistInstance}">
            <div class="errors">
                <g:renderErrors bean="${playlistInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${playlistInstance?.id}" />
                <g:hiddenField name="version" value="${playlistInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="playlist.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: playlistInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${playlistInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="songs"><g:message code="playlist.songs.label" default="Songs" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: playlistInstance, field: 'songs', 'errors')}">
                                    <g:select name="songs" from="${grails.plugins.selenium.test.Song.list()}" multiple="yes" optionKey="id" size="5" value="${playlistInstance?.songs}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="active"><g:message code="playlist.active.label" default="Active" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: playlistInstance, field: 'active', 'errors')}">
                                    <g:checkBox name="active" value="${playlistInstance?.active}" />
                                </td>
                            </tr>
                        
							<tr class="prop">
								<td valign="top" class="name">
									<label for="active"><g:message code="playlist.genre.label" default="Genre" /></label>
								</td>
								<td valign="top" class="value ${hasErrors(bean: playlistInstance, field: 'genre', 'errors')}">
									<g:select name="genre" from="${Genre.values()}" value="${playlistInstance?.genre}" noSelection="['':'']" />
								</td>
							</tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
