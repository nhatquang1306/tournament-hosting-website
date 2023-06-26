import axios from 'axios';

export default {
    getAll() {
        return axios.get('/tournaments/');
    },
    get(id) {
        return axios.get(`/tournaments/${id}`);
    },
    getByTeamId(id) {
        return axios.get(`/tournaments/team/${id}`)
    },
    getByGame(game) {
        return axios.get(`/tournaments/${game}`)
    },
    getOpen() {
        return axios.get('/tournaments/status/open');
    },
    getUpcoming() {
        return axios.get('/tournaments/status/upcoming')
    },
    getInProgress() {
        return axios.get('/tournaments/status/active');
    },
    getFinished() {
        return axios.get('/tournaments/status/finished');
    },
    create(tournament) {
        return axios.post('/tournaments/create', tournament);
    },
    generateBracket(id) {
        return axios.post(`/tournaments/${id}/bracket`);
    },
    update(id) {
        return axios.put(`/tournaments/${id}/update`);
    },
    cancel(id) {
        return axios.put(`/tournaments/${id}/cancel`);
    },
    start(id) {
        return axios.put(`/tournaments/${id}/start`);
    },
    getFeatured(number) {
        return axios.get(`/tournaments/featured/${number}`)
    },
    resetTournament(id) {
        return axios.put(`/tournaments/${id}/reset`)
    }

}