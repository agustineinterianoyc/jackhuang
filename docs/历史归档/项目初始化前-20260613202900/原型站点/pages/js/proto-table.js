// ===== 统一表格操作 =====
// 提供排序、搜索筛选、清空搜索等通用方法

window.ProtoTable = (function () {

  // 表格排序 —— 对 data 数组按 field 排序，返回新数组
  // direction: 'asc' | 'desc'
  function sort(data, field, direction) {
    return data.slice().sort(function (a, b) {
      var va = a[field];
      var vb = b[field];
      // 尝试数字比较
      var na = parseFloat(va);
      var nb = parseFloat(vb);
      if (!isNaN(na) && !isNaN(nb)) {
        return direction === 'asc' ? na - nb : nb - na;
      }
      // 字符串比较
      va = String(va || '');
      vb = String(vb || '');
      return direction === 'asc' ? va.localeCompare(vb) : vb.localeCompare(va);
    });
  }

  // 更新排序图标状态
  function updateSortIcon(iconContainerId, activeDir) {
    var container = document.getElementById(iconContainerId);
    if (!container) return;
    var icons = container.querySelectorAll('i');
    icons.forEach(function (icon) {
      var dir = icon.getAttribute('data-dir');
      icon.classList.toggle('active', dir === activeDir);
    });
  }

  // 搜索筛选 —— 对 data 过滤，返回新数组
  // filters: { field1: 'value', field2: 'value' }，支持字符串包含匹配
  function filter(data, filters) {
    return data.filter(function (item) {
      return Object.keys(filters).every(function (key) {
        var filterVal = filters[key];
        if (!filterVal) return true;
        var itemVal = String(item[key] || '').toLowerCase();
        return itemVal.indexOf(String(filterVal).toLowerCase()) !== -1;
      });
    });
  }

  // 清空表单控件值
  function clearInputs(inputIds) {
    (inputIds || []).forEach(function (id) {
      var el = document.getElementById(id);
      if (!el) return;
      if (el.tagName === 'SELECT') {
        el.selectedIndex = 0;
      } else {
        el.value = '';
      }
    });
  }

  return {
    sort: sort,
    updateSortIcon: updateSortIcon,
    filter: filter,
    clearInputs: clearInputs
  };
})();
