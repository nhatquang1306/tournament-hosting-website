<template>
  <div class="page">
    <navbar />
    <div class="content">
      <tournament-bracket :tournament="tournament" class="parent-bracket"/>
      <card :tournament="tournament"/>
    </div>
  </div>
</template>


<script>
import Navbar from "../components/Navbar.vue";
import TournamentBracket from "../components/TournamentBracket.vue";
import Card from "../components/Card.vue";
import TournamentService from "../services/TournamentService.js";

export default {
  components: {
    TournamentBracket,
    Navbar,
    Card
  },
  data() {
    return {
      tournament: {}
    }
  },
  created() {
    TournamentService.get(this.$route.params.tournamentId).then(response => {
      this.tournament = response.data
    })
  },
};
</script>

<style scoped>
.page {
  background: rgb(35, 34, 37);
  background: radial-gradient(
    circle,
    rgba(35, 34, 37, 1) 0%,
    rgba(26, 25, 30, 1) 78%
  );
  width: 100vw;
  max-width: 100%;
  min-height: 100vh;
  height: 100%;
  display: flex;
  flex-direction: column;
  background-size: cover;
  overflow: hidden;
}

.content {
  padding: 0 10px;
  gap: 10px;
  display: flex;
  justify-content: space-evenly;
}
.card {
  margin-top: 20px;
}
</style>
