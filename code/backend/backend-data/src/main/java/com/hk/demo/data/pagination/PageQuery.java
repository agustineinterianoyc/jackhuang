package com.hk.demo.data.pagination;

/**
 * 通用分页查询参数抽象。
 */
public class PageQuery {

    public static final int DEFAULT_PAGE_NUMBER = 1;
    public static final int DEFAULT_PAGE_SIZE = 20;

    private int pageNumber = DEFAULT_PAGE_NUMBER;
    private int pageSize = DEFAULT_PAGE_SIZE;

    public PageQuery() {
    }

    public PageQuery(int pageNumber, int pageSize) {
        setPageNumber(pageNumber);
        setPageSize(pageSize);
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber <= 0 ? DEFAULT_PAGE_NUMBER : pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize;
    }

    /**
     * 获取分页偏移量，便于 JDBC 或 ORM 直接拼接分页条件。
     *
     * @return 偏移量
     */
    public int getOffset() {
        return (pageNumber - 1) * pageSize;
    }
}
