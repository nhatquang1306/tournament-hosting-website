<template>
  <div class = "main">
    <div class="buttons">
        <div v-if="$store.state.user.username != tournament.tournament_owner && tournament.status == 'Open'">
            <a class="button" @click="createTeam()">Create Team</a>
            <create-team v-if="popup" @close-popup="popup = false" :tournament="tournament" :teamList="teamList"></create-team>
        </div>
        <div class="button" v-if="$store.state.user.username == tournament.tournament_owner"><a @click="generateBracket()">Generate Bracket</a></div>
        <div class="button" v-if="matches['round1'].length > 0 && tournament.status == 'Upcoming' && $store.state.user.username == tournament.tournament_owner">
            <a @click="startTournament()">Start Tournament</a>
        </div>
        <div class="button" v-if="$store.state.user.username == tournament.tournament_owner && (tournament.status == 'Active' || tournament.status == 'Finished')">
            <a @click="resetTournament()">Restart Tournament</a>
        </div>
    </div>
    <div v-if="bracketMessage != ''" style="margin-top: 10px; color: #dd6b6b; font-size: 14px">{{ bracketMessage }}</div>
    <div class="top3" v-if="tournament.status == 'Finished'">
        <div class="medal">
            <img src="../assets/images/1st.svg" alt="first-place-medal.png">
            <p>{{ first }}</p>
        </div>
        <div class="medal">
            <img src="../assets/images/2nd.svg" alt="second-place-medal.png">
            <p>{{ second }}</p>
        </div>
        <div class="medal">
            <img src="../assets/images/3rd.svg" alt="third-place-medal.png">
            <p>{{ third }}</p>
        </div>
    </div>
    
    <div class="bracket" :class="{'rematch': matches['round0'].length > 0}" v-if="matches['round1'].length > 0" >
        <div class="round" v-for="(round, key) in matches" :class="key" :key="key">
            <div class="match" v-for="match in round" :key="match.match_id">
                <div class="team" v-if="!loading" :class="{'winner1': match.winner_id == match.first_team_id && match.winner_id != 0, 'loser1': match.winner_id == match.second_team_id && match.winner_id != 0,}">
                    <p>{{ teams[match.first_team_id] == 0 ? "" : teams[match.first_team_id]}}</p>
                    <div class="score">
                        <button @click="progress(match, match.first_team_id)" class="add"
                        v-if="$store.state.user.username == tournament.tournament_owner && match.winner_id == 0 && currentRound == match.round">
                        +</button>
                        <p>{{ match.first_score }}</p>
                    </div>
                </div>
                <div class="team" v-if="!loading" :class="{'winner2': match.winner_id == match.second_team_id && match.winner_id != 0, 'loser2': match.winner_id == match.first_team_id && match.winner_id != 0,}">
                    <p>{{ teams[match.second_team_id] == 0 ? "" : teams[match.second_team_id]}}</p>
                    <div class="score">
                        <button @click="progress(match, match.second_team_id)" class="add"
                        v-if="$store.state.user.username == tournament.tournament_owner && match.winner_id == 0 && currentRound == match.round">
                        +</button>
                        <p>{{ match.second_score }}</p>
                    </div>
                </div>
                <div class="line" v-if="match.round != 4 && match.round != 0"></div><div class="line-2" v-if="match.round != 4 && match.round != 0"></div>
                <div class="final-line" v-if="match.round == 4 && matches['round0'].length > 0"></div>
            </div>
        </div>
        <div class="upper">
            <p>Quarterfinal</p><p>Semifinal</p><p>Final</p><p class="space"></p><p>Grand Final</p>
        </div>
        <div class="lower">
            <p>Round 1</p><p>Round 2</p><p>Round 3</p><p>Round 4</p>
        </div>
    </div>
    <div v-else class="teams">
    <table v-if="teamList.length > 0">
      <tr>
        <th>Team Name</th>
        <th>Owner</th>
        <th>Action</th>
      </tr>
      <tr v-for="team in teamList" :key="team.team_id">
        <td>{{team.teamName}}</td>
        <td>{{team.createdBy}}</td>
        <td class="button-cell">
            <button v-if="team.openToPublic && $store.state.user.username != tournament.tournament_owner" @click="requestToJoin(team)">Join</button>
            <p v-else-if="$store.state.user.username != tournament.tournament_owner">Private Team</p>
            <button v-if="$store.state.user.username == tournament.tournament_owner" @click="removeTeam(team)">Remove</button>
        </td>
      </tr>
    </table>
    <p v-else>There are no teams in this tournament right now.</p>
    <p v-if="joinMessage != ''" style="margin-top: 10px; color: #dd6b6b; font-size: 14px">{{ joinMessage }}</p>
    </div>
  </div>
