import React, {Component} from 'react';
import Device from "./Device";
import deviceService from "../services/deviceService"
import {CircularProgress} from 'material-ui/Progress';

export default class DeviceList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            devices: [],
            loading: false
        }
    }

    componentDidMount() {
        this.setState({loading: true})
        deviceService.getAllDevices()
            .then(devices => this.setState(
                {
                    devices: devices,
                    loading: false
                }
            ))
    }

    render() {
        const { devices, loading } = this.state;
        return (
            (loading) ?
            <div>Loading...<CircularProgress size={60} thickness={7} /></div> :
            <div>List of connected devices:
            <table>
                <tbody>
                <tr>
                    <th>uuid</th>
                    <th>name</th>
                    <th>ip</th>
                    <th>port</th>
                    <th></th>
                    <th></th>
                </tr>
                {
                    devices.map(device => <Device device={device} key={device.id}/>)
                }
                </tbody>
            </table>
            </div>
        )
    }
}
