
<%@ page import="musicstore.Song" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'song.label', default: 'Song')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'song.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="title" title="${message(code: 'song.title.label', default: 'Title')}" />
                        
							<th><g:message code="song.artist.label" default="Artist" /></th>

                            <g:sortableColumn property="album" title="${message(code: 'song.album.label', default: 'Album')}" />
                        
                            <g:sortableColumn property="genre" title="${message(code: 'song.genre.label', default: 'Genre')}" />
                        
                            <g:sortableColumn property="durationSeconds" title="${message(code: 'song.durationSeconds.label', default: 'Duration Seconds')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${songInstanceList}" status="i" var="songInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${songInstance.id}">${fieldValue(bean: songInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: songInstance, field: "title")}</td>
                        
							<td>${fieldValue(bean: songInstance, field: "artist")}</td>

                            <td>${fieldValue(bean: songInstance, field: "album")}</td>
                        
                            <td>${fieldValue(bean: songInstance, field: "genre")}</td>
                        
                            <td>${fieldValue(bean: songInstance, field: "durationSeconds")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${songInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
