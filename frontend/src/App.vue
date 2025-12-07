<template>
  <div>
    <Header></Header>
    <router-view></router-view>
  </div>
</template>

<script setup>
  import { RouterView } from 'vue-router';
  import Header from './components/common/Header.vue';
</script>

<style scoped>

</style>
