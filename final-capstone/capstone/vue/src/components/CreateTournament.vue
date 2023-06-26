<template>
    <form class="create-tournament" v-on:submit.prevent="createTournament">
      <h1>Create New Tournament</h1>
      <div class="input-field">
      <label for="tournament-name">Tournament Name</label>
      <input id="tournament-name" type="text" v-model="tournament.tournament_name" placeholder="Tournament Name"/>
      </div>
      <div class="input-field">
      <label for="game-name">Game Name</label>
      <select id="game-name" v-model="tournament.game">
        <option value="">Select a game</option>
        <option value="League of Legends">League of Legends</option>
        <option value="Dota 2">Dota 2</option>
        <option value="Overwatch 2">Overwatch 2</option>
        <option value="Smite">Smite</option>
        <option value="Valorant">Valorant</option>
      </select>
      </div>
      <label for="match-format">Match Format</label>
        <select id="match-format" v-model="tournament.format">
        <option value="">Select a match format</option>
        <option value="Best of 3">Best of 3</option>
        <option value="Best of 5">Best of 5</option>
        <option value="Best of 7">Best of 7</option>
        <option value="Best of 9">Best of 9</option>
      </select>
      <div class="input-field">
      <label for="entry-fee">Entry Fee</label>
      <input type="text" name="entry_fee" id="entry-fee" placeholder="Entry Fee" v-model="tournament.entry_fee"/>
      </div>
      <div class="input-field">
      <label for="prize">Prize Amount</label>
      <input type="text" id="prize" v-model="tournament.prize" placeholder="Prize Amount">
      </div>
      <div class="input-field">
      <label for="date_time">Date and Time</label>
      <input type="datetime-local" id="date_time" v-model="tournament.date_and_time" placeholder="Date and Time">
      </div>
      <div>
      <div class="input-field">
          <label v-show="!online" for="location">Location</label>
          <input v-show="!online" type="text" id="location" v-model="tournament.location" placeholder="Location">
        </div>
      <div class="checkbox">
        <input type="checkbox" id="checkbox" v-on:change="online=!online">
        <label for="checkbox">Online only</label>
      </div>
      </div>
      <div class="input-field">
      <label for="description">Description</label>
      <textarea id="description" placeholder="Description" rows="5" cols="50" v-model="tournament.description"></textarea>
      </div>
      <p v-if="message.length > 0" style="color: #dd6b6b; font-size: 14px">{{ message }}</p>
      <input class="submit" type="submit" value="Submit">
    </form>
</template>

<script>
import TournamentService from '../services/TournamentService.js';
export default {
  name: "create-tournament",
  data() {
    return {
      tournament: {
        tournament_name: "",
        date_and_time: "",
        description: "",
        format: "",
        entry_fee: "",
        game: "",
        location: "",
        prize: "",
        tournament_owner: this.$store.state.user.username,
      },
      message: '',
      online: false,
    };
  },
  methods: {
    removeMessage() {
      this.message = "";
      document.removeEventListener('click', this.removeMessage)
    },
    createTournament() {
      document.removeEventListener('click', this.removeMessage)
      if (this.tournament.tournament_name == "" || this.tournament.date_and_time == "" || this.tournament.description == "" || this.tournament.entry_fee == ""
      || this.tournament.game == "" || this.tournament.prize == "" || this.tournament.format == "" || (this.tournament.location == "" && !this.online)) {
        this.message = "Please fill out all fields.";
        setTimeout(() => {
          document.addEventListener('click', this.removeMessage)
        }, 300)
        return;
      } else if (isNaN(this.tournament.entry_fee) || parseFloat(this.tournament.entry_fee) < 0|| isNaN(this.tournament.prize) || parseFloat(this.tournament.prize) < 0) {
        this.message = "Entry fee and prize need to be non-negative numbers.";
        setTimeout(() => {
          document.addEventListener('click', this.removeMessage)
        }, 300)
        return;
      }
      if (this.online) this.tournament.location = "Online";
      let date = new Date();
      let offset = -date.getTimezoneOffset();
      this.tournament.date_and_time = this.tournament.date_and_time + (offset < 0 ? "-" : "+")
      + Math.floor(Math.abs(offset) / 60).toString().padStart(2, "0") + ":" + (Math.abs(offset) % 60).toString().padStart(2, "0");
      TournamentService.create(this.tournament).then(response => {
        if (response.status == 200 || response.status == 201) {
          alert("Tournament created.");
          this.$router.push({name: 'tournament', params: {tournamentId: response.data.tournament_id}});
        }
      })
    }
  }
};
</script>
<style scoped>

.create-tournament {
  color: white;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  margin: 40px;
  gap: 10px;
}
.input-field {
  display: flex;
  flex-direction: column;
}
.input-field > input, select, textarea {
  height: 30px;
  width: 40%;
  border-radius: 3px;
  border: 0.5px solid orange;
}
textarea {
  height: auto
}
.submit {
  background-color: orange;
  width: 80px;
  border-radius: 5px;
  height: 40px;
  position: relative;
  left: 0;

}
.checkbox {
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.checkbox input {
  -moz-appearance:none;
	-webkit-appearance:none;
	-o-appearance:none;
  margin-right: 10px;
  border: 1px solid orange;
  width: 12px;
  height: 12px;
  color: transparent;
  border-radius: 2px;
  position: relative;
  
}
.checkbox input:checked:before {
  position: absolute;
  content: "\2713";
  top: -4px;
  color: lightblue;
  font-size: 12px;
}

label {
  margin-bottom: 5px;
}
label[for="checkbox"] {
  margin-bottom: 0;
}
input.submit {
  margin-top: 10px;
}

@media only screen and (max-width: 480px) {
  .input-field > input, select, textarea {
  height: 30px;
  width: 100%;
  border-radius: 3px;
  border: 0.5px solid orange;
  }
  input.submit {
    margin-left: auto;
    margin-right: auto;
  }
}
</style>
