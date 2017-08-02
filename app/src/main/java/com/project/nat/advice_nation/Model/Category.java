package com.project.nat.advice_nation.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Chari on 8/3/2017.
 */

public class Category {

    private long status;
    private List<CategoryList> data = null;

    public long getStatus() {
        return status;
    }
    public List<CategoryList> getData() {
        return data;
    }

    public class CategoryList {

        private long id;
        private String productCategoryName;

        public long getId() {
            return id;
        }

        public String getProductCategoryName() {
            return productCategoryName;
        }



    }
}
