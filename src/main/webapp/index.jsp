<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dom1</title>
</head>
<body>

<table border=1>
<%
String content[][]={
{"Hello","helloindex.html"},
{"Dom1 Kalkulator Kredytowy","kalkulatorKredytowy.html"},
};
for(String[] i : content){
    %>
    <tr>
    <td><%= i[0] %></td>
    <td><a href="<%= i[1] %>"><%= i[1] %></a></td>
    </tr>
    <%
}
%>
</table>

</body>
</html>