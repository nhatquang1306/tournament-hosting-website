<template>
  <div class="card">
    <div class="thumbnail">
      <router-link :to="{name: 'tournament', params: {tournamentId: tournament.tournament_id}}">
        <img :src="require('@/assets/images/' + thumbnails[tournament.game])" alt="Tournament Thumbnail"/>
      </router-link>
    </div>
    <div class="paddedBottom">
    <div class="details">
      <h2 class="tournament-name">{{ tournament.tournament_name }}</h2>
      <p class="tournament-time">{{ time }}</p>
      <p class="tournament-description">{{ tournament.description }}</p>
    </div>
    <div class="buttons">
      <button class="status-button">{{tournament.status}}</button>
      <button class="format-button">{{ tournament.format }}</button>
      <button class="location-button">{{ tournament.location }}</button>
    </div>
  </div>
  </div>
</template>

<script>
export default {
  props: {
    tournament: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      thumbnails: {
        "Smite": "SmiteThumbnail.png",
        "Dota 2": "DOTAThumbnail.png",
        "League of Legends": "LeagueOfLegendsThumbnail.png",
        "Overwatch 2": "OverwatchThumbnail.png",
        "Valorant": "ValorantThumbnail.png",
      },
      showForm: false,
      time: "",
    };
  },
  mounted() {
    setTimeout(() => {
      let date = new Date(this.tournament.date_and_time);
      this.time = date.toLocaleString();
    }, 300)
    
  },
  watch: {
    tournament: {
      immediate: true,
      handler(prop) {
        let date = new Date(prop.date_and_time);
        this.time = date.toLocaleString();
      }
    }
  }
};
</script>

<style scoped>
.details {
  min-height:9rem;
}
.card {
  color: white;
  position: relative;
  display: flex;
  flex-direction: column;
  background-color: #1d1b22;
  /* padding: 20px; */
  border-radius: 4px;
  width: 22rem;
  max-height: 27rem;
  -webkit-box-shadow: 0px 0px 0px 1px rgba(230,165,0,1);
  -moz-box-shadow: 0px 0px 0px 1px rgba(230,165,0,1);
  box-shadow: 0px 0px 0px 1px rgba(230,165,0,1);

}
.paddedBottom {
  padding: 20px;
  padding-top: 0px;
}
.thumbnail img {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  position:relative;
  height: auto;
  object-fit: cover;
  border-radius: 4px 4px 0 0;
}

.tournament-name {
  font-size: 20px;
  font-weight: bold;
  margin: 10px 0;
}

.tournament-time {
  font-size: 14px;
  color: rgb(167, 167, 167);
  margin-bottom: 10px;
}

.tournament-description {
  margin-bottom: 20px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: auto;
}

.buttons {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.status-button,
.format-button,
.location-button {
  padding: 8px 10px;
  border: none;
  border-radius: 4px;
  background-color: #333;
  color: #fff;
  cursor: pointer;
}
</style>
