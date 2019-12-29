package com.union.replytax.ui.Info.bean;

import com.union.replytax.base.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/9/18.
 */

public class InfoBean extends BaseBean<InfoBean.DataBean> {

    public static class DataBean implements Serializable{
        private int endRow;
        private boolean firstPage;
        private boolean hasNextPage;
        private boolean hasPreviousPage;
        private boolean lastPage;
        private int nextPage;
        private int pageNum;
        private int pageSize;
        private int pages;
        private int prePage;
        private int size;
        private int startRow;
        private int total;
        private ArrayList<RecordsBean> records;

        public int getEndRow() {
            return endRow;
        }

        public void setEndRow(int endRow) {
            this.endRow = endRow;
        }

        public boolean isFirstPage() {
            return firstPage;
        }

        public void setFirstPage(boolean firstPage) {
            this.firstPage = firstPage;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public boolean isHasPreviousPage() {
            return hasPreviousPage;
        }

        public void setHasPreviousPage(boolean hasPreviousPage) {
            this.hasPreviousPage = hasPreviousPage;
        }

        public boolean isLastPage() {
            return lastPage;
        }

        public void setLastPage(boolean lastPage) {
            this.lastPage = lastPage;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getPrePage() {
            return prePage;
        }

        public void setPrePage(int prePage) {
            this.prePage = prePage;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getStartRow() {
            return startRow;
        }

        public void setStartRow(int startRow) {
            this.startRow = startRow;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public ArrayList<RecordsBean> getRecords() {
            return records;
        }

        public void setRecords(ArrayList<RecordsBean> records) {
            this.records = records;
        }

        public static class RecordsBean implements Serializable{
            /**
             * categoryName : 热门活动
             * cover : https://item.jd.com/100000287145.html
             * createTime : 2018-09-15T01:33:38.000+0000
             * createTimeStr : 2018/09/15 09:33
             * isTop : true
             * rightTag : 活动报名中
             * srcId : 1
             * title : 天猫双十一 , 马云送手机
             * type : 1
             * id : c5ad4013-9dcb-4bf5-9c76-510c0cae50b7
             */

            private String categoryName;
            private String cover;
            private String createTime;
            private String createTimeStr;
            private boolean isTop;
            private String rightTag;
            private int srcId;
            private String title;
            private int type;
            private String id;
            private String recordArticleTime;
            private String writNo;
            private String writOrder;
            private int year;
            private boolean isFree;

            public String getRecordArticleTime() {
                return recordArticleTime;
            }

            public void setRecordArticleTime(String recordArticleTime) {
                this.recordArticleTime = recordArticleTime;
            }


            public String getWritNo() {
                return writNo;
            }

            public void setWritNo(String writNo) {
                this.writNo = writNo;
            }

            public String getWritOrder() {
                return writOrder;
            }

            public void setWritOrder(String writOrder) {
                this.writOrder = writOrder;
            }

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }

            public boolean isFree() {
                return isFree;
            }

            public void setFree(boolean free) {
                isFree = free;
            }

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getCreateTimeStr() {
                return createTimeStr;
            }

            public void setCreateTimeStr(String createTimeStr) {
                this.createTimeStr = createTimeStr;
            }

            public boolean isIsTop() {
                return isTop;
            }

            public void setIsTop(boolean isTop) {
                this.isTop = isTop;
            }

            public String getRightTag() {
                return rightTag;
            }

            public void setRightTag(String rightTag) {
                this.rightTag = rightTag;
            }

            public int getSrcId() {
                return srcId;
            }

            public void setSrcId(int srcId) {
                this.srcId = srcId;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
    }
}
