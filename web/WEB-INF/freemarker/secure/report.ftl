<!-- Подключение библиотеки тэгов Stimulsoft -->
<#assign stiwebviewer=JspTaglibs["http://stimulsoft.com/webviewer"] />

<html>
<head>
    <title>Просмотр отчета</title>
    <@stiwebviewer.resources />
</head>

<body>
    <!-- Отобрежение вьювера(объект модели reportObj) -->
    <@stiwebviewer.webviewer report=reportObj/>
</body>
</html>
