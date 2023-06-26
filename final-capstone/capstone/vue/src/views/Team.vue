<template>
  <main class="team">
    <Navbar />
    <div class="team-layout" :class="{'member-layout': team.createdBy !== $store.state.user.username
    || tournament.tournament_id == 0 || tournament.status == 'Active' || tournament.status == 'Finished'}">
      <div class = "header">
        <h2 v-if="!editing">{{ team.teamName }}</h2>
        <div v-else><input type="text" v-model="team.teamName" /></div>
        <div class="buttons" v-if="team.createdBy === $store.state.user.username">
            <button @click="saveTeamName()" v-if="editing">Save</button>
            <button v-else @click="editing=!editing">Edit</button>
        </div>   
      </div>
      <div class="members">
      <table>
        <tr>
          <th>Role</th>
          <th>Username</th>
          <th>Action</th>
        </tr>
        <tr>
          <td>Owner</td>
          <td>{{ owner.username }}</td>
          <td class="button-cell">
            <button v-if="team.createdBy === $store.state.user.username" @click="captainLeave()">Leave</button>
          </td>
        </tr>
        <tr v-for="member in members" :key="member.id">
          <td>Member</td>
          <td>{{ member.username }}</td>
          <td class="button-cell">
            <button v-if="team.createdBy === $store.state.user.username" @click="removeMember(member)" style="margin-right: 5px">Remove</button>
            <button v-if="team.createdBy === $store.state.user.username" @click="assignCaptain(member)">Assign Captain</button>
            <button v-if="member.username === $store.state.user.username" @click="leave()">Leave</button>
          </td>
        </tr>
      </table>
      <p v-if="removeMessage.length > 0" style="color: #dd6b6b; font-size: 14px; margin-top: 10px">{{ removeMessage }}</p> 
      </div>
      <div class="requests" v-if="team.createdBy === $store.state.user.username && tournament.tournament_id != 0 && (tournament.status == 'Open' || tournament.status == 'Upcoming')">
        <h4>Team Requests</h4>
        <table v-if="joinRequests.length >= 1">
          <tr>
            <th>Username</th>
            <th>Action</th>
          </tr>
          <tr v-for="request in joinRequests" :key="request.teamRequestId">
            <td v-if="isLoaded">{{ request.username }}</td>
            <td class="button-cell">
              <button @click="acceptJoinRequest(request)" style="margin-right: 20px">Accept</button>
              <button @click="declineJoinRequest(request.teamRequestId)">Decline</button>
            </td>
          </tr>
        </table>
        <p v-else>No requests to display at the moment.</p>
        <p v-if="acceptMessage.length > 0" style="color: #dd6b6b; font-size: 14px; margin-top: 10px">{{ acceptMessage }}</p>
      </div>
      <div class="invite" v-if="team.createdBy === $store.state.user.username && tournament.tournament_id != 0 && (tournament.status == 'Open' || tournament.status == 'Upcoming')">
        <h4>Invite Member</h4>
        <input type="text" placeholder="username" v-model="invited" />
        <p v-if="inviteMessage.length > 0" style="color: #dd6b6b; font-size: 14px">{{ inviteMessage }}</p>  
        <button @click="invite">Invite</button>
      </div>
      <card :tournament="tournament" v-if="tournament.tournament_id != 0" class="card"/>
      <p v-else class="card">This team has been removed from the tournament, please consider joining or creating another team.</p>  
    </div>
  </main>
</template>

<script>
import UserService from "../services/UserService.js";
import Navbar from "../components/Navbar.vue";
import Card from "../components/Card.vue";
import TeamService from "../services/TeamService.js";
import TournamentService from "../services/TournamentService.js";
import TeamRequestService from "../services/TeamRequestService.js";

