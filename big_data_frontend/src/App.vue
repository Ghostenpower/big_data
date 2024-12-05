<template>
  <el-container>
    <el-affix :offset="0">
      <el-header>
        <el-menu :default-active="currentRoute" class="el-menu-demo" mode="horizontal" @select="handleSelect">
          <el-menu-item index="/">
            首页
          </el-menu-item>
          <el-menu-item index="/stocklist">
            股票列表
          </el-menu-item>
          <el-menu-item index="/stockdetail">
            股票详情
          </el-menu-item>
          <el-sub-menu index="3">
            <template #title>Workspace</template>
            <el-menu-item index="3-1">item one</el-menu-item>
            <el-menu-item index="3-2">item two</el-menu-item>
            <el-menu-item index="3-3">item three</el-menu-item>
          </el-sub-menu>
          <el-menu-item index="4" disabled>Info</el-menu-item>
          <el-menu-item index="/about">
            关于
          </el-menu-item>
        </el-menu>
      </el-header>
    </el-affix>

    <el-main min-width="1280px">
      <router-view></router-view>
    </el-main>
    <el-footer>Footer</el-footer>
  </el-container>


</template>

<script lang="ts" setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

// 获取路由实例
const router = useRouter()
const route = useRoute()

const currentRoute = ref("")
onMounted(() => {
  setTimeout(() => {
    currentRoute.value = route.path
  }, 200)
})


const handleSelect = (idx) => {
  currentRoute.value = idx
  router.push(currentRoute.value)
}

watch(() => route.path, (newValue, _oldValue) => {
  currentRoute.value = newValue
})


</script>

<style>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}

.el-header {
  padding: 0;
}
</style>
