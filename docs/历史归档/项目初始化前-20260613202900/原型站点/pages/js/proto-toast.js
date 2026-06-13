// ===== 统一 Toast 提示 =====
// 使用：ProtoToast.success('保存成功') / ProtoToast.error('操作失败') / ProtoToast.info('提示信息')
window.ProtoToast = (function () {
  var container = null;

  function ensureContainer() {
    if (!container || !document.contains(container)) {
      container = document.getElementById('protoToastContainer');
      if (!container) {
        container = document.createElement('div');
        container.id = 'protoToastContainer';
        container.className = 'proto-toast-container';
        document.body.appendChild(container);
      }
    }
    return container;
  }

  var icons = {
    success: '<i class="fas fa-check-circle" style="color:#52c41a;margin-right:8px;"></i>',
    error: '<i class="fas fa-times-circle" style="color:#ff4d4f;margin-right:8px;"></i>',
    info: '<i class="fas fa-info-circle" style="color:#1890ff;margin-right:8px;"></i>'
  };

  function show(type, message) {
    var root = ensureContainer();
    var toast = document.createElement('div');
    toast.className = 'proto-toast proto-toast-' + (type || 'info');
    toast.innerHTML = (icons[type] || icons.info) + '<span>' + message + '</span>';
    root.appendChild(toast);

    // 进场动画
    requestAnimationFrame(function () {
      toast.style.opacity = '1';
      toast.style.transform = 'translateY(0)';
    });

    // 自动移除
    setTimeout(function () {
      toast.style.opacity = '0';
      toast.style.transform = 'translateY(-12px)';
      setTimeout(function () { toast.remove(); }, 300);
    }, 3000);
  }

  return {
    success: function (msg) { show('success', msg); },
    error: function (msg) { show('error', msg); },
    info: function (msg) { show('info', msg); },
    show: show
  };
})();

// 内联样式（不依赖外部 CSS）
(function () {
  var style = document.createElement('style');
  style.textContent = ''
    + '.proto-toast-container{position:fixed;top:24px;left:50%;transform:translateX(-50%);z-index:2000;display:flex;flex-direction:column;gap:8px;pointer-events:none;}'
    + '.proto-toast{background:#fff;border-radius:4px;padding:10px 20px;box-shadow:0 4px 12px rgba(0,0,0,0.15);display:flex;align-items:center;font-size:14px;opacity:0;transform:translateY(-12px);transition:all 0.3s ease;pointer-events:auto;}'
    + '.proto-toast-success{border-left:4px solid #52c41a;}'
    + '.proto-toast-error{border-left:4px solid #ff4d4f;}'
    + '.proto-toast-info{border-left:4px solid #1890ff;}';
  document.head.appendChild(style);
})();