</template>

<script>
import TeamRequestService from "../services/TeamRequestService.js"
import MatchService from "../services/MatchService.js";
import TeamService from "../services/TeamService.js";
import TournamentService from '../services/TournamentService.js';
import CreateTeam from '../components/CreateTeam.vue';
export default {
    props: {
        tournament: {
        type: Object,
        required: true,
        },
    },
    components: {
        CreateTeam
    },
    data() {
        return {
            popup: false,
            bracketMessage: "",
            joinMessage: "",
            first: null,
            second: null,
            third: null,
            loading: false,
            currentRound: null,
            matches: {},
            teams: {},
            teamList: [],
        }
    },
    created() {
        this.resetMatches();
        TeamService.getAllTeams(this.$route.params.tournamentId).then((response) => {
            this.teamList = response.data;
            this.teamList.forEach((e) => this.teams[e.teamId] = e.teamName);
            this.importMatches();    
        });        
    },
    methods: {
        resetMatches() {
            this.matches = {"round1": [], "round2": [], "round3": [], "round4": [], "round0": [],
                "round-1": [], "round-2": [], "round-3": [], "round-4": []}
        },
        resetTournament() {
            TournamentService.resetTournament(this.tournament.tournament_id).then(response => {
                if (response.status == 200 || response.status == 201) {           
                    this.resetMatches();
                    this.currentRound = null;
                    // eslint-disable-next-line vue/no-mutating-props
                    this.tournament.status = 'Upcoming';
                } 
            })
        },
        requestToJoin(team) {        
            if (this.$store.state.teamId != 0) {
                document.removeEventListener('click', this.removeJoinMessage);
                this.joinMessage = "You are already in a team. Please leave before requesting to join another team.";
            } else {
                TeamRequestService.requestToJoin(team.teamId).then(response => {
                    if (response.status == 200 || response.status == 201) {
                        this.joinMessage = `You have requested to join team ${team.teamName}.`; 
                    }
                }).catch(error => {
                    this.joinMessage = error.response.data.message;
                })
            }
            setTimeout(() => {
                document.addEventListener('click', this.removeJoinMessage)
            }, 300)
        },
        removeTeam(team) {
            TeamService.removeTeam(this.tournament, team.teamId).then(response => {
                if (response.status == 200 || response.status == 201) {
                    this.joinMessage = `You have removed team ${team.teamName} from the tournament.`;
                    if (this.tournament.status == 'Upcoming') {
                        // eslint-disable-next-line vue/no-mutating-props
                        this.tournament.status = 'Open';
                    }
                    this.teamList = this.teamList.filter(remainingTeam => remainingTeam.teamId != team.teamId)
                    delete(this.teams[team.teamId]);
                    setTimeout(() => {
                        document.addEventListener('click', this.removeJoinMessage)
                    }, 300)
                }
            })
        },
        getTop3() {
            let match = null;
            if (this.matches["round0"].length > 0) {
                match = this.matches["round0"][0];
            } else {
                match = this.matches["round4"][0];
            }
            this.first = this.teams[match.winner_id];
            this.second = this.teams[match.winner_id == match.first_team_id ? match.second_team_id : match.first_team_id]
            let thirdMatch = this.matches["round-4"][0];
            this.third = this.teams[thirdMatch.winner_id == thirdMatch.first_team_id ? thirdMatch.second_team_id : thirdMatch.first_team_id];    
        },
        importMatches() {           
            MatchService.getAllMatches(this.$route.params.tournamentId).then(response => {
                let importedMatches = response.data;
                importedMatches.forEach(match => {
                    this.matches["round" + match.round].push(match);
                })
                if (this.tournament.status == 'Finished') this.getTop3();
                if (this.tournament.status != 'Active') return;
                let arr = [1, -1, 2, -2, 3, -3, -4, 4, 0];
                for (let i = 0; i < arr.length; i++) {
                    if (this.matches["round" + arr[i]].filter(match => match.winner_id == 0).length > 0) {
                        this.currentRound = arr[i];
                        break;
                    }    
                }
            })
        },
        startTournament() {
            TournamentService.start(this.$route.params.tournamentId)
            this.currentRound = 1;
            // eslint-disable-next-line vue/no-mutating-props
            this.tournament.status = 'Active';
        },
        removeJoinMessage() {
            this.joinMessage = "";
            document.removeEventListener('click', this.removeJoinMessage) 
        },
        removeBracketMessage() {
            this.bracketMessage = "";
            document.removeEventListener('click', this.removeBracketMessage)
        },
        createTeam() {
            if (this.$store.state.teamId != 0) {
                document.removeEventListener('click', this.removeBracketMessage)         
                this.bracketMessage = "You are already in a team. Please leave before creating a new team."
                setTimeout(() => {
                    document.addEventListener('click', this.removeBracketMessage)                
                }, 300) 
            } else {
                this.popup = !this.popup;
            }
            
        },
        generateBracket() {    
            if (Object.keys(this.teams).length < 8) {
                document.removeEventListener('click', this.removeBracketMessage);
                this.bracketMessage = "There are not enough teams to generate a bracket."
                setTimeout(() => {
                    document.addEventListener('click', this.removeBracketMessage)
                }, 300)
                return;
            } else if (this.matches["round1"].length > 0) {
                document.removeEventListener('click', this.removeBracketMessage);
                this.bracketMessage = "You already have a bracket for this tournament.";
                setTimeout(() => {
                    document.addEventListener('click', this.removeBracketMessage)
                }, 300)
                return;
            }
            TournamentService.generateBracket(this.$route.params.tournamentId).then(response => {
                if (response.status == 200 || response.status == 201) {
                    this.importMatches();
                }
            })
        },
        progress(match, id) {
            let point = (parseInt(this.tournament.format.replaceAll("Best of ", "")) + 1) / 2;
            if (match.first_team_id == id) {
                match.first_score++;
            } else {
                match.second_score++;
            }
            if (match.first_score ==  point) {
                match.winner_id = match.first_team_id;
            } else if (match.second_score == point) {
                match.winner_id = match.second_team_id;
            }
            if (this.matches["round" + match.round].every(match => match.winner_id != 0)) {
                switch(this.currentRound) {
                    case 1: case 2: case 3: case -4:
                        this.currentRound = this.currentRound * (-1);
                        break;
                    case 4:
                        this.currentRound = 0;
                        break;
                    case -1: case -2:
                        this.currentRound = this.currentRound * (-1) + 1;
                        break;
                    case -3:
                        this.currentRound = -4;                  
                }
            }
            if (match.round == 4 || match.round == 0) {
                MatchService.finalMatch(this.tournament.tournament_id, match).then(response => {
                    this.loading = true;   
                    let newMatch = response.data;
                    if (newMatch) {                  
                        this.matches["round0"].push(newMatch);     
                    } else if (match.first_score == point || (match.second_score == point && match.round == 0)) {
                        // eslint-disable-next-line vue/no-mutating-props
                        this.tournament.status = "Finished";
                        this.getTop3();
                    }
                    this.loading = false;  
                })
                return;
            }
            MatchService.progressMatch(this.tournament.tournament_id, match).then(response => {
                this.loading = true;
                let newMatches = response.data, amount = 0;
                if (newMatches.length != 0) {
                    newMatches.forEach(match => {
                        let index = this.matches["round" + match.round].findIndex(e => e.match_id == match.match_id); 
                        this.matches["round" + match.round][index] = match;
                        amount++;
                    })
                }
                if (amount == newMatches.length) {
                    this.loading = false;
                }
            })
        }
    }

}
</script>

