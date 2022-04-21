<#macro regError>
<span class="error"><@spring.showErrors"<br>"/></span>
</#macro>

<#macro pageTitle subTitle>
    <@spring.message "site.title"/><#if subTitle?has_content> :: ${subTitle?upper_case}</#if>
</#macro>

<#macro box class id title>
<div class="${class}">
    <h2>${title}</h2>
    <div class="right"></div>
    <div id="${id}" class="boxContent">
        <#nested>
    </div>
</div>
</#macro>

<#macro loginForm>
<form id="upForm" class="" action="<@spring.url "/j_acegi_security_check"/>"
      method="POST">
      <fieldset style="width: 300px">
          <legend>Введите для входа</legend>
        <p>
            <label for="j_username">
                                <@spring.message "login.user"/><br/>
                <input type='text' name='j_username' id = 'j_username'>
            </label>
        </p>
        <p>
            <label for="j_password">
                                <@spring.message "login.pass"/><br/>
                <input type='password' name='j_password' id = 'j_password'>

            </label>
        </p>
        <p>
            <input name="login" value="<@spring.message "login.button"/>"
                   type="submit">
        </p>
    </fieldset>
</form>
</#macro>


<#macro pngImage src width height>
<#if iePre7?exists>
<div>
    <span style="display:inline-block;width:${width}px;height:${height}px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<@spring.url "${src}"/>', sizingMethod='scale')"></span>
</div>
<#else>
<img src=<@spring.url "${src}"/> width=${width} height=${height} border=0/>
     </#if>
     </#macro>

