package com.hk.demo.data.pagination;

import java.util.List;

/**
 * 通用分页结果对象。
 *
 * @param <T> 列表元素类型
 */
public class PageResult<T> {

    private final long total;
    private final List<T> records;

    public PageResult(long total, List<T> records) {
        this.total = total;
        this.records = records;
    }

    public long getTotal() {
        return total;
    }

    public List<T> getRecords() {
        return records;
    }
}
