<html>
<#import "/spring.ftl" as spring/>
<#import "common.ftl" as common/>
	<head>
	<#--decorator won't be called on some errors-->
  	<link rel="stylesheet" type="text/css" href="/css/styles.css"/>
	<link rel="stylesheet" type="text/css" href="/css/gxt-gray.css"
	<title>ERROR</title>		
	</head>
	
	
<body>
    		
	   <div id="main">
        <h1>Произошла ошибка!</h1>
	
	
	 <#if message?exists>
		 <div class="message">${message}</div>
	 </#if>

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
					No Error Message found !!!
			</#if>
					
	</#if>

	</div>
	
</body>
</html>