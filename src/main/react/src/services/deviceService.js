export default {

    getAllDevices: () =>
        fetch(`http://localhost:8080/api/devices`, {
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
        fetch(`http://localhost:8080/api/devices`, {
            headers: {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json; charset=UTF-8',
                'Access-Control-Allow-Origin' : '*'
            }
        })
            .then(response => {

                //Emulate delay
                var ms = 3000;
                var start = new Date().getTime();
                var end = start;
                while(end < start + ms) {
                    end = new Date().getTime();
                }

                if (response.ok) {
                    return response.json();
                }}),

    updateDevice: (device) =>
        fetch(`http://localhost:8080/api/devices/${device.id}` , {
            method: 'PUT',
            headers: {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json; charset=UTF-8'
            },
            body: JSON.stringify(device)
        }).then(response => {
            if (response.code === 204) {
                console.log('updated');
            }
        }),

    addDevice : (device) => {
        fetch(`http://localhost:8080/api/devices/`, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json; charset=UTF-8'
            },
            body: JSON.stringify(device)
        }).then(response => {
            if (response.code === 204) {
                console.log('updated');
            }
        })
    }

}
