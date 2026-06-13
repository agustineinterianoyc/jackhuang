window.PROTOTYPE_MENU = {
  siteName: '意见征集原型',
  siteDesc: '用于展示意见征集、反馈与审核相关原型页面。',
  defaultPage: 'home',
  items: [
    {
      key: 'home',
      label: '首页',
      file: 'pages/home.html',
      desc: '原型总览与使用说明'
    },
    {
      key: 'opinion-collection',
      label: '意见征集流程',
      children: [
        {
          key: 'opinion-collection-management',
          label: '意见征集管理',
          file: 'pages/p01-opinion-collection-management.html',
          desc: 'P01 管理列表与维护'
        },
        {
          key: 'grassroots-opinion-collection',
          label: '基层单位意见征集',
          file: 'pages/p02-grassroots-opinion-collection.html',
          desc: 'P02 基层单位填报页面'
        },
        {
          key: 'grassroots-opinion-review',
          label: '基层单位意见征集审核',
          file: 'pages/p03-grassroots-opinion-review.html',
          desc: 'P03 基层单位审核页面'
        },
        {
          key: 'department-feedback',
          label: '专业部门反馈',
          file: 'pages/p04-department-feedback.html',
          desc: 'P04 专业部门反馈页面'
        },
        {
          key: 'department-feedback-review',
          label: '专业部门反馈审核',
          file: 'pages/p05-department-feedback-review.html',
          desc: 'P05 专业部门反馈审核页面'
        }
      ]
    }
  ]
};
