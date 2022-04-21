<html>
    <#import "spring.ftl" as spring/>
    <#import "common.ftl" as common/>
    <head>
        <#--decorator won't be called on some errors-->
        <link rel="stylesheet" type="text/css" href="/css/styles.css"/>

        <title><@common.pageTitle "Ошибка"/></title>
    </head>


    <body>
    <div id="main">
        <h1>Произошла ошибка</h1>

        <#if message?exists>
            <div class="message">${message}</div>
        </#if>

	Пожалуйста, свяжитесь с системным администратором.
        Спасибо!

        <#if exception?exists>
            ${exception}
        <#list exception.stackTrace as st>
            ${st}
        </#list>
        <#else>
        <#if javax?exists && javax.servlet?exists && javax.servlet.error?exists && javax.servlet.error.exception?exists>
				Servlet Exception:<p>				
					${javax.servlet.error.exception} <BR>
					${javax.servlet.error.exception.message?default("")} <BR>
            <#list javax.servlet.error.exception.stackTrace as st>
					${st}<BR>
            </#list>

            <#else>
					Текст ошибки отсутствует !!!
            </#if>

            </#if>

    </div>

</body>
</html>