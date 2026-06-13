package com.hk.demo.data.pagination;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PageQueryTests {

    @Test
    void shouldNormalizeInvalidPageArguments() {
        PageQuery pageQuery = new PageQuery(0, -1);

        Assertions.assertEquals(PageQuery.DEFAULT_PAGE_NUMBER, pageQuery.getPageNumber());
        Assertions.assertEquals(PageQuery.DEFAULT_PAGE_SIZE, pageQuery.getPageSize());
        Assertions.assertEquals(0, pageQuery.getOffset());
    }

    @Test
    void shouldCalculateOffsetFromPageArguments() {
        PageQuery pageQuery = new PageQuery(3, 10);

        Assertions.assertEquals(20, pageQuery.getOffset());
    }
}
