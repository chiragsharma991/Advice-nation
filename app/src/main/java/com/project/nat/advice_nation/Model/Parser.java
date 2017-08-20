package com.project.nat.advice_nation.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Surya Chundawat on 8/19/2017.
 */

public class Parser implements Parcelable {


    private ArrayList<Category> SubcategoryList;

    public Parser(ArrayList<Category> subcategoryList) {
        SubcategoryList = subcategoryList;
    }

    public ArrayList<Category> getSubcategoryList() {
        return SubcategoryList;
    }

    public void setSubcategoryList(ArrayList<Category> subcategoryList) {
        SubcategoryList = subcategoryList;
    }

    protected Parser(Parcel in) {
        if (in.readByte() == 0x01) {
            SubcategoryList = new ArrayList<Category>();
            in.readList(SubcategoryList, Category.class.getClassLoader());
        } else {
            SubcategoryList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (SubcategoryList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(SubcategoryList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Parser> CREATOR = new Parcelable.Creator<Parser>() {
        @Override
        public Parser createFromParcel(Parcel in) {
            return new Parser(in);
        }

        @Override
        public Parser[] newArray(int size) {
            return new Parser[size];
        }
    };
}
