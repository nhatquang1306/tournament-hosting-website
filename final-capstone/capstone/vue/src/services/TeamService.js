import axios from 'axios';

export default {
    createTeam(tournamentId, team, openToPublic) {
        return axios.post(`tournaments/${tournamentId}/addTeam?openToPublic=${openToPublic}`, team)
    },
    getAllTeams(tournamentId) {
        return axios.get(`tournaments/${tournamentId}/allTeams`)
    },
    findTeamsByName(tournamentId, searchTerm) {
        return axios.get(`tournaments/${tournamentId}/filter`, searchTerm)
    },
    getTeamById(teamId) {
        return axios.get(`/teams/${teamId}`)
    },
    removeMember(teamId, userId){
        return axios.delete(`/teams/${teamId}/members/${userId}`)
    },
    editTeam(team) {
        return axios.put('/teams/editTeam', team)
    },
    searchTeams(id) {
        return axios.get(`/tournaments/${id}/filterOpen?searchTerm=`)
    },
    removeTeam(tournament, teamId) {
        return axios.put(`teams/${teamId}/remove`, tournament)
    },
    assignCaptain(teamId, username) {
        return axios.put(`teams/${teamId}/assign?username=${username}`)
    }
   
}