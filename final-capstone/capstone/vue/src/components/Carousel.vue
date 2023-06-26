<template>
  <div>
    <carousel-3d :autoplay="true" :autoplay-timeout="3000">
      <slide v-for="(game, index) in games" :key="index" :index="index" :class="slide">
        <img :src="game.thumbnail" alt="Game Thumbnail" @click="goToBrowse(game, index)"/>
        <div class="descriptionArea">
          <h1>{{ game.name }}</h1>
        </div>
      </slide>
    </carousel-3d>
  </div>
</template>

<script>
import { Carousel3d, Slide } from "vue-carousel-3d";

export default {
  components: {
    Carousel3d,
    Slide,
  },
  data() {
    return {
      currentSlideIndex: 0,
      games: [
        {
          name: "Overwatch 2",
          thumbnail: require("@/assets/images/OverwatchThumbnail.png"),
          description: "Description of Overwatch 2",
        },
        {
          name: "League of Legends",
          thumbnail: require("@/assets/images/LeagueOfLegendsThumbnail.png"),
          description: "Description of League of Legends",
        },
        {
          name: "Dota 2",
          thumbnail: require("@/assets/images/DOTAThumbnail.png"),
          description: "Description of Dota 2",
        },
        {
          name: "Valorant",
          thumbnail: require("@/assets/images/ValorantThumbnail.png"),
          description: "Description of Valorant",
        },
        {
          name: "Smite",
          thumbnail: require("@/assets/images/SmiteThumbnail.png"),
          description: "Description of Smite",
        },
      ],
    };
  },
  methods: {
    goToBrowse(game, index) {
      if (this.currentSlideIndex == index) {
        this.$store.commit('SET_GAME', game.name)
        this.$router.push({name: 'browse'})
      }
      this.currentSlideIndex = index;
      
    }
  }
};
</script>

<style>
.carousel-3d-container {
  position: relative !important;
  overflow: visible !important;
  display: flex !important;
  justify-content: center !important;
}
.carousel-3d-container * {
  text-align: center !important;
}
.carousel-3d-container div {
  border-radius: 10px !important;
  height: 18rem !important;
  width: 22rem !important;
}
.carousel-3d-container img {
  border-radius: 10px 10px 0 0 !important;
  cursor: pointer;
}

.carousel-3d-slide.current {
  color: rgb(255, 255, 255);
  background-color: rgb(30, 26, 34);
    box-shadow: 0 0 10px 5px #e0a348;
    border: solid rgb(255, 204, 128) 1px !important;
}
</style>
