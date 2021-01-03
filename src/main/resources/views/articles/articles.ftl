<#ftl encoding="utf-8">

<body xmlns="http://www.w3.org/1999/html">

    <#list articles as article>
            <h2><span class="article_id">#${article.id}</span> ${article.name}</h2>
            <p>${article.content}</p>
            <p class="author">Rédigé par ${article.author}
                le ${article.created_time}</p>
    </#list>


</body>

</html>
