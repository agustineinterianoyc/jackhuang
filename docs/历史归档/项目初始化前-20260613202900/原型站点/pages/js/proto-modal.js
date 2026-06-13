// ===== 统一弹窗控制 =====
// 使用：ProtoModal.open('myModal') / ProtoModal.close('myModal')
// 自动绑定弹窗遮罩点击关闭

window.ProtoModal = (function () {
  function open(modalId) {
    var el = document.getElementById(modalId);
    if (!el) return;
    el.classList.add('show');
    el.classList.add('active');
  }

  function close(modalId) {
    var el = document.getElementById(modalId);
    if (!el) return;
    el.classList.remove('show');
    el.classList.remove('active');
  }

  function initAll() {
    document.querySelectorAll('.modal-overlay').forEach(function (overlay) {
      // 避免重复绑定
      if (overlay.dataset.protoModalInit === '1') return;
      overlay.dataset.protoModalInit = '1';

      overlay.addEventListener('click', function (e) {
        if (e.target === overlay) {
          close(overlay.id);
        }
      });
    });
  }

  // 页面加载后自动初始化已有弹窗
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initAll);
  } else {
    initAll();
  }

  return {
    open: open,
    close: close,
    initAll: initAll
  };
})();
