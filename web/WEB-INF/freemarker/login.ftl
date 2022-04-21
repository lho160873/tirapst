<#import "spring.ftl" as spring/>
<#import "common.ftl" as common/>
<html>
    <head>
        <#if access_error?exists>
            <link href="../css/gxt-all.css" type="text/css" rel="stylesheet">
            <!--<link href="./css/gxt-gray.css" type="text/css" rel="stylesheet">-->
            <link href="../css/archives.css" type="text/css" rel="stylesheet">
            <link href="../css/archives_gray.css" type="text/css" rel="stylesheet">
        <#else>
            <link href="./css/gxt-all.css" type="text/css" rel="stylesheet">
            <!--<link href="./css/gxt-gray.css" type="text/css" rel="stylesheet">-->
            <link href="./css/archives.css" type="text/css" rel="stylesheet">
            <link href="./css/archives_gray.css" type="text/css" rel="stylesheet">
        </#if>
        <title><@common.pageTitle "Вход в систему"/></title>

    </head>

    <body body class=" ext-gecko ext-gecko3 ext-windows" style="overflow: hidden;" style="text-align: center;" onload="document.forms[0].elements[0].focus();">
    <form id="upForm" class="" action="<@spring.url "/j_acegi_security_check"/>" method="POST">
    <div id="outerlogin">
        <div id="innerlogin">
            <div style="width: 350px; height: 200px; display: block; margin-left: auto; margin-right: auto;">
                <div class="x-panel-tl">
                    <div class="x-panel-tr">
                        <div class="x-panel-tc">
                            <div unselectable="on" id="x-auto-7" class=" x-small-editor x-panel-header x-unselectable">
                                <div class=" x-panel-toolbar" id="x-auto-6" style="overflow: visible; float: right;">
                                    <table cellpadding="0" cellspacing="0">
                                        <tbody>
                                            <tr>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <span class="x-panel-header-text" id="x-auto-7-label"><@spring.message "login.title"/></span></div>
                        </div>
                    </div>
                </div>
                <div class="x-panel-bwrap">
                    <div class="x-panel-ml">
                        <div class="x-panel-mr">
                            <div class="x-panel-mc">
                                <div style="background: transparent none repeat scroll 0% 0%; -moz-background-clip: border;
                                    -moz-background-origin: padding; -moz-background-inline-policy: continuous;"
                                    class="x-panel-body">
                                    <!--<form class=" x-form-label-left" action="http://www.ya.ru" target="gxt.formpanel-1"
                                    method="post" style="padding: 10px; overflow: hidden; width: 318px;">-->
                                    <div class="x-form-item " tabindex="-1">
                                        <label for="x-auto-14" style="width: 80px;" class="x-form-item-label">
                                        </label>
                                        <div class="x-form-element" id="x-form-el-x-auto-14" style="padding-left: 85px;">
                                            <div style="color: red; width: 220px;" class=" x-form-label" id="x-auto-14">
                                                <#if login_error?has_content>
                                                    <div class="message" style="color: red;  padding-left: 15px;">${login_error}<br/></div>
                                                </#if>
                                                <#if message?exists>
                                                    <div class="message">${message}</div>
                                                </#if>
                                            </div>
                                        </div>
                                        <div class="x-form-clear-left">
                                        </div>
                                    </div>
                                    <div class="x-form-item " tabindex="-1">
                                        <label for="x-auto-15-input" style="width: 80px; padding-left: 10px;" class="x-form-item-label">
                                            <@spring.message "login.user"/>:</label><div class="x-form-element" id="x-form-el-x-auto-15" style="padding-left: 100px;">
                                                <div style="width: 223px;" id="x-auto-15" class=" x-form-field-wrap ">
                                                    <input style="width: 213px;" tabindex="0" id="x-auto-15-input" class=" x-form-field x-form-text"
                                                        type="text" name='j_username' id='j_username'></div>
                                            </div>
                                        <div class="x-form-clear-left">
                                        </div>
                                    </div>
                                    <div class="x-form-item " tabindex="-1">
                                        <label for="x-auto-16-input" style="width: 80px; padding-left: 10px;" class="x-form-item-label">
                                             <@spring.message "login.pass"/>:</label><div class="x-form-element" id="x-form-el-x-auto-16" style="padding-left: 100px;">
                                                <div style="width: 223px;" id="x-auto-16" class=" x-form-field-wrap ">
                                                    <input style="width: 213px;" tabindex="0" id="x-auto-16-input" class=" x-form-field x-form-text"
                                                        type='password' name='j_password' id='j_password'></div>
                                            </div>
                                        <div class="x-form-clear-left">
                                        </div>
                                    </div>
                                    <!--</form>-->
                                </div>
                                <div style="width: 338px;" class="x-panel-bbar">
                                    <div style="width: 338px;" class=" x-box-layout-ct" id="x-auto-9">
                                        <div style="width: 338px; height: 22px;" class="x-box-inner">
                                            <div style="margin: 0px; left: 0px; top: 0px; width: 238px;" class=" x-box-item"
                                                id="x-auto-11">
                                                &nbsp;</div>
                                            <table style="margin: 0px; width: 75px; left: 238px; top: 0px;" id="x-auto-12" class="x-btn  x-btn-noicon x-box-item "
                                                role="presentation" cellspacing="0" onclick="document.forms[0].submit()">
                                                <tbody class="x-btn-small x-btn-icon-small-left">
                                                    <tr>
                                                        <td class="x-btn-tl">
                                                            <i>&nbsp;</i>
                                                        </td>
                                                        <td class="x-btn-tc">
                                                        </td>
                                                        <td class="x-btn-tr">
                                                            <i>&nbsp;</i>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="x-btn-ml">
                                                            <i>&nbsp;</i>
                                                        </td>
                                                        <td class="x-btn-mc">
                                                            <em class="" unselectable="off">
                                                              <button name="login" tabindex="0" class="x-btn-text" type="submit" style="position: relative;-->
                                                                 width: 69px;">
                                                                    <@spring.message "login.button"/></button>
                                                            </em>
                                                        </td>
                                                        <td class="x-btn-mr">
                                                            <i>&nbsp;</i>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="x-btn-bl">
                                                            <i>&nbsp;</i>
                                                        </td>
                                                        <td class="x-btn-bc">
                                                        </td>
                                                        <td class="x-btn-br">
                                                            <i>&nbsp;</i>
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="x-panel-bl">
                        <div class="x-panel-br">
                            <div class="x-panel-bc">
                                <div class="x-panel-footer">
                                    <div class=" x-panel-btns">
                                        <div style="width: 324px;" id="x-auto-8" hidefocus="true" tabindex="0" class=" x-small-editor x-panel-btns-center x-panel-fbar x-toolbar-layout-ct">
                                            <table class="x-toolbar-ct" role="presentation" cellspacing="0">
                                                <tbody>
                                                    <tr>
                                                        <td class="x-toolbar-left" align="left">
                                                            <table role="presentation" cellspacing="0">
                                                                <tbody>
                                                                    <tr class="x-toolbar-left-row">
                                                                        <td class="x-toolbar-cell">
                                                                            <table style="margin-right: 0px;" id="x-auto-10" class="x-btn button-link  x-btn-noicon"
                                                                                role="presentation" cellspacing="0">
                                                                                <tbody class="x-btn-small x-btn-icon-small-left">
                                                                                    <tr>
                                                                                        <td class="x-btn-tl">
                                                                                            <i>&nbsp;</i>
                                                                                        </td>
                                                                                        <td class="x-btn-tc">
                                                                                        </td>
                                                                                        <td class="x-btn-tr">
                                                                                            <i>&nbsp;</i>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td class="x-btn-ml">
                                                                                            <i>&nbsp;</i>
                                                                                        </td>
                                                                                        <td class="x-btn-mc">
                                                                                        </td>
                                                                                        <td class="x-btn-mr">
                                                                                            <i>&nbsp;</i>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td class="x-btn-bl">
                                                                                            <i>&nbsp;</i>
                                                                                        </td>
                                                                                        <td class="x-btn-bc">
                                                                                        </td>
                                                                                        <td class="x-btn-br">
                                                                                            <i>&nbsp;</i>
                                                                                        </td>
                                                                                    </tr>
                                                                                </tbody>
                                                                            </table>
                                                                        </td>
                                                                    </tr>
                                                                </tbody>
                                                            </table>
                                                        </td>
                                                        <td class="x-toolbar-right" align="right">
                                                            <table class="x-toolbar-right-ct" cellspacing="0">
                                                                <tbody>
                                                                    <tr>
                                                                        <td>
                                                                            <table cellspacing="0">
                                                                                <tbody>
                                                                                    <tr class="x-toolbar-right-row">
                                                                                    </tr>
                                                                                </tbody>
                                                                            </table>
                                                                        </td>
                                                                        <td>
                                                                            <table cellspacing="0">
                                                                                <tbody>
                                                                                    <tr class="x-toolbar-extras-row">
                                                                                    </tr>
                                                                                </tbody>
                                                                            </table>
                                                                        </td>
                                                                    </tr>
                                                                </tbody>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="x-clear">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    </form>

<!--
        <center>
            <table border="0" cellpadding="0" cellspacing="0" width=100%  height="50%" align="center" valign="middle">
                <tr>
                    <td align="center" valign="middle" class="Verdana" style="color: black; font-size: 15px;">
                        <#if login_error?has_content>
                        <div class="message" style="color: red">
                         ${login_error}<br/>
                        </div>
                        </#if>
                        <#if message?exists>
                        <div class="message">${message}</div>
                        </#if>

                        <h1 style="color: black; font-family: Verdana;">АИС "Госархивы СПб"</h1>
                        <div id="loginBox">
                            <@common.loginForm/>
                        </div>
                    </td>
                </tr>
            </table>
        </center>
-->
    </body>
</html>




