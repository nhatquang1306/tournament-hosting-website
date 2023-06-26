<template>
  <main>
    <navbar />
    <side-bar/>
  </main>
</template>

<script>
import Navbar from "../components/Navbar.vue";
import SideBar from "../components/SideBar.vue"
export default {
    name: "side-bar-mobile",
    components: {
        Navbar, SideBar
    }
}
</script>

<style scoped>
main {
background: rgb(35, 34, 37);
  background: radial-gradient(
    circle,
    rgba(35, 34, 37, 1) 0%,
    rgba(26, 25, 30, 1) 78%
  );
  width: 100vw;
  height: 100vh;
  background-size: cover;
}
</style>