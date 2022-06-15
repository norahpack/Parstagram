package com.example.parstagram;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    public static final String KEY_POST_PARENT="postParent";
    public static final String KEY_TEXT = "text";
    public static final String KEY_COMMENTER = "commenter";

    public String getText(){
        return getString(KEY_TEXT);
    }

    public void setText(String text){
        put(KEY_TEXT, text);
    }

    public ParseObject getPostParent(){return getParseObject(KEY_POST_PARENT);}

    public void setPostParent(ParseObject parent){put(KEY_POST_PARENT, parent);}

    public ParseUser getCommenter(){
        return getParseUser(KEY_COMMENTER);
    }

    public void setCommenter(ParseUser commenter){
        put(KEY_COMMENTER, commenter);
    }
}
