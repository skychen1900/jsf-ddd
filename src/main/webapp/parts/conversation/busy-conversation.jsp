<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<%@ page import="spec.scope.conversation.ConversationExceptionKey"%>

<%
    String forwardPage = (String) request.getAttribute(ConversationExceptionKey.FORWARD_PAGE);
    String cid = (String) request.getAttribute(ConversationExceptionKey.CONVERSATION_ID);
    String ex = (String) request.getAttribute(ConversationExceptionKey.EXCEPTION);
%>
<c:redirect url="<%=forwardPage%>">
    <c:param name="cid" value="<%=cid%>"/>
    <c:param name="<%=ConversationExceptionKey.EXCEPTION%>" value="<%=ex%>"/>
</c:redirect>

