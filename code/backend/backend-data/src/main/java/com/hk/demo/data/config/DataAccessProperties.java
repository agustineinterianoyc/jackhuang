package com.hk.demo.data.config;

import com.hk.demo.data.pagination.PageQuery;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 数据访问模块公共配置，统一收口分页等基础约定。
 */
@ConfigurationProperties(prefix = "hk.demo.data")
public class DataAccessProperties {

    private int defaultPageSize = PageQuery.DEFAULT_PAGE_SIZE;
    private int maxPageSize = 200;

    public int getDefaultPageSize() {
        return defaultPageSize;
    }

    public void setDefaultPageSize(int defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }

    public int getMaxPageSize() {
        return maxPageSize;
    }

    public void setMaxPageSize(int maxPageSize) {
        this.maxPageSize = maxPageSize;
    }
}
