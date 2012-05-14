<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="flip" uri="http://www.tacitknowledge.com/flip" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Welcome to Spring Web MVC project</title>
    </head>

    <body>
        <p>
            Hello this is the test web application which shows you how 
            Flip framework works in a web application.
            Below you will find two pages:
        <ol>
            <li><a href="page.html">The page managed by Spring MVC.</a> 
                It will redirect you to a page ("404") if the "test" feature is disabled.
            </li>
            <li><a href="test">The page made as a simple Servlet.</a>
                It will show you the "BBB(NO-CODE)" message if the "test" feature is disabled.
            </li>
        </ol>
        </p>
        <p>
            Below you will see the message flipped by JSP tags: <br />
            <strong>
                <flip:enabled feature="test">ENABLED</flip:enabled>
                <flip:disabled feature="test">TEST FEATURE DISABLED</flip:disabled>
            </strong>
        </p>
        <p>
            If you want to enable or disable the "test" feature runtime please follow these links:
        <ol>
            <li><a href="index.html?flip.test=enabled">to enable</a></li>
            <li><a href="index.html?flip.test=disabled">to disable</a></li>
        </ol>
        </p>
        <hr />
        <strong>Here you will see the current overriden state of the features:</strong>
        <pre style="border:1px solid silver;">
            <flip:sessionFeatures />
        </pre>
        <br /><br /><br /><br /><br />
        <hr />
        <p style="font-size: 8pt;">
            To run this sample application you should add to web server JVM the following argument:
            <pre>-javaagent:path/to/aspectjweaver.jar</pre>
        </p>
    </body>
</html>
