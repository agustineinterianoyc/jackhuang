window.PROTOTYPE_MENU = {
  siteName: '产品创建原型',
  siteDesc: '用于展示产品创建相关页面、流程和配置关系。',
  defaultPage: 'home',
  items: [
    {
      key: 'home',
      label: '首页',
      file: 'pages/home.html',
      desc: '原型总览与使用说明'
    },
    {
      key: 'product-create',
      label: '产品创建',
      children: [
        {
          key: 'product-basic',
          label: '基础信息',
          file: 'pages/empty.html',
          desc: '示例占位，后续替换为真实页面'
        },
        {
          key: 'product-preview',
          label: '预览确认',
          file: 'pages/empty.html',
          desc: '示例占位，后续替换为真实页面'
        }
      ]
    }
  ]
};
