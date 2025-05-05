Database design:
- https://www.dragonflydb.io/databases/schema/blog


Blogging
- Authenticate users via JWT
- CRU- users (sign up & settings page)
- CRUD Posts
- CRUD Comments on posts
- GET and display paginated lists of posts
- Categories/Tags: A way to classify and organize blog posts.
- Favorite posts
- Follow other users

Coding
Đầu vào:
- 1 mảng chữ số thể hiện 1 số tự nhiên
Đầu ra:
- mảng chữ số thể hiện số trên đã cộng thêm 1.


Blogging
- Authenticate users via JWT
- CRU- users (sign up & settings page)
- CRUD Posts
- CRUD Comments on posts
- GET and display paginated lists of posts
- Categories/Tags: A way to classify and organize blog posts.
- Favorite posts
- Follow other users


// query posts that user like

select * from post p join favorite_post fp where fp.user_id = :user

// query posts recommend for user
// select post(id, name, tag)
<!-- select * from post p join favorite_post fp where fp.user_id = :user join post_tag pt where pt.tag in () -->
select pt.id from post_tag pt where pt.post_id in ( select p.id from post p join favorite_post fp where fp.user_id = :user ) as t1

select * from post p join post_tag pt where pt.tag in (select * from t1)

user, post, comment, category, tag, user_follow, post_category, post_tag

user {
    id, name, password
}

post {
    id, post_name, post_description, favorite_post
}

post_category {
    id, post_id, category_id
}

post_tag {
    id, post_id, tag_id
}

comment {
    id, user_id, post_id, content
}

category {
    id, name
}

tag {
    id, name
}

user_follow {
    id, user_id, follow_user_id
}

favorite_post {
    id, user_id, post_id-
}


Coding
Đầu vào:
- 1 mảng chữ số thể hiện 1 số tự nhiên
Đầu ra:
- mảng chữ số thể hiện số trên đã cộng thêm 1.

[1,2,3]

[1,2,4]

[9,9]

[1,0,0]

Java
- Triển khai deploy, Java phát triển chậm hơn
