<#import "../common_simple.ftl" as common/>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../css/gxt-all.css" />
        <link rel="stylesheet" type="text/css" href="../css/gxt-gray.css" />
        <title>Welcome to Spring Web MVC project</title>
        <!--<meta name="gwt:property" content="locale=ru_RU">-->
    </head>

    <body>
        <p>Hello! This is the default welcome page for a Spring Web MVC project. ${moduleName}</p> <@common.pageTitle />
        <script type="text/javascript"  src="../pst.arm.Interactive/pst.arm.Interactive.nocache.js"></script>

        <div id="loginBox">
                   <@common.loginForm/>
        </div>
    </body>
</html>
