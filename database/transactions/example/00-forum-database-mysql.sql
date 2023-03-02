--
-- Create the forum database
--

/**
  * General rules:
  *
  * - every surrogated primary key is named 'pk' (as Primary Key);
  * - every table name is plural
  *
  * To use this script from a shell
  * $ psql -U postgres template1 < 00-forum-database.sql
  */

CREATE DATABASE forumdb;

\c forumdb

CREATE TABLE users (
    pk int NOT NULL AUTO INCREMENT
    , username text NOT NULL
    , gecos text
    , email text NOT NULL
    , PRIMARY KEY( pk )
    , UNIQUE ( username )
    );


/**
  * A 'categories' is the main top level element
  * that groups a set of posts.
  * For instance categories can be
  * 'unix', 'database', 'php', 'perl' and so on
  */
CREATE TABLE categories (
    pk int NOT NULL AUTO INCREMENT
    , title text NOT NULL
    , description text
    , PRIMARY KEY ( pk )
    );


INSERT INTO categories( title, description )
    VALUES ( 'Database', 'Database related discussions' )
          ,( 'Unix', 'Unix and Linux discussions' )
          ,( 'Programming Languages', 'All about programming languages' )
          ,( 'Football', 'All about football' );

/**
  * A post is the actual content within a discussion thread.
  * Every post belongs to an author and a category.
  * If a post is the original top post (OP), it has a NULL 'reply_to',
  * otherwise if a post is a reply to another post it does contain
  * a link to the post it is replying to.
  */
CREATE TABLE posts (
    pk int NOT NULL AUTO INCREMENT
    , title           text
    , content         text
    , author          int NOT NULL
    , category        int NOT NULL
    , reply_to        int
    , created_on      timestamp with time zone DEFAULT current_timestamp
    , last_edited_on  timestamp with time zone DEFAULT current_timestamp
    , editable        boolean default true

    , PRIMARY KEY( pk )
    , FOREIGN KEY( author )   REFERENCES users( pk )
    , FOREIGN KEY( reply_to ) REFERENCES posts( pk )
    , FOREIGN KEY( category ) REFERENCES categories( pk )
    );

/**
  * A tag is a "label" that can be attached to a post
  * to help indexing and searching for specific arguments.
  *
  * A tag can be nested into other tags.
  */
CREATE TABLE tags (
    pk        int NOT NULL AUTO INCREMENT
    , tag     text NOT NULL
    , parent  int

    , PRIMARY KEY( pk )
    , FOREIGN KEY( parent ) REFERENCES tags( pk )
    );


CREATE TABLE j_posts_tags (
    tag_pk    int NOT NULL
    , post_pk int NOT NULL

    , FOREIGN KEY( tag_pk )  REFERENCES tags( pk )
    , FOREIGN KEY( post_pk ) REFERENCES posts( pk )
    );