import axios from 'axios';

export default {
    getAllMatches(id) {
        return axios.get(`/tournaments/${id}/matches`)
    },
    progressMatch(id, match) {
        return axios.put(`/tournaments/${id}/match`, match)
    },
    finalMatch(id, match) {
        return axios.put(`/tournaments/${id}/final`, match)
    },
}