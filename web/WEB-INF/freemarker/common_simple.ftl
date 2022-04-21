
<#macro pageTitle >
    ${moduleTitle}
</#macro>


<#macro url relativeUrl>${springMacroRequestContext.getContextPath()}${relativeUrl}</#macro>
<#macro message code>${springMacroRequestContext.getMessage(code)}</#macro>

<#macro loginForm>
<form id="upForm" class="" action="<@url "/j_acegi_security_check"/>"
      method="POST">
      <fieldset style="width: 300px">
          <legend>Введите для входа</legend>
        <p>
            <label for="j_username">
                                <@message "login.user"/><br/>
                <input type='text' name='j_username' id = 'j_username'>
            </label>
        </p>
        <p>
            <label for="j_password">
                                <@message "login.pass"/><br/>
                <input type='password' name='j_password' id = 'j_password'>

            </label>
        </p>
        <p>
            <input name="login" value="<@message "login.button"/>"
                   type="submit">
        </p>
    </fieldset>
</form>
</#macro>