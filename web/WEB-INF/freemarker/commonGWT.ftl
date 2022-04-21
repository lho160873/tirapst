<#import "spring.ftl" as spring/>
<#macro gwtURL str><@spring.url "/pst.arm.Interactive/${str}"/></#macro>

<#macro css>
<link href="<@gwtURL "themes/alphacube.css"/>" rel="stylesheet" type="text/css"></link>
  		<link href="<@gwtURL "themes/alphacube-off.css"/>" rel="stylesheet" type="text/css"></link>
  		    	    			
		<#--<link href="<@gwtURL "themes/alphacube-blue.css"/>" rel="stylesheet" type="text/css"></link>
		<link href="<@gwtURL "themes/alphacube-blue-off.css"/>" rel="stylesheet" type="text/css"></link>   	
    	<link href="<@gwtURL "themes/overlay.css"/>" rel="stylesheet" type="text/css"></link>-->
</#macro>
<#macro maps>
<script language='javascript' src="http://maps.google.com/maps?file=api&amp;v=2.x&amp;key=ABQIAAAA6k005Q8HxzHfW1quTBpGbBQiJfR8suas9TieyuC6pP83yB5P8BTmXjViCxeONSb9-B_BVwz4Kfc5SA"></script>
</#macro>
<#macro mapScript>

<style type="text/css">
			v\:* {
			 behavior:url(#default#VML);
			}
		</style>
</#macro>

<#macro image imageName>
<@widget "ImageBundle", {"name":"${imageName}"}/>
</#macro>

<#macro widget widgetName, extraParams={}, bootstrap="">
    <script language="JavaScript">
         var Vars = {}                          
    </script>
    <script language='javascript' src='../flash/swfobject.js'></script>     

    <script language="JavaScript">
                Vars['currentModule'] = "${widgetName}"
            <#list extraParams?keys as key> 
                Vars['${key}'] = "${extraParams[key]}"
            </#list>
            <#if bootstrap?has_content>
                <#--Replace \ with \\ and " with \"-->
                Vars['serialized_${widgetName}'] = "${bootstrap.serialized?default("")?replace("\\","\\\\")?replace("\"","\\\"")}"
            </#if>
    </script>

    <#if bootstrap?has_content>
        <noscript>
        ${bootstrap.noscript}
        </noscript>
    </#if>

        <style>
        div.loading {
        position: absolute; left: 45%; top: 40%; padding: 2px;
        margin-left:-45px; z-index:20001; border:1px solid #ccc;
        }
        div.loading .loading-indicator {
        background:#eef;font:bold 13px tahoma,arial,helvetica;
        padding:10px;margin:0;height:auto;color:#444;
        }
        div.loading .loading-indicator img {
        margin-right:8px; float:left; vertical-align:top;
        }
        #loading-msg {font:normal 10px arial,tahoma,sans-serif;}
        </style>
        <div id="gwt-slot-${widgetName}"></div>
        <div id="gwt-loading-${widgetName}" class="loading">
            <div class="loading-indicator">
                <img src="<@spring.url '/images/gxt/shared/large-loading.gif'/>"/>
                 <@spring.message "module.msgLoading"/><br />
                <span id="loading-msg"><nobr><@spring.message "module.msgWait"/></nobr></span>
            </div>
        </div>
        <div id="preload"></div>

</#macro>
<#macro finalize>
    <div id="gwt-status"></div>
    <script language='javascript' src='<@gwt.gwtURL "pst.arm.Interactive.nocache.js"/>'></script>
    <!--<script type='text/javascript' src='https://maps.googleapis.com/maps/api/js?v=3.exp&amp;sensor=false'></script>-->
    <iframe id='__gwt_historyFrame' style='width:0;height:0;border:0'></iframe>
</#macro>