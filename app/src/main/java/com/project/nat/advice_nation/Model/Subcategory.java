package com.project.nat.advice_nation.Model;

import java.util.List;

/**
 * Created by Surya Chundawat on 8/20/2017.
 */

public class Subcategory {


    private long status;
    private List<Data> data = null;

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data{

        private long id;
        private String productName;
        private long price;
        private long anKoin;
        private long date;
        private String name;
        private String features;
        private String transactionType;
        private String transactionCategory;
        private String message;
        private String description;
        private long productSubCategoryId;
        private long productRating;
        private long userId;
        private ImageMeta imageMeta;

        public long getAnKoin() {
            return anKoin;
        }

        public String getTransactionType() {
            return transactionType;
        }

        public String getTransactionCategory() {
            return transactionCategory;
        }

        public String getMessage() {
            return message;
        }



        public long getId() {
            return id;
        }
        public void setId(long id) {
            this.id = id;
        }
        public long getDate() {
            return date;}
        public String getName() {
            return name;}

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public long getPrice() {
            return price;
        }

        public void setPrice(long price) {
            this.price = price;
        }

        public String getFeatures() {
            return features;
        }

        public void setFeatures(String features) {
            this.features = features;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public long getProductSubCategoryId() {
            return productSubCategoryId;
        }

        public void setProductSubCategoryId(long productSubCategoryId) {
            this.productSubCategoryId = productSubCategoryId;
        }

        public long getProductRating() {
            return productRating;
        }

        public void setProductRating(long productRating) {
            this.productRating = productRating;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public ImageMeta getImageMeta() {
            return imageMeta;
        }

        public void setImageMeta(ImageMeta imageMeta) {
            this.imageMeta = imageMeta;
        }
    }


    public class ImageMeta {

        private String uri;
        private String fileName;

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }

}
