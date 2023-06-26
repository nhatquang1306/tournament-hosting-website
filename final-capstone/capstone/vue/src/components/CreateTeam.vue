<template>
  <form v-on:submit.prevent="createTeam()" class="create-team">
    <button @click.prevent="closePopup" class="close">
        <i class="fas fa-times"></i>
    </button>
    <div>
      <label for="name">Team Name</label>
      <input type="text" id="name" placeholder="Team Name" v-model="team.teamName"/>
    </div>
    <div>
      <label for="open-to-public">Open to Public?</label>
      <input type="checkbox" id="open-to-public" v-on:change="openToPublic = !openToPublic"/>
    </div>
    <div>
      <p style="color: #dd6b6b; font-size: 14px">{{ message }}</p>
    </div>
    <input type="submit" value="Submit" />
  </form>
</template>

<script>
import TeamService from "../services/TeamService.js";
export default {
  name: "create-team",
  props: {
    tournament: {
      type: Object,
    },
    teamList: {
      type: Array,
    }
  },
  data() {
    return {
      team: {
        teamName: "",
      },
      message: "",
  
      openToPublic: false,
    };
  },
  methods: {
    removeMessage() {
      this.message = "";
      document.removeEventListener('click', this.removeMessage)
    },
    createTeam() {
        document.removeEventListener('click', this.removeMessage)
        if (this.team.teamName === "") {
          this.message = "Please input team name.";
          setTimeout(() => {
            document.addEventListener('click', this.removeMessage)
          }, 300)
          return;
        } else if (this.teamList.filter(team => team.teamName == this.team.teamName).length > 0) {
          this.message = `There is already a team ${this.team.teamName} in the tournament.`;
          setTimeout(() => {
            document.addEventListener('click', this.removeMessage)
          }, 300)
          return;
        }
        TeamService.createTeam(this.tournament.tournament_id, this.team, this.openToPublic).then((response) => {
            if (response.status == 200 || response.status == 201) {
              alert("Team created for tournament.");
              this.$router.push({name: "team"})
            }
        }).catch(error => {
          this.message = error.response.data.message;
        })    
        
    },
    closePopup() {
      this.$emit('close-popup')
    }
  },
};
</script>

<style scoped>
.close {
  position: absolute;
  top: 10px;
  right: 10px;
  transform: scale(0.8);
  cursor: pointer;
}

.create-team {
  width: 19.5rem;
  position: relative;
  z-index: 1;
  margin-top: 20px;
  padding-top: 30px;
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
  border-radius: 10px;
  background-color: rgba(0, 0, 0, 0.8);
  color: white;
}
.create-team > div{
    margin: 20px;
}
label {
    margin-right: 5px;
}
input[type="submit"] {
    margin-bottom: 30px;
    position: relative;
    left: 40%;
    width: 20%;
    background-color: lightblue;
}

</style>