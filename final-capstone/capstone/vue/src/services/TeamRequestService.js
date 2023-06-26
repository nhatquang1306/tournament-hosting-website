import axios from 'axios';

export default {
    getTeamInvites() {
        return axios.get("/requests/")
    },
    acceptJoinTeam(id) {
        return axios.post(`/requests/${id}/accept`)
    },
    declineJoinTeam(id) {
        return axios.post(`/requests/${id}/decline`)
    },
    declineAll(userId) {
        return axios.put(`/requests/${userId}/decline-all`)
    },
    invite(sender, receiver) {
        return axios.post(`/requests?senderId=${sender}&receiverUsername=${receiver}`)
    },
    getJoinRequests() {
        return axios.get('/requests/join-requests')
    },
    acceptJoinRequest(id) {
        return axios.put(`/join-requests/${id}/accept`)
    },
    declineJoinRequest(id) {
        return axios.put(`/join-requests/${id}/decline`)
    },
    requestToJoin(id) {
        return axios.post(`/teams/${id}/join-request`)
    }
}