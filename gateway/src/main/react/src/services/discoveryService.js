import {host, port} from '../config.json';

export default {

    trigger : (discovery) => {
        fetch(`/api/discovery/`, {
            method: 'POST',
            headers: {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json; charset=UTF-8'
            },
            body: JSON.stringify(discovery)
        }).then(response => {
            if (response.ok) {
                return response.json();
            }
            const error = new Error(response.statusText);
            error.response = response;
            throw error;
        })
    }
}
