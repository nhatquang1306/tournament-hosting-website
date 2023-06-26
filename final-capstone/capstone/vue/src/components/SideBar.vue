<template>
<main class="side-bar">
    <div class="team-invites">
        <h2 class="header">{{invites.length}} Pending Invite{{invites.length > 1 ? 's' : ''}}</h2>
        <p v-if="message.length > 0" style="color: #dd6b6b; font-size: 14px">{{ message }}</p>
        <button v-if="invites.length > 0" @click="declineAll()">Decline All</button>
        <div v-if="isLoaded">
            <div class="card" v-for="invite in invites" :key="invite.teamRequestId">
                <h3>Tournament: {{invite.tournamentName}}</h3>
                <h5 v-if="isLoaded">Team: {{invite.team}}</h5>
                <p v-if="isLoaded">Owner: {{invite.owner}}</p>
                <div class="buttons">
                    <button @click="acceptJoinTeam(invite.teamRequestId)">Accept</button>
                    <button @click="declineJoinTeam(invite.teamRequestId)">Decline</button>
                </div>
            </div>
        </div>
    </div>
    <div></div>
</main>
</template>

<script>
import TeamService from "../services/TeamService.js"
import UserService from "../services/UserService.js"
import TournamentService from "../services/TournamentService.js"
import TeamRequestService from "../services/TeamRequestService.js"
export default {
    name: 'side-bar',
    data() {
        return {
            invites: [],
            tournamentResponses: 0,
            ownerResponses: 0,
            teamResponses: 0,
            message: '',
        }
    },
    created() {
        TeamRequestService.getTeamInvites().then(response => {
            this.invites = response.data;
            this.invites.forEach(invite => {
                TournamentService.getByTeamId(invite.teamId).then(response => { 
                    invite.tournamentName = response.data.tournament_name
                    this.tournamentResponses++;        
                })
                UserService.getUser(invite.senderId).then(response => {
                    invite.owner = response.data.username
                    this.ownerResponses++;
                })
                TeamService.getTeamById(invite.teamId).then(response => {
                    invite.team = response.data.teamName
                    this.teamResponses++;
                })
            })    
        })
    },
    computed: {
        isLoaded() {
            return this.tournamentResponses == this.invites.length && this.ownerResponses == this.invites.length && this.teamResponses == this.invites.length
        }
    },
    methods: {
        declineAll() {
            TeamRequestService.declineAll(this.$store.state.user.id).then(response => {
                if (response.status == 200 || response.status == 201) {
                    this.invites = [];
                    this.tournamentResponses = 0;
                    this.ownerResponses = 0;
                    this.teamResponses = 0;
                }
            })
        },
        removeMessage() {
            this.message = "";
            document.removeEventListener('click', this.removeMessage)
        },
        acceptJoinTeam(id) {
            if (this.$store.state.teamId != 0 && this.$store.state.teamId != null) {
                document.removeEventListener('click', this.removeMessage)
                this.message = "You're already in a team. Please leave before accepting a request."
                setTimeout(() => {
                    document.addEventListener('click', this.removeMessage)
                }, 300)
                return;
            }
            TeamRequestService.acceptJoinTeam(id).then(response => {
                if (response.status == 200 || response.status == 201) {
                    alert("You have accepted the request.")
                    this.$router.push({name: "team"})
                }
            })
        },
        declineJoinTeam(requestId) {
            TeamRequestService.declineJoinTeam(requestId).then(response => {
                if (response.status == 200 || response.status == 201) {
                    this.invites = this.invites.filter(invite => invite.teamRequestId != requestId)
                    this.tournamentResponses--;
                    this.ownerResponses--;
                    this.teamResponses--;
                }
            })
        }
    }
}
</script>

<style scoped>
.side-bar {
    overflow: auto;
    color: white;
    transform: translateX(1vw);
    border-radius: 5px;
    background-color: rgba(44, 41, 51, 0.726);
    position: relative;
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
.team-invites {
    position: absolute;
    display: flex;
    flex-direction: column;
    top: 10vh;
    gap: 10px;
}
.team-invites > p {
    margin-left: 20px;
}
h2 {
    margin-left: 30px;
}
.card {
    margin-left: 20px;
    margin-right: calc(20px + 1vw);
    display: flex;
    flex-direction: column;
    gap: 10px;
    margin-bottom: 30px;
    padding: 10px;
    background-color: rgba(128, 0, 0, 0.3);
    border: 1px solid orange;
    box-shadow: 0 0 5px orange;
    border-radius: 5px;
    max-width: 11vw;
}
button {
    background-color: lightblue;
    width: 70px;
    margin-right: 20px;
}
.team-invites > button {
    margin-left: 20px;
    margin-bottom: 10px;
    width: 100px;
}
@media (max-width: 480px) {
    .side-bar {
        height: calc(100% - 50px);
        background-image: none;
        translate: none;
        border-radius: 0;
    }
    .card {
        max-width: 100%
    }
}

</style>

