const { chromium } = require('playwright')

;(async () => {
  const browser = await chromium.launch({ headless: true })
  const page = await browser.newPage()

  // Go directly to the dictionary page so the submenu is already expanded
  await page.goto('http://localhost:5173/#/system/dictionary', { timeout: 10000 })
  await page.waitForSelector('.arco-layout', { timeout: 10000 })
  console.log('1. LOADED:', page.url())

  // Check breadcrumb
  const breadText = await page.locator('[data-testid="layout-breadcrumb"]').textContent()
  console.log('2. BREADCRUMB:', breadText?.trim())

  // Check menu icons exist
  const iconCount = await page.locator('[data-testid="menu-icon"]').count()
  console.log('3. MENU ICONS:', iconCount)

  // Check submenu is expanded
  const submenuCount = await page.locator('.arco-sub-menu').count()
  console.log('4. SUBMENU COUNT:', submenuCount)

  // Find the dictionary menu item that's selected
  const selectedItem = await page.locator('.arco-menu-selected').textContent()
  console.log('5. SELECTED MENU:', selectedItem?.trim())

  // Find and click the home menu item
  const homeLinks = page.locator('a[data-testid="menu-item-home"]')
  const homeLinkCount = await homeLinks.count()
  console.log('6. HOME LINK COUNT:', homeLinkCount)

  if (homeLinkCount > 0) {
    await homeLinks.first().click()
    await page.waitForTimeout(800)
    console.log('7. AFTER HOME CLICK:', page.url())
    console.log('7. ROUTE OK:', page.url().includes('#/') && !page.url().includes('dictionary'))
  } else {
    console.log('7. SKIP: no home link found')
  }

  // Test collapse/expand
  const collapseBtn = page.locator('[data-testid="layout-collapse-trigger"]')
  if (await collapseBtn.isVisible()) {
    await collapseBtn.click()
    await page.waitForTimeout(600)
    const collapsedTitle = (await page.locator('[data-testid="sidebar-title"]').textContent())?.trim()
    console.log('8. COLLAPSED TITLE:', collapsedTitle)
    console.log('8. IS AL:', collapsedTitle === 'AL')

    await collapseBtn.click()
    await page.waitForTimeout(600)
    const expandedTitle = (await page.locator('[data-testid="sidebar-title"]').textContent())?.trim()
    console.log('9. EXPANDED TITLE:', expandedTitle)
    console.log('9. IS 公司培训项目:', expandedTitle === '公司培训项目')
  }

  // Check user panel
  const userPanelText = (await page.locator('[data-testid="layout-user-panel"]').textContent())?.trim()
  console.log('10. USER PANEL:', userPanelText)
  console.log('10. HAS ADMIN:', userPanelText?.includes('管理员') || false)

  await browser.close()
  console.log('\n=== ALL DONE ===')
})()
