import { defineStore } from 'pinia'
import { shallowRef } from 'vue'

export const useNavigationStore = defineStore('navigation', () => {
  const collapsed = shallowRef(false)

  function setCollapsed(value: boolean) {
    collapsed.value = value
  }

  function toggleCollapsed() {
    collapsed.value = !collapsed.value
  }

  return {
    collapsed,
    setCollapsed,
    toggleCollapsed,
  }
})
