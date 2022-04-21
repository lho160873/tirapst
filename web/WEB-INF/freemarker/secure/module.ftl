<#import "../spring.ftl" as spring/>
<#import "../common.ftl" as common/>
<#import "../commonGWT.ftl" as gwt/>

<html>
    <head>

        <meta name="gwt:property" content="locale=${defaultLocale}">
        <title>
                <@common.pageTitle springMacroRequestContext.getMessage("modules."+moduleName,"")/>
        </title>
        <link rel="stylesheet" type="text/css" href="../css/gxt-all.css" />
        <!--<link rel="stylesheet" type="text/css" href="../css/gxt-gray.css" />-->
        <link rel="stylesheet" type="text/css" href="../css/archives.css" />
        <link rel="stylesheet" type="text/css" href="../css/archives_gray.css" />
    </head>
    <body>
        <#if message?exists>
        <div style="z-index: 99; position: absolute; left: 200px;">
            <div class="message">${message}</div>
        </div>
        </#if>
        <#if moduleName?exists>
	    <@gwt.widget "${moduleName}", config/>
            <@gwt.finalize/>
        </#if>
        <div id="">
        </div><!--end browseWidgets-->
    </body>
</html>