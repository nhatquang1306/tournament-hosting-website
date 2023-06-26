<template>
  <main class="home">
    <navbar />
    <div class="main-page">
      <carousel/>
      <div class="descriptionArea">
        <h1>Featured Games</h1>
        <p class="description">Take a moment to check out these featured games and discover the exciting tournaments happening right now. Whether you're a seasoned gamer or just getting started, these tournaments offer a chance to showcase your skills and join in on the fun. Get ready to engage in thrilling matches and compete against fellow gamers from around the world. Don't miss out on this opportunity to showcase your talents and make your mark in the gaming world!</p>
      </div>
    <div class="heading">
      <h1> Featured Tournaments</h1>
      </div>
      <div class = "featuredCards">
        <card v-for="tournament in featuredTournaments" :key="tournament.tournament_id" :tournament="tournament" />
      </div>
      <side-bar></side-bar>
    </div>
  </main>
  </template>
  
  <script>
  import TournamentService from "../services/TournamentService.js";
  import Navbar from "../components/Navbar.vue";
  import Card from "../components/Card";
  import SideBar from "../components/SideBar.vue"
  import carousel from "../components/Carousel.vue"
  export default {
    components: { Navbar, Card, SideBar, carousel },
    name: "home",
    data() {
      return {
        searchbar: "",
        featuredTournaments: [],
      };
    },
    created() {
      TournamentService.getFeatured(4).then(response => {
        this.featuredTournaments = response.data;
      })
      
    },
    computed: {
      filteredTournaments() {
        if (this.searchbar === "") {
          return this.$store.state.tournament;
        }
        const searchQuery = this.searchbar.toLowerCase();
        return this.$store.state.tournament.filter((eachTournament) => {
          const tournamentName = eachTournament.name.toLowerCase();
          return tournamentName.includes(searchQuery);
        });
      },
      filteredTeams() {
        const teams = this.$store.state.teams;
        if (this.searchbar === "") {
          return teams.filter((team) => team.isOpenToMembers);
        }
        const searchQuery = this.searchbar.toLowerCase();
        return teams.filter((team) => {
          const teamName = team.name.toLowerCase();
          return team.isOpenToMembers && teamName.includes(searchQuery);
        });
      },
    },
  };
  </script>
  <style scoped>
  .home {
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
    display: flex;
    flex-direction: column;
    overflow: hidden;
  }
  
  .main-page {
    flex: 1;
    display: grid;
    grid-template-areas:
      "carousel descriptionArea sidebar"
      "heading heading sidebar"
      "feature feature sidebar";
    grid-template-columns: 6fr 2fr 15vw;
    gap: 5px;
    margin: 5px;
  }
  
  .featuredCards {
    grid-area: feature;
    display: flex;
    justify-content: center;
    align-items: flex-start;
    flex-wrap: wrap;
    max-height: calc(100% - 70px); /* Adjust as needed */
    overflow-y: visible;
  }
  
  .card {
    margin: 20px;
  }
  
  .heading {
    color: white;
    grid-area: heading;
    margin: 30px 0 0 10vw;
  }
  
  .side-bar {
    grid-area: sidebar;
    position: relative;
  }
  
  .descriptionArea {
    position: relative;
    grid-area: descriptionArea;
    display: flex;
    flex-direction: column;
    text-align: center;
    align-items: center;
    justify-content: center;
    color: white;
    padding: 0px;
  }
  .description {
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 9;
    overflow-y: auto;
    overflow-x: hidden;
    max-height: 12rem;
  
  }
  @media (max-width: 480px) {
    .side-bar {
      display: none;
    }
    .main-page {
      display: grid;
      grid-template-areas:
        "carousel"
        "descriptionArea"
        "heading"
        "feature";
      grid-template-columns: 1fr;
      gap: 5px;
      margin: 0;
      width: 100vw;
      height: 100%;
      overflow-y: visible;
    }
    .heading {
      margin-left: 0;
      margin-bottom: 5px;
      display: flex;
      justify-content: center;
    }
    .descriptionArea {
      margin: 10px 15px 10px 15px;
      
    }
    .card {
      margin: 0px auto 10px auto;
    }
  }
  </style>
  