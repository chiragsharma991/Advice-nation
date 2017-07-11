package com.project.nat.advice_nation.Model;

import static com.project.nat.advice_nation.R.id.UserName;

/**
 * Created by Chari on 7/10/2017.
 */

public class ReviewItem
{

    String UserName;
    String UserComment;

    public ReviewItem(String username, String usercomment) {
        this.UserName= username;
        this.UserComment=usercomment;
    }

    public String getUserName()
    {
        return UserName;
    }

    public void setUserName(String userName)
    {
        UserName = userName;
    }

    public String getUserComment() {
        return UserComment;
    }

    public void setUserComment(String userComment) {
        UserComment = userComment;
    }



}
