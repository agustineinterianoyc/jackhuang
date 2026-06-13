<script setup lang="ts">
defineOptions({
  name: 'HomeView',
})
</script>

<template>
  <section class="home-page">
    <h2>首页</h2>
    <p>当前脚手架已移除演示业务模块，请按正式需求在对应模块中补充页面与接口。</p>
  </section>
</template>

<style scoped lang="less">
.home-page {
  background: #fff;
  padding: 16px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);

  h2 {
    margin-bottom: 8px;
    font-size: 18px;
    color: #303133;
  }

  p {
    font-size: 14px;
    color: #303133;
    line-height: 1.8;
  }
}
</style>
