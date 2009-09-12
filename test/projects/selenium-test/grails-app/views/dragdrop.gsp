<html>
	<head>
		<title>Javascript Tabs</title>
		<meta name="layout" content="main"/>
		<style type="text/css">
		#draggable {
			width: 100px;
			height: 100px;
			padding: 0.5em;
			float: left;
			margin: 10px 10px 10px 0;
		}

		#droppable {
			width: 150px;
			height: 150px;
			padding: 0.5em;
			float: left;
			margin: 10px;
		}
		</style>
		<g:javascript>
			$(function() {
				$("#draggable").draggable();
				$("#droppable").droppable({
					drop: function(event, ui) {
						$(this).addClass('ui-state-highlight').find('p').html('Dropped!');
					}
				});

			});
		</g:javascript>
	</head>
	<body>
		<div class="demo">
			<div id="draggable" class="ui-widget-content">
				<p>Drag me to my target</p>
			</div>
			<div id="droppable" class="ui-widget-header">
				<p>Drop here</p>
			</div>
		</div>
	</body>
</html>