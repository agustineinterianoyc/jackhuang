// ===== 统一页面切换 =====
// 页面容器约定：使用 .page-container 类名，通过 .active 类控制显示
// 使用：ProtoPage.switchTo('pageList') / ProtoPage.back()

window.ProtoPage = (function () {
  function switchTo(pageId) {
    document.querySelectorAll('.page-container').forEach(function (el) {
      el.classList.remove('active');
    });
    var target = document.getElementById(pageId);
    if (target) {
      target.classList.add('active');
    }
    window.scrollTo(0, 0);
  }

  function back(listPageId) {
    switchTo(listPageId || 'pageList');
  }

  return {
    switchTo: switchTo,
    back: back
  };
})();
