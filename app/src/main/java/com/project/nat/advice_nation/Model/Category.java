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
        private long userId;
        private long date;
        private int productId;
        private String comment;
        private String name;
        private String followUserLink;
        private long productCategoryId;
        private String productCategoryName;
        private String productSubCategoryName;

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }
        public void setId(long id) {
            this.id = id;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFollowUserLink() {
            return followUserLink;
        }

        public void setFollowUserLink(String followUserLink) {
            this.followUserLink = followUserLink;
        }

        public void setProductCategoryId(long productCategoryId) {
            this.productCategoryId = productCategoryId;
        }

        public void setProductCategoryName(String productCategoryName) {
            this.productCategoryName = productCategoryName;
        }

        public void setProductSubCategoryName(String productSubCategoryName) {
            this.productSubCategoryName = productSubCategoryName;
        }



        public long getProductCategoryId() {
            return productCategoryId;
        }

        public String getProductSubCategoryName() {
            return productSubCategoryName;
        }

        public long getId() {
            return id;
        }

        public String getProductCategoryName() {
            return productCategoryName;
        }

    }
}
