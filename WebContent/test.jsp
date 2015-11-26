<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>test jsp</title>
</head>
<body>
<%
Map<String,String> envs = System.getenv();
Iterator<String> iterator = envs.keySet().iterator();
while(iterator.hasNext()){
	String key = iterator.next();
	String val = envs.get(key);
%>
<p><%=key %>:<%=val %>
</p>
<%
}
%>
</body>
</html>