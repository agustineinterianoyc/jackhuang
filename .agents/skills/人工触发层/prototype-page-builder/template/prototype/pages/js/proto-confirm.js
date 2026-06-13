// ===== 统一确认弹窗（替代原生 confirm） =====
// 使用：
//   ProtoConfirm.show({
//     title: '删除确认',
//     message: '确定要删除该记录吗？',
//     confirmText: '确定',
//     cancelText: '取消',
//     danger: true,            // 确认按钮使用危险样式
//     onConfirm: function() { /* 执行删除 */ },
//     onCancel: function() {}  // 可选
//   });

(function () {
  var mask = null;
  var confirmCallback = null;
  var cancelCallback = null;

  function ensureDOM() {
    if (mask && document.contains(mask)) return;
    mask = document.getElementById('protoConfirmMask');
    if (!mask) {
      mask = document.createElement('div');
      mask.id = 'protoConfirmMask';
      mask.className = 'modal-overlay';
      mask.innerHTML = ''
        + '<div class="modal-content" style="width:420px;max-width:90vw;">'
        +   '<div class="modal-header">'
        +     '<h3 id="protoConfirmTitle">提示</h3>'
        +     '<button class="modal-close" id="protoConfirmClose">&times;</button>'
        +   '</div>'
        +   '<div class="modal-body" id="protoConfirmBody">确定要执行此操作吗？</div>'
        +   '<div class="modal-footer">'
        +     '<button class="btn-default" id="protoConfirmCancel">取消</button>'
        +     '<button class="btn-primary" id="protoConfirmOk">确定</button>'
        +   '</div>'
        + '</div>';
      document.body.appendChild(mask);

      // 关闭按钮
      document.getElementById('protoConfirmClose').addEventListener('click', close);
      // 取消按钮
      document.getElementById('protoConfirmCancel').addEventListener('click', close);
      // 确定按钮
      document.getElementById('protoConfirmOk').addEventListener('click', function () {
        close();
        if (typeof confirmCallback === 'function') confirmCallback();
      });
      // 点击遮罩关闭
      mask.addEventListener('click', function (e) {
        if (e.target === mask) close();
      });
      // Esc 关闭
      document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape' && mask.classList.contains('show')) close();
      });
    }
  }

  function close() {
    var cb = cancelCallback;
    mask.classList.remove('show');
    confirmCallback = null;
    cancelCallback = null;
    if (typeof cb === 'function') cb();
  }

  function show(options) {
    options = options || {};
    ensureDOM();

    confirmCallback = options.onConfirm || null;
    cancelCallback = options.onCancel || null;

    document.getElementById('protoConfirmTitle').textContent = options.title || '提示';
    document.getElementById('protoConfirmBody').textContent = options.message || '确定要执行此操作吗？';

    var okBtn = document.getElementById('protoConfirmOk');
    okBtn.textContent = options.confirmText || '确定';
    okBtn.className = options.danger ? 'btn-danger' : 'btn-primary';

    var cancelBtn = document.getElementById('protoConfirmCancel');
    cancelBtn.textContent = options.cancelText || '取消';

    mask.classList.add('show');
  }

  window.ProtoConfirm = { show: show };
})();
