import {host, port} from '../config.json';

export default {

    trigger : (boot) => {
        console.log(boot);
        fetch(`http://${host}:${port}/api/boot/`, {
            method: 'POST',
            headers: {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json; charset=UTF-8'
            },
            body: JSON.stringify(boot)
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
