<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<%@ page import="spec.scope.conversation.ConversationExceptionKey"%>

<%
    String conversationException = (String) request.getAttribute(ConversationExceptionKey.EXCEPTION);
    String conversationStartPage = (String) request.getAttribute(ConversationExceptionKey.START_PAGE);
%>
<c:redirect url="/parts/conversation/conversation-exception-handler.xhtml">
    <c:param name="conversation-start-page" value="<%=conversationStartPage%>"/>
    <c:param name="conversation-exception" value="<%=conversationException%>"/>
</c:redirect>

