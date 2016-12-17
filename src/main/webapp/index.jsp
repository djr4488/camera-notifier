<%--
  Created by IntelliJ IDEA.
  User: djr4488
  Date: 12/15/16
  Time: 9:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Swagger UI</title>
    <link rel="icon" type="image/png" href="webjars/swagger-ui/2.2.6/images/favicon-32x32.png" sizes="32x32" />
    <link rel="icon" type="image/png" href="webjars/swagger-ui/2.2.6/images/favicon-16x16.png" sizes="16x16" />
    <link href='webjars/swagger-ui/2.2.6/css/typography.css' media='screen' rel='stylesheet' type='text/css'/>
    <link href='webjars/swagger-ui/2.2.6/css/reset.css' media='screen' rel='stylesheet' type='text/css'/>
    <link href='webjars/swagger-ui/2.2.6/css/screen.css' media='screen' rel='stylesheet' type='text/css'/>
    <link href='webjars/swagger-ui/2.2.6/css/reset.css' media='print' rel='stylesheet' type='text/css'/>
    <link href='webjars/swagger-ui/2.2.6/css/print.css' media='print' rel='stylesheet' type='text/css'/>

    <script src='webjars/swagger-ui/2.2.6/lib/object-assign-pollyfill.js' type='text/javascript'></script>
    <script src='webjars/swagger-ui/2.2.6/lib/jquery-1.8.0.min.js' type='text/javascript'></script>
    <script src='webjars/swagger-ui/2.2.6/lib/jquery.slideto.min.js' type='text/javascript'></script>
    <script src='webjars/swagger-ui/2.2.6/lib/jquery.wiggle.min.js' type='text/javascript'></script>
    <script src='webjars/swagger-ui/2.2.6/lib/jquery.ba-bbq.min.js' type='text/javascript'></script>
    <script src='webjars/swagger-ui/2.2.6/lib/handlebars-4.0.5.js' type='text/javascript'></script>
    <script src='webjars/swagger-ui/2.2.6/lib/lodash.min.js' type='text/javascript'></script>
    <script src='webjars/swagger-ui/2.2.6/lib/backbone-min.js' type='text/javascript'></script>
    <script src='webjars/swagger-ui/2.2.6/swagger-ui.js' type='text/javascript'></script>
    <script src='webjars/swagger-ui/2.2.6/lib/highlight.9.1.0.pack.js' type='text/javascript'></script>
    <script src='webjars/swagger-ui/2.2.6/lib/highlight.9.1.0.pack_extended.js' type='text/javascript'></script>
    <script src='webjars/swagger-ui/2.2.6/lib/jsoneditor.min.js' type='text/javascript'></script>
    <script src='webjars/swagger-ui/2.2.6/lib/marked.js' type='text/javascript'></script>
    <script src='webjars/swagger-ui/2.2.6/lib/swagger-oauth.js' type='text/javascript'></script>
</head>

<body class="swagger-section">
<div id="message-bar" class="swagger-ui-wrap">&nbsp;</div>
<div id="swagger-ui-container" class="swagger-ui-wrap"></div>

<script type="text/javascript">
    $(function () {
        new SwaggerUi({
            url: '<%= application.getContextPath() %>/api/swagger.json',
            dom_id:"swagger-ui-container",
            sorter : 'alpha'
        }).load();
    });
</script>
</body>
</html>