<style scoped>
.main {
    color: white;
}

.buttons {
    display: flex;
    gap: 20px;
    margin-top: 20px;
}

.button {
  border-radius: 5px;
  padding: 5px 8px;
  width: 155px;
  border: 1px solid #ff3860;
  color: #ff3860;
  cursor: pointer;
  text-align: center;
}
.match {
  border-radius: 5px;
  width: 184px;
  border: 1px solid orange;
  background-color: black;
  color: white;
  cursor: default;
  position: relative;
}
.round {
    height: 268px;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
}

.bracket {
    max-width: 72vw;
    height: 580px;
    position: relative;
    display: grid;
    overflow-x: auto;
    overflow-y: hidden;
    grid-template-areas:
        "upper upper upper upper upper ."
        "round1 round2 round3 . round4 round0"
        "lower lower lower lower round4 round0"
        "round-1 round-2 round-3 round-4 round4 round0";
    grid-template-columns: 186px 186px 186px 186px;
    grid-template-rows: 28px 268px 28px 1fr;
    grid-row-gap: 10px;
    grid-column-gap: 68px;
    margin-top: 20px;
}
.rematch {
    grid-template-columns: 186px 186px 186px 186px 186px;
}

.upper {
    grid-area: upper;
    width: 1202px;
}
.lower {
    grid-area: lower;
    width: 948px;
}
.upper, .lower {
    border: 1px solid orange;
    display: flex;
    justify-content: space-between;
    box-shadow: 0 0 5px rgba(255, 165, 0, 1);
    background-color: black;
}
.upper > p, .lower > p {
    width: 184px;
    border-left: 1px solid orange;
    border-right: 1px solid orange;
    text-align: center;
    height: 22px;
    padding: 2px 0 2px 0;
}
.upper > p:first-child, .lower > p:first-child {
    border-left: none;
}
.upper > p:last-child, .lower > p:last-child {
    border-right: none;
}
.upper > .space {
    border: none;
}
.round1 {
    grid-area: round1;
}
.round2 {
    grid-area: round2;
}
.round3 {
    grid-area: round3;
}
.round4 {
    grid-area: round4;
    height: 476px;
}
.round0 {
    grid-area: round0;
    height: 476px;
}
.round-1 {
    grid-area: round-1;
}
.round-2 {
    grid-area: round-2;
}
.round-3 {
    grid-area: round-3;
}
.round-4 {
    grid-area: round-4;
}
.round-1, .round-2, .round-3, .round-4 {
    height: 150px;
}
.line {
    position: absolute;
    left: 185px;
    top: 28px;
    border-top: 1px solid gray;
    border-right: 1px solid gray; 
    width: 35px;
    height: 33.5px;
}
.final-line {
    border-top: 1px solid gray;
    position: absolute;
    width: 69px;
    top: 28px;
    left: 185px;
}
.line-2 {
  position: absolute;
  left: 220px;
  top: 28px;
  border-bottom: 1px solid gray;
  width: 35px;
  height: 33.5px;
}
.round1 > .match:nth-child(even) > .line {
    border-top: none;
    border-bottom: 1px solid gray;
    top: -5.5px;
}
.round1 > .match:nth-child(even) > .line-2 {
    border-bottom: none;  
}
.round2 > .match:first-child > .line {
    height: 67px;
}
.round2 > .match:first-child > .line-2 {
    height: 67px;
}
.round2 > .match:last-child > .line {
    border-top: none;
    border-bottom: 1px solid gray;
    height: 67px;
    top: -39px;
}
.round2 > .match:last-child > .line-2 {
    border-bottom: none;
}
.round3 > .match > .line {
    width: 289px;
    height: 104px;
}
.round3 > .match > .line-2 {
    left: 475px;
    height: 104px;
}

.round-1 {
    position: relative;
    top: 60px;
}
.round-1 > .match > .line {
    height: 30px;
    top: -2px;
    border-top: none;
    border-bottom: 1px solid gray;
}
.round-1 > .match > .line-2 {
    height: 30px;
    top: -2px;
    border-bottom: none;
    border-top: 1px solid gray;
}
.round-2, .round-3 {
    position: relative;
    top: 30px;
}
.round-2 > .match > .line {
    height: 37.5px;
}
.round-2 > .match > .line-2 {
    height: 37.5px;
}
.round-2 > .match:last-child > .line {
    border-top: none;
    border-bottom: 1px solid gray;
    top: -9.5px;
}
.round-2 > .match:last-child > .line-2 {
    border-bottom: none;
}
.round-3 > .match > .line {
    top: -2px;
    height: 30px;
    border-top: none;
    border-bottom: 1px solid gray;
}
.round-3 > .match > .line-2 {
    top: -2px;
    height: 30px;
    border-bottom: none;
    border-top: 1px solid gray;
}
.round-4 > .match > .line {
    height: 153px;
    top: -125px;
    border-top: none;
    border-bottom: 1px solid gray;
}
.round-4 > .match > .line-2 {
    border: none;
}
.button:hover {
  transform: scale(1.1);
  transition: 0.1s;
}
.team {
    display: flex;
    justify-content: space-between;
    
}
.team:first-child {
    border-bottom: 1px solid gray;
}
.team > .score {
    border-left: 1px solid gray;
    text-align: center;
}
.team > p, .team > .score {
    padding: 3px;
    height: 22px;
    display: flex;

}
.team > .score > p {
    width: 12px;
}
.winner {
    background-color: green;
    border-radius: 0 0 5px 5px;
}
.add {
    scale: 0.6;
    cursor: pointer;
    position: relative;
    top: -1.5px;
}
.winner1, .winner2 {
    background-color: #20c768;
}
.loser1, .loser2 {
    background-color: #800000;
}
.winner1, .loser1 {
    border-radius: 4px 4px 0 0;
}
.winner2, .loser2 {
    border-radius: 0 0 4px 4px;
}
.top3 {
    display: flex;
    width: 60vw;
    border: 2px solid orange;
    box-shadow: 0 0 10px rgba(255, 165, 0, 1);
    margin: 20px 0 10px 0;
}
img {
    width: 70px;
}
.medal {
    width: 20vw;
    display: flex;
    align-items: center;
    height: 100px;
    justify-content: center;
    gap: 30px;
}
.medal:first-child {
    background-color: rgba(255, 215, 0, 0.2);
}
.medal:nth-child(even) {
    border-right: 2px solid orange;
    border-left: 2px solid orange;
    background-color: rgba(192, 192, 192, 0.2)
}
.medal:last-child {
    background-color: rgba(205, 127, 50, 0.2);
}

.medal > p {
    font-size: 25px;
}
.teams {
    margin-top: 20px;
}
table {
  background-image: linear-gradient(to bottom, red 0%, yellow 50%, orange 100%);
  background-origin: border-box;
  border-spacing: 2px;
}
table td,
table th {  
  width: 200px;
  min-width: 100px;
  background-color: black;
  padding: 8px;
}
table td {
  padding-left: 15px;
}
.button-cell {
  padding-left: 0;
  text-align: center;
}


</style>