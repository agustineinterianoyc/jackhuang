// ===== 统一分页组件 =====
// 使用：
//   ProtoPagination.render({
//     containerId: 'pagination',
//     total: 100,
//     current: 1,
//     pageSize: 10,
//     onChange: function(page) { renderTable(page); }
//   });

window.ProtoPagination = (function () {
  var ELLIPSIS_THRESHOLD = 7;

  function render(options) {
    var container = document.getElementById(options.containerId);
    if (!container) return;

    var total = options.total || 0;
    var current = options.current || 1;
    var pageSize = options.pageSize || 10;
    var totalPages = Math.ceil(total / pageSize) || 1;
    var onChange = options.onChange || function () {};
    var windowSize = options.windowSize || 3; // 当前页两侧各显示 windowSize 个页码

    var html = '';

    // 上一页
    html += '<button class="page-item' + (current <= 1 ? ' disabled' : '') + '"'
      + (current <= 1 ? '' : ' onclick="ProtoPagination._fire(' + (current - 1) + ')"') + '>'
      + '<i class="fas fa-chevron-left" style="font-size:12px;"></i></button>';

    if (totalPages <= ELLIPSIS_THRESHOLD) {
      for (var i = 1; i <= totalPages; i++) {
        html += '<button class="page-item' + (i === current ? ' active' : '') + '" onclick="ProtoPagination._fire(' + i + ')">' + i + '</button>';
      }
    } else {
      html += '<button class="page-item' + (current === 1 ? ' active' : '') + '" onclick="ProtoPagination._fire(1)">1</button>';

      var leftBound = Math.max(2, current - Math.floor(windowSize / 2));
      var rightBound = Math.min(totalPages - 1, current + Math.floor(windowSize / 2));

      if (leftBound > 2) html += '<span class="page-ellipsis">...</span>';
      for (var j = leftBound; j <= rightBound; j++) {
        html += '<button class="page-item' + (j === current ? ' active' : '') + '" onclick="ProtoPagination._fire(' + j + ')">' + j + '</button>';
      }
      if (rightBound < totalPages - 1) html += '<span class="page-ellipsis">...</span>';

      html += '<button class="page-item' + (current === totalPages ? ' active' : '') + '" onclick="ProtoPagination._fire(' + totalPages + ')">' + totalPages + '</button>';
    }

    // 下一页
    html += '<button class="page-item' + (current >= totalPages ? ' disabled' : '') + '"'
      + (current >= totalPages ? '' : ' onclick="ProtoPagination._fire(' + (current + 1) + ')"') + '>'
      + '<i class="fas fa-chevron-right" style="font-size:12px;"></i></button>';

    container.innerHTML = html;
  }

  return {
    render: render,
    _fire: function (page) { /* 占位，由调用方在 onChange 中实现 */ },
    ELLIPSIS_THRESHOLD: ELLIPSIS_THRESHOLD
  };
})();
