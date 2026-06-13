(function () {
  var menuConfig = window.PROTOTYPE_MENU || {};
  var pageFrame = document.getElementById('pageFrame');
  var menuRoot = document.getElementById('menuRoot');
  var siteName = document.getElementById('siteName');
  var siteDesc = document.getElementById('siteDesc');
  var pagePath = document.getElementById('pagePath');
  var pageTitle = document.getElementById('pageTitle');
  var pageDesc = document.getElementById('pageDesc');
  var dialogMask = document.getElementById('dialogMask');
  var dialogContent = document.getElementById('dialogContent');
  var headerHelpBtn = document.getElementById('headerHelpBtn');
  var dialogCloseBtn = document.getElementById('dialogCloseBtn');
  var flatMenu = [];

  function canPrecheckPageFile() {
    return window.location.protocol === 'http:' || window.location.protocol === 'https:';
  }

  function flattenItems(items, parentLabel) {
    (items || []).forEach(function (item) {
      var currentPath = parentLabel ? parentLabel + ' / ' + item.label : item.label;
      if (item.children && item.children.length) {
        flattenItems(item.children, currentPath);
        return;
      }

      flatMenu.push({
        key: item.key,
        label: item.label,
        file: item.file,
        desc: item.desc || '',
        path: currentPath
      });
    });
  }

  function getMenuItem(key) {
    for (var i = 0; i < flatMenu.length; i += 1) {
      if (flatMenu[i].key === key) {
        return flatMenu[i];
      }
    }
    return null;
  }

  function setActiveMenu(key) {
    var buttons = document.querySelectorAll('[data-menu-key]');
    buttons.forEach(function (button) {
      var isActive = button.getAttribute('data-menu-key') === key;
      button.classList.toggle('is-active', isActive);
    });
  }

  function updatePageHeader(item) {
    pagePath.textContent = '原型 / ' + (item.path || item.label || '未命名页面');
    pageTitle.textContent = item.label || '未命名页面';
    pageDesc.textContent = item.desc || '当前页面没有额外说明。';
  }

  function getHashKey() {
    return window.location.hash.replace(/^#/, '');
  }

  function setHashKey(key) {
    if (getHashKey() !== key) {
      window.location.hash = key;
    }
  }

  function showToast(message, type) {
    var root = document.getElementById('toastRoot');
    var toast = document.createElement('div');
    toast.className = 'toast-item' + (type === 'error' ? ' is-error' : '');
    toast.textContent = message;
    root.appendChild(toast);

    window.setTimeout(function () {
      toast.remove();
    }, 2200);
  }

  function openDialog(options) {
    dialogContent.textContent = (options && options.content) || '暂无内容。';
    dialogMask.hidden = false;
  }

  function closeDialog() {
    dialogMask.hidden = true;
  }

  function loadFallback(file, titleText, descText) {
    pageFrame.src = file;
    pagePath.textContent = '原型 / ' + titleText;
    pageTitle.textContent = titleText;
    pageDesc.textContent = descText;
    setActiveMenu('');
  }

  function verifyPageFile(file) {
    if (!canPrecheckPageFile()) {
      return Promise.resolve(true);
    }

    return window.fetch('./' + file, { cache: 'no-store' })
      .then(function (response) {
        return response.ok;
      })
      .catch(function () {
        return false;
      });
  }

  function loadPage(key) {
    var item = getMenuItem(key);

    if (!item) {
      loadFallback('./pages/404.html', '页面不存在', '未找到对应菜单配置，已回退到 404 页面。');
      showToast('未找到页面配置，已打开 404。', 'error');
      return;
    }

    verifyPageFile(item.file).then(function (isValid) {
      if (!isValid) {
        loadFallback('./pages/404.html', '页面文件不可用', '页面文件不存在或不可访问，已回退到 404 页面。');
        showToast('页面文件不可用，已打开 404。', 'error');
        return;
      }

      pageFrame.src = './' + item.file;
      updatePageHeader(item);
      setActiveMenu(item.key);
      setHashKey(item.key);
    });
  }

  function createLeafButton(item) {
    var button = document.createElement('button');
    button.type = 'button';
    button.className = 'app-menu-item';
    button.setAttribute('data-menu-key', item.key);
    button.innerHTML = '<span>' + item.label + '</span>' + (item.desc ? '<span class="app-menu-desc">' + item.desc + '</span>' : '');
    button.addEventListener('click', function () {
      loadPage(item.key);
    });
    return button;
  }

  function renderMenu(items) {
    menuRoot.innerHTML = '';

    function renderBranch(branchItems, container) {
      (branchItems || []).forEach(function (item) {
        if (item.children && item.children.length) {
          var group = document.createElement('div');
          group.className = 'app-menu-group';

          var label = document.createElement('div');
          label.className = 'app-menu-label';
          label.textContent = item.label;

          var children = document.createElement('div');
          children.className = 'app-menu-children';
          renderBranch(item.children, children);

          group.appendChild(label);
          group.appendChild(children);
          container.appendChild(group);
          return;
        }

        container.appendChild(createLeafButton(item));
      });
    }

    renderBranch(items || [], menuRoot);
  }

  function initSiteMeta() {
    siteName.textContent = menuConfig.siteName || '产品原型站点';
    siteDesc.textContent = menuConfig.siteDesc || '请在 menu.js 中维护站点说明。';
  }

  function initEvents() {
    window.addEventListener('hashchange', function () {
      var key = getHashKey();
      if (key) {
        loadPage(key);
      }
    });

    headerHelpBtn.addEventListener('click', function () {
      openDialog({
        content: '新增页面时，优先修改 menu.js 和 pages/*.html。只有明确出现站点级复用需求时，再调整 main.js 或 index.css。当前模板支持多层 children 菜单。'
      });
    });

    dialogCloseBtn.addEventListener('click', closeDialog);
    dialogMask.addEventListener('click', function (event) {
      if (event.target === dialogMask) {
        closeDialog();
      }
    });
  }

  function init() {
    initSiteMeta();
    flattenItems(menuConfig.items || []);
    renderMenu(menuConfig.items || []);
    initEvents();

    if (!flatMenu.length) {
      loadFallback('./pages/empty.html', '暂无页面', 'menu.js 里还没有可加载的页面，请先维护菜单配置。');
      return;
    }

    var initialKey = getHashKey() || menuConfig.defaultPage || flatMenu[0].key;
    loadPage(initialKey);
  }

  window.prototypeApp = {
    loadPage: loadPage,
    getMenuItem: getMenuItem,
    setActiveMenu: setActiveMenu,
    renderMenu: renderMenu,
    updatePageHeader: updatePageHeader,
    showToast: showToast,
    openDialog: openDialog,
    closeDialog: closeDialog
  };

  init();
})();
