<template>
  <main>
    <navbar />
    <create-tournament class="create-tournament" />
  </main>
</template>

<script>
import Navbar from "../components/Navbar.vue";
import CreateTournament from "../components/CreateTournament.vue"
export default {
  name: "create-tournament-view",
  components: {
    Navbar,
    CreateTournament
  },
  data() {
    return {
      tournament: {
        tournament_name: "",
        date_and_time: "",
        description: "",
        entry_fee: "",
        game: "",
        location: "",
        prize: "",
        tournament_owner: this.$store.state.user.username,
      },
      online: false,
    };
  }
};
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
  max-width: 100%;
  height: 100%;
  min-height: 100vh;
  background-size: cover;
  overflow: hidden;
}
.create-tournament {
  height: 80%;
}
</style>
