import {host, port} from '../config.json';
//TODO add handlers for response
export default {

    getAllDevices: () =>
        fetch(`http://${host}:${port}/api/devices`, {
            headers: {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json; charset=UTF-8'
            }
        }).then(response => {
            if (response.ok) {
                return response.json();
            }
            const error = new Error(response.statusText);
            error.response = response;
            throw error;
        }),

    getDevicesWithDelay: () =>
        fetch(`http://${host}:${port}/api/devices`, {
            headers: {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json; charset=UTF-8',
                'Access-Control-Allow-Origin' : '*'
            }
        })
            .then(response => {

                //Emulate delay
                var ms = 500;
                var start = new Date().getTime();
                var end = start;
                while(end < start + ms) {
                    end = new Date().getTime();
                }

                if (response.ok) {
                    return response.json();
                }}),

    updateDevice: (device) =>
        fetch(`http://${host}:${port}/api/devices/${device.id}` , {
            method: 'PUT',
            headers: {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json; charset=UTF-8'
            },
            body: JSON.stringify(device)
        }).then(response => {
            if (response.code === 204) {
            }
        }),

    deleteDevice: (device) =>
        fetch(`http://${host}:${port}/api/devices/${device.id}` , {
            method: 'DELETE',
            headers: {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json; charset=UTF-8'
            }
        }).then(response => {
            if (response.code === 204) {
            }
        }),

    addDevice : (device) =>
        fetch(`http://${host}:${port}/api/devices/`, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json; charset=UTF-8'
            },
            body: JSON.stringify(device)
        }).then(response => {
            if (response.code === 204) {
            }
        })

}
