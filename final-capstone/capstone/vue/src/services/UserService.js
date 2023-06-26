import axios from 'axios';
export default {
    getUser(id) {
        return axios.get(`/user/${id}`)
    },
    getTeamMembers(id) {
        return axios.get(`/users/${id}`)
    },
    leaveTeam(team, status) {
        return axios.put(`/user/leave/${status}`, team)
    }
}