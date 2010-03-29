
<%@ page import="grails.plugins.selenium.test.Playlist" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'playlist.label', default: 'Playlist')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'playlist.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'playlist.name.label', default: 'Name')}" />
                        
                            <th><g:message code="playlist.songs.label" default="Songs" /></th>
                   	    
                            <g:sortableColumn property="active" title="${message(code: 'playlist.active.label', default: 'Active')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${playlistInstanceList}" status="i" var="playlistInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${playlistInstance.id}">${fieldValue(bean: playlistInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: playlistInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: playlistInstance, field: "songs")}</td>
                        
                            <td><g:formatBoolean boolean="${playlistInstance.active}" /></td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${playlistInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
