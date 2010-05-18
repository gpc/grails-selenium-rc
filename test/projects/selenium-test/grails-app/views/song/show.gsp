
<%@ page import="musicstore.Song" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'song.label', default: 'Song')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="song.title.label" default="Title" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: songInstance, field: "title")}</td>
                            
                        </tr>
                    
						<tr class="prop">
							<td valign="top" class="name"><g:message code="song.artist.label" default="Artist" /></td>

							<td valign="top" class="value"><g:link controller="artist" action="show" id="${songInstance?.artist?.id}">${songInstance?.artist?.encodeAsHTML()}</g:link></td>

						</tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="song.album.label" default="Album" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: songInstance, field: "album")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="song.durationSeconds.label" default="Duration Seconds" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: songInstance, field: "durationSeconds")}</td>
                            
                        </tr>
                    
						<tr class="prop">
							<td valign="top" class="name"><g:message code="song.genre.label" default="Genre" /></td>

							<td valign="top" class="value"><g:message message="${songInstance?.genre}"/></td>

						</tr>

                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${songInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
