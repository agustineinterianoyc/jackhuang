// ===== 统一数据映射 =====
// 状态文案、部门名称等静态映射表，页面内直接引用避免各处散落 switch-case

window.ProtoData = (function () {
  // 状态码 → 中文文案（可被页面覆盖/扩展）
  var _statusMap = {
    draft: '草稿',
    pending: '待提交',
    filling: '填报中',
    submitted: '已提交',
    feedback: '专业反馈中',
    completed: '已完成',
    published: '已发布',
    approved: '已审核',
    rejected: '已退回',
    returned: '已退回',
    auditing: '待审核'
  };

  // 部门代码 → 中文名（可被页面覆盖/扩展）
  var _deptMap = {
    finance: '财务部',
    development: '发展部',
    publicity: '党委宣传部',
    safety: '安监部'
  };

  function statusText(status) {
    return _statusMap[status] || status || '-';
  }

  function deptName(dept) {
    return _deptMap[dept] || dept || '-';
  }

  // 允许页面扩展映射表
  function extendStatus(map) {
    Object.assign(_statusMap, map);
  }

  function extendDept(map) {
    Object.assign(_deptMap, map);
  }

  return {
    statusText: statusText,
    deptName: deptName,
    extendStatus: extendStatus,
    extendDept: extendDept,
    statusMap: _statusMap,
    deptMap: _deptMap
  };
})();
