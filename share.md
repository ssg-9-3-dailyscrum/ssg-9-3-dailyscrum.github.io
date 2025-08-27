---
layout: page
title: Share
permalink: /share/
---
<style>
  .post-item {
    margin-bottom: 20px;
  }
 .post-item h2 a {
    color: #000;
  }
</style>

<div class="posts-list">
  {% for post in site.categories.Share %}
  <article class="post-item">
    <h2><a href="{{ post.url | relative_url }}">{{ post.title }}</a></h2>
    <p class="post-meta">{{ post.date | date: "%Y년 %m월 %d일" }} {{ post.author }}</p>
  </article>
  {% endfor %}
</div>