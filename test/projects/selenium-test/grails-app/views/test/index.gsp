<%@ page contentType="text/html;charset=UTF-8" %>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Selenium Obstacle Course</title>
		<g:javascript library="scriptaculous"/>
		<style type="text/css">
		.section {
			border: 1px solid #cccccc;
			margin: 1em;
			padding: 1em;
		}

		img#chuck {
			position: absolute;
			top: 50%;
			left: 50%;
			margin-left: -150px;
		}

		div#drag-drop {
			height: 140px;
			width: 400px;
		}

		div#draggable-box {
			width: 60px;
			height: 60px;
			cursor: move;
			background: #9fcfba;
			border: 1px solid #666;
			text-align: center;
			position: relative;
			top: 30px;
			line-height: 50px;
		}

		div#droppable-box {
			width: 160px;
			height: 120px;
			background: #fff;
			border: 5px solid #ccc;
			text-align: center;
			position: relative;
			top: -60px;
			left: 140px;
			line-height: 100px;
		}

		div#droppable-box.hover {
			border: 5px dashed #aaa;
			background: #efefef;
		}
		</style>
	</head>
	<body>
		<div class="body">
			<h1>Selenium Obstacle Course</h1>

			<div class="text-para section">
				<h2>Example Text</h2>
				<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>
			</div>

			<div class="ajax-alert section">
				<h2>AJAX Alert</h2>
				<p><input type="button" onclick="${remoteFunction(action: 'alert', update: 'ajax-alert-target', onLoading: 'showSpinner();')}" value="Click me"/></p>
				<p id="ajax-alert-target"></p>
			</div>

			<div class="ajax-message section">
				<h2>AJAX Message</h2>
				<p><input type="button" onclick="${remoteFunction(action: 'message', update: 'ajax-message-target', onLoading: 'showSpinner();')}" value="Click me"/></p>
				<p id="ajax-message-target"></p>
			</div>

			<div class="show-and-hide section">
				<h2>Show &amp; Hide</h2>
				<p>
					<input type="button" onclick="Effect.Appear('chuck')" value="Show" class="show"/>
					<input type="button" onclick="Effect.Fade('chuck')" value="Hide" class="hide"/>
				</p>
				<img src="/images/thumb_up.jpg" width="300" height="253" id="chuck" style="display: none"/>
			</div>

			<div class="sortable section">
				<h2>Sortable</h2>
				<ul id="sortable-list">
					<li>Item 1</li>
					<li>Item 2</li>
					<li>Item 3</li>
				</ul>
				<g:javascript>Sortable.create('sortable-list');</g:javascript>
			</div>

			<div class="drag-drop section">
				<h2>Drag &amp; Drop</h2>
				<div id="draggable-box" class="draggable">Drag me!</div>
				<div id="droppable-box">Drop here!</div>
				<g:javascript>
					new Draggable('draggable-box', {
						revert: true
					});

					Droppables.add('droppable-box', {
						accept: 'draggable',
						hoverclass: 'hover',
						onDrop: function() {
							$('droppable-box').highlight();
							$('droppable-box').textContent = 'Win!'
						}
					});
				</g:javascript>
			</div>
		</div>
	</body>
</html>