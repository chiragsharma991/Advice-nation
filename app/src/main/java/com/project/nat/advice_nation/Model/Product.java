package com.project.nat.advice_nation.Model;

/**
 * Created by Chari on 7/4/2017.
 */

public class Product
{
    private final String time;
    String Title;
    String Subtitle;
    int image;



    public Product(String time, String title, String Subtitle, int image) {
        this.Title=title;
        this.time=time;
        this.Subtitle=Subtitle;

        this.image=image;

    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSubtitle() {
        return Subtitle;
    }

    public void setSubtitle(String subtitle) {
        Subtitle = subtitle;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
