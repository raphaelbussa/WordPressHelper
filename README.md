# WordPressHelper
[![License](https://img.shields.io/badge/License-MIT-blue.svg?style=flat)](http://opensource.org/licenses/MIT) [ ![Download](https://api.bintray.com/packages/raphaelbussa/maven/wordpress-helper/images/download.svg) ](https://bintray.com/raphaelbussa/maven/wordpress-helper/_latestVersion) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-WordPressHelper-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1816) [![JitPack.io](https://img.shields.io/github/release/rebus007/WordPressHelper.svg?label=JitPack)](https://jitpack.io/#rebus007/WordPressHelper/)

This library is created for get post and information from a blog or a website created with WordPress
### Import
At the moment the library is in my personal maven repo
```Gradle
repositories {
    maven {
        url 'http://dl.bintray.com/raphaelbussa/maven'
    }
}
```
```Gradle
dependencies {
    compile 'rebus:wordpress-helper:1.0.10'
}
```
### How to use
How to parse a generic WordPress feed
```java
import rebus.wordpress.helper.FeedItem;
import rebus.wordpress.helper.FeedParser;

private FeedParser feedParser;

feedParser = new FeedParser("feed rss"); //set rss url like http://www.smartphone-italia.com/feed
feedParser.showLoading(true, getString(R.string.loading), this); //set loading dialog, need boolean value, string to show and context
feedParser.execute(); //start parsing!
feedParser.onFinish(new FeedParser.OnComplete() {
    @Override
    public void onFinish(ArrayList<FeedItem> feedItems) {
        //return arraylist with all info for each item
        //do here something when parsing is finished
    }

    @Override
    public void onError() {
        //this method is call on parsing error
    }
});
```
You can also use StarDataBase class for save article in a database
```java
import rebus.wordpress.helper.StarDataBase;
import rebus.wordpress.helper.FeedItem;

private StarDataBase starDataBase;
private FeedItem feedItem;

starDataBase = new StarDataBase(this);
int numStar = getStarCount(); //return number of item in the database
boolean isPresent = starDataBase.isStar(feedItem); //check if the item is in the database, returns true if there is
starDataBase.addStar(feedItem); //add item
starDataBase.deleteStar(feedItem); //delete item
starDataBase.deleteAll(); //delate all item form database
ArrayList<FeedItem> feedItems = starDataBase.getStar(); //get arraylist with all item
```
Get comments from an article
```java
import rebus.wordpress.helper.CommentItem;
import rebus.wordpress.helper.CommentParser;

private CommentParser commentParser;

commentParser = new CommentParser("comments feed rss"); //you can get this value from precedent parsing using feedParser.getCommentRss();
commentParser.execute(); //start parsing!
commentParser.onFinish(new CommentParser.OnComplete() {
    @Override
    public void onFinish(ArrayList<CommentItem> commentItems) {
        //return arraylist with all info for each comment
        //do here something when parsing is finished
    }

    @Override
    public void onError() {
        //this method is call on parsing error
    }
});
```
If the blog use default WordPress comment system you can use PostComment class for add a comment
```java
import rebus.wordpress.helper.PostComment;

PostComment postComment = new PostComment(
        "id post", //you can get this value from precedent parsing using feedParser.getId();
        "http://www.smartphone-italia.com", //base url of blog
        "name user", //name of the commenter required
        "email user", //email of the commenter required
        "web site user", //web site of the commenter not required, you can pass an empty string
        "body of comment", //body of comment required
        "0"); //if is a new comment you must pass a string with value "0", if is an answer you must pass the id of the comment  to which you want to respond, you can get this value from precedent parsing commentItem.getId();
postComment.showLoading(true, getString(R.string.loading), this); //set loading dialog, need boolean value, string to show and context
postComment.execute(); //send comment!
postComment.onFinish(new PostComment.OnComplete() {
    @Override
    public void onFinish() {
        //this method is call on sending comment successful
    }

    @Override
    public void onError() {
        //this method is call on sending comment error
    }
});
```
You can also get post from a specific author using AuthorParser
```java
import rebus.wordpress.helper.AuthorParser;
import rebus.wordpress.helper.FeedItem;

private AuthorParser authorParser;

authorParser = new AuthorParser("http://www.smartphone-italia.com", "rebus"); //set url of blog and author nickname
authorParser.showLoading(true, getString(R.string.loading), this); //set loading dialog, need boolean value, string to show and context
authorParser.execute(); //start parsing!
authorParser.onFinish(new AuthorParser.OnComplete() {
    @Override
    public void onFinish(ArrayList<FeedItem> feedItems) {
        //return arraylist with all info for each item
        //do here something when parsing is finished
    }

    @Override
    public void onError() {
        //this method is call on parsing error
    }
});
```
And in the end you can search in the blog
```java
import rebus.wordpress.helper.SearchParser;
import rebus.wordpress.helper.FeedItem;

private SearchParser searchParser;

searchParser = new SearchParser("http://www.smartphone-italia.com", "search something"); //set url of blog and search query
searchParser.showLoading(true, getString(R.string.loading), this); //set loading dialog, need boolean value, string to show and context
searchParser.execute(); //start parsing!
searchParser.onFinish(new SearchParser.OnComplete() {
    @Override
    public void onFinish(ArrayList<FeedItem> feedItems) {
        //return arraylist with all info for each item
        //do here something when parsing is finished
    }

    @Override
    public void onError() {
        //this method is call on parsing error
    }
});
```
Remember if you use loading dialog
```java
@Override
protected void onPause() {
    super.onPause();
    feedParser.onPauseLoading();
}
```
### Changelog
- 1.0.8 fix category/tag bug 
- 1.0.7 fix bintray maven upload 
- 1.0.6 bug fix
- 1.0.5 bug fix
- 1.0.4 bug fix
- 1.0.3 bug fix
- 1.0.2 bug fix
- 1.0.1 bug fix
- 1.0.0 add StarDatabase
- 0.0.1 first alpha

### App using WordPress Helper
If you use this lib [contact me](mailto:raphaelbussa@gmail.com?subject=WordPress Helper) and I will add it to the list below:
- [Smartphone Italia](https://play.google.com/store/apps/details?id=rebus.smartphone.italia)
- [Mister Gadget Beta](https://play.google.com/store/apps/details?id=rebus.mister.gadget)

###Developed By
Raphaël Bussa - [raphaelbussa@gmail.com](mailto:raphaelbussa@gmail.com)

[ ![Twitter](https://raw.githubusercontent.com/rebus007/WordPressHelper/master/img/social/twitter-icon.png) ](https://twitter.com/rebus_007)[ ![Google Plus](https://raw.githubusercontent.com/rebus007/WordPressHelper/master/img/social/google-plus-icon.png) ](https://plus.google.com/+RaphaelBussa/posts)[ ![Linkedin](https://raw.githubusercontent.com/rebus007/WordPressHelper/master/img/social/linkedin-icon.png) ](https://www.linkedin.com/in/rebus007)

### License
```
The MIT License (MIT)

Copyright (c) 2015 Raphael Bussa <raphaelbussa@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