export default {
  name: "team",
  components: {
    Navbar,
    Card,
  },
  data() {
    return {
      teamId: 0,
      team: {},
      tournament: {},
      members: [],
      owner: {},
      invited: "",
      editing: false,
      joinRequests: [],
      requestCounts: 0,
      removeMessage: "",
      acceptMessage: "",
      inviteMessage: "",
    };
  },

  created() {
    this.import();
  },

  computed: {
    isLoaded() {
      return this.requestCounts == this.joinRequests.length
    }
  },

  methods: {
    import() {
      UserService.getUser(this.$store.state.user.id).then((response) => {
        this.teamId = response.data.team_id;
        this.$store.commit("IMPORT_TEAM_ID", this.teamId);
        TeamService.getTeamById(this.teamId).then((response) => {
          this.team = response.data;
          if (this.$store.state.user.username == this.team.createdBy) {
            TeamRequestService.getJoinRequests().then((response) => {
              this.joinRequests = response.data;
              this.joinRequests.forEach(request => {
                UserService.getUser(request.senderId).then((response) => {
                  request.username = response.data.username;
                  this.requestCounts++;
                })
              })
            })
          }
          UserService.getTeamMembers(this.teamId).then((response) => {
            this.members = response.data.filter(member => this.team.createdBy !== member.username);
            this.owner = response.data.find(member => this.team.createdBy === member.username);
          })
        })
        TournamentService.getByTeamId(this.teamId).then((response) => {
          this.tournament = response.data;
        })
      })
      
    },
    assignCaptain(member) {
      TeamService.assignCaptain(this.teamId, member.username).then(response => {
        if (response.status == 200 || response.status == 201) {
          this.import();
        }
      })
    },
    removeInviteMessage() {
      this.inviteMessage = "";
      document.removeEventListener('click', this.removeInviteMessage)
    },
    removeRemoveMessage() {
      this.removeMessage = "";
      document.removeEventListener('click', this.removeRemoveMessage);
    },
    removeAcceptMessage() {
      this.acceptMessage = "";
      document.removeEventListener('click', this.removeAcceptMessage);
    },
    captainLeave() {
      if (this.members.length == 0) {
        this.leave();
      } else {
        document.removeEventListener('click', this.removeRemoveMessage)
        this.removeMessage = "Please assign another member to be the owner or remove all members before leaving the team.";
        setTimeout(() => {
          document.addEventListener('click', this.removeRemoveMessage)
        }, 300)
      }
    },
    invite() {
      document.removeEventListener('click', this.removeInviteMessage)
      let stop = false;
      if (this.$store.state.user.username == this.invited) {
        this.inviteMessage = "Please don't invite yourself.";
        this.invited = "";
        stop = true;
      } else if (this.tournament.tournament_owner == this.invited) {
        this.inviteMessage = "You can't invite the tournament host."
        this.invited = "";
        stop = true;
      } else if (this.members.filter((member) => member.username == this.invited).length >= 1) {
        this.inviteMessage = "This user is already on your team.";
        this.invited = "";
        stop = true;
      } else if (this.joinRequests.filter(request => request.username == this.invited).length >= 1) {
        this.inviteMessage = "This user already requested to join your team."
        this.invited = "";
        stop = true;
      }
      if (stop) {
        setTimeout(() => {
          document.addEventListener('click', this.removeInviteMessage)
        }, 300)
        return;
      }
      TeamRequestService.invite(this.$store.state.user.id, this.invited).then(
        (response) => {
          if (response.status === 200 || response.status === 201) {
            this.inviteMessage = `You have invited ${this.invited}.`;
            this.invited = "";
          }
        }
      ).catch((error) => {
        this.inviteMessage = error.response.data.message;
        this.invited = "";
      })
      setTimeout(() => {
        document.addEventListener('click', this.removeInviteMessage)
      }, 300)
    },
    acceptJoinRequest(request) {
      TeamRequestService.acceptJoinRequest(request.teamRequestId).then((response) => {
        if (response.status == 200 || response.status == 201) {
          document.removeEventListener('click', this.removeAcceptMessage)
          this.acceptMessage = "You have accepted the join request.";
          this.members.push({id: request.senderId, username: request.username})
          this.joinRequests = this.joinRequests.filter(filteredRequest => filteredRequest.teamRequestId != request.teamRequestId)
          this.requestCounts--;
          setTimeout(() => {
            document.addEventListener('click', this.removeAcceptMessage)
          }, 300)
        }
      });
    },

    declineJoinRequest(id) {
        TeamRequestService.declineJoinRequest(id).then((response) => {
            if (response.status == 200 || response.status == 201) {
                document.removeEventListener('click', this.removeAcceptMessage)
                this.acceptMessage = "You have declined the join request.";
                this.joinRequests = this.joinRequests.filter(request => request.teamRequestId != id)
                this.requestCounts--;
                setTimeout(() => {
                  document.addEventListener('click', this.removeAcceptMessage)
                }, 300)
            }
        })
    },

    removeMember(member) {
      TeamService.removeMember(this.teamId, member.id).then((response) => {
        if (response.status === 200) {
          this.removeMessage = `You have removed ${member.username} from your team.`;
          this.members = this.members.filter((m) => m.username !== member.username);
        }
      });
    },

    leave() {
      if (this.tournament.status == 'Active') {
        document.removeEventListener('click', this.removeRemoveMessage)
        this.removeMessage = "You can't leave your team during an active tournament."
        setTimeout(() => {
          document.addEventListener('click', this.removeRemoveMessage)
        }, 300)
        return;
      }
      UserService.leaveTeam(this.team, this.tournament.status).then((response) => {
        if (response.status == 200 || response.status == 201) {
          alert("You have left your team.");
        }
        this.$router.push({ name: "home" });
      });
    },
    saveTeamName() {
      TeamService.editTeam({'teamId': this.teamId, 'teamName': this.team.teamName}).then((response) => {
          if (response.status === 200) {
            this.editing = false
          }
        })
    },
  },
};
</script>


