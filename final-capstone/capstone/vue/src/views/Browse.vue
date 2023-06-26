<template>
  <main>
    <div class="home">
      <navbar />
      <div class="browse-page">
        <div class="searchArea">
          <label for="search">Search</label>
          <input type="text" id="search" v-model="searchbar" />
          <label for="game">Select Game:</label>
          <select id="game" v-model="selectedGame">
            <option value="">All Games</option>
            <option v-for="game in games" :key="game" :value="game">{{ game }}</option>
          </select>
          <button @click="selectedStatus = ''">View All</button>
          <button @click="selectedStatus = 'open'">Open</button>
          <button @click="selectedStatus = 'upcoming'">Upcoming</button>
          <button @click="selectedStatus = 'inprogress'">In Progress</button>
          <button @click="selectedStatus = 'finished'">Finished</button>
        </div>
        <div class="searchResults">
          <card
            v-for="tournament in filteredTournaments"
            :key="tournament.tournament_id"
            :tournament="tournament"
          />
        </div>
        <div class="sidebar">
          <side-bar></side-bar>
        </div>
      </div>
    </div>
  </main>
</template>


<script>
import SideBar from "../components/SideBar.vue";
import TournamentService from "../services/TournamentService.js";
import Card from "../components/Card.vue";
import Navbar from "../components/Navbar.vue";

export default {
  components: { Card, Navbar, SideBar },
  name: "home",
  data() {
    return {
      searchbar: "",
      selectedGame: "", // Selected game from the dropdown
      games: [
        "League of Legends",
        "Overwatch 2",
        "Valorant",
        "Smite",
        "Dota 2",
      ],
      selectedStatus: "",
    };
  },
  created() {
    this.importTournaments();
    this.selectedGame = this.$store.state.game;
    this.$store.commit('SET_GAME', '');
  },
  computed: {
    filteredTournaments() {
      let tournaments = this.$store.state.tournaments;
      if (this.selectedGame !== "") {
        tournaments = tournaments.filter((tournament) => tournament.game === this.selectedGame);
      }
      if (this.searchbar !== "") {
        tournaments = tournaments.filter((tournament) => tournament.tournament_name.toLowerCase().includes(this.searchbar.toLowerCase()));
      }
      if (this.selectedStatus === "inprogress") {
        tournaments = tournaments.filter((tournament) => tournament.status === "Active");
      } else if (this.selectedStatus === "finished") {
        tournaments = tournaments.filter((tournament) => tournament.status === "Finished");
      } else if (this.selectedStatus === "open") {
        tournaments = tournaments.filter((tournament) => tournament.status === "Open");
      } else if (this.selectedStatus === "upcoming") {
        tournaments = tournaments.filter((tournament) => tournament.status === "Upcoming");
      }
      return tournaments;
    },
  },

  methods: {
    importTournaments() {
      TournamentService.getAll().then((response) => {
        this.$store.commit("IMPORT_TOURNAMENTS", response.data);
      });
    }
  },
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
  min-height: 100vh;
  height: 100%;
  background-size: cover;
  overflow: hidden;
}
/* ------------------------------------- */
.browse-page {
  display: grid;
  grid-template-areas:
    "search sidebar"
    "results sidebar";
  gap: 5px;
  margin: 5px;
  min-height: calc(100vh - 60px);
  grid-template-columns: 1fr 15vw;
}
.sidebar {
  grid-area: sidebar;
}

.searchResults {
  display: flex;
  flex-wrap: wrap;
  flex-direction: row;
  width: auto;
  height: 100%; /* Adjust the value as needed */
  padding-left: 5vh;
  
  grid-area: results;
}

.searchResults::-webkit-scrollbar {
  display: none;
}
.card {
  margin: 10px;
}

input,
select,
button {
  border-radius: 14px;
  background-image: radial-gradient(
      circle at 100% 100%,
      transparent 8px,
      #ff0000 8px,
      #ff0000 10px,
      transparent 10px
    ),
    linear-gradient(to right, #ff0000, #eeff00),
    radial-gradient(
      circle at 0% 100%,
      transparent 8px,
      #eeff00 8px,
      #eeff00 10px,
      transparent 10px
    ),
    linear-gradient(to bottom, #eeff00, #ff531a),
    radial-gradient(
      circle at 0% 0%,
      transparent 8px,
      #ff531a 8px,
      #ff531a 10px,
      transparent 10px
    ),
    linear-gradient(to left, #ff531a, #ffa200),
    radial-gradient(
      circle at 100% 0%,
      transparent 8px,
      #ffa200 8px,
      #ffa200 10px,
      transparent 10px
    ),
    linear-gradient(to top, #ffa200, #ff0000);
  background-size: 10px 10px, calc(100% - 20px) 2px, 10px 10px,
    2px calc(100% - 20px);
  background-position: top left, top center, top right, center right,
    bottom right, bottom center, bottom left, center left;
  background-repeat: no-repeat;
}
.searchArea {
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
  grid-area: search;
}

button {
  height: 25px;
}
select#game {
  height: 23px;
}
input#search {
  height: 20px;
}

@media only screen and (max-width: 480px) {
  div.searchArea {
    display: flex;
    flex-direction: column;
    align-items: stretch;
    /* position: relative; */
  }
  .searchResults {
    overflow: unset;
  }
  button {
    height: 30px;
  }
  select#game {
    height: 30px;
  }
  input#search {
    height: 30px;
  }
  .side-bar {
    display: none;
  }
  .browse-page {
    display: block;
    grid-template-areas:
      "search"
      "results";
    gap: 5px;
    margin: 5px;
    height: 100%;
    grid-template-columns: 1fr;
    max-width: 480px;
    overflow-y: auto;
  }
  label {
    font-size: 16px;
    font-weight: normal;
    color: #eed709;
  }
  div.searchResults{
    padding: 0px;
    margin-left: 1rem;
    /* margin-right: auto; */
  }
}
</style>
