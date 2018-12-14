<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<%@ page import="spec.scope.conversation.ConversationExceptionKey"%>

<%
    String forwardPage = (String) request.getAttribute(ConversationExceptionKey.FORWARD_PAGE);
    String ex = (String) request.getAttribute(ConversationExceptionKey.EXCEPTION);
    String cid = (String) request.getAttribute(ConversationExceptionKey.CONVERSATION_ID);
%>
<c:redirect url="/parts/conversation/conversation-exception-handler.xhtml">
    <c:param name="<%=ConversationExceptionKey.FORWARD_PAGE%>" value="<%=forwardPage%>"/>
    <c:param name="<%=ConversationExceptionKey.EXCEPTION%>" value="<%=ex%>"/>
    <c:param name="cid" value="<%=cid%>"/>
</c:redirect>