<style scoped>
.team {
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
  color: white;
}
.team-layout {
  margin: 40px;
  display: grid;
  grid-template-areas:
    "header header card"
    "members members card"
    "requests invite card";
  grid-template-rows: 1fr 6fr min-content;
  grid-template-columns: 3fr 1.5fr 3fr;
  row-gap: 50px;
  column-gap: 20px;
}
p.card {
  width: 30rem;
}
.member-layout {
  grid-template-areas:
    "header card"
    "members card";
  grid-template-columns: 4fr 3fr;
  grid-template-rows: 0.7fr 8fr;
}
.header {
  grid-area: header;
  display: flex;
  align-items: center;
  gap: 50px;
}
.buttons {
  display: flex;
  align-items: center;
}
table {
  grid-area: members;
  background-image: linear-gradient(to bottom, red 0%, yellow 50%, orange 100%);
  background-origin: border-box;
  border-spacing: 2px;
}
table td,
table th {
  width: 200px;
  background-color: black;
  padding: 8px;
}
table td {
  padding-left: 15px;
}
.requests {
  grid-area: requests;
}
.requests > h4 {
  margin-bottom: 20px;
}
.invite {
  grid-area: invite;
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: flex-start;
}
.card {
  grid-area: card;
}
.invite button {
  background-color: lightblue;
  width: 80px;
}
.button-cell {
  padding: 0;
  text-align: center;
  min-width: 200px;
}
.member-layout .button-cell {
  min-width: 0;
}
@media (max-width: 480px) {
    .team {
        height: 100%;
        min-height: 100vh;
        overflow-y: hidden;
    }
    .team-layout {
        margin: 15px;
        margin-right: 0px;
        width: 100%;
        grid-template-areas:
            "header"
            "members"
            "requests"
            "invite"
            "card";
        row-gap: 50px;
        grid-template-columns: 1fr;
        grid-template-rows: auto;
    }
    .member-layout {
        grid-template-areas:
            "header"
            "members"
            "card";
    }
    .card {
        width: 90%;
    }
}
</style>
