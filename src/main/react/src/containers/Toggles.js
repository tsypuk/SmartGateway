import React, {Component} from 'react';
import Toggle from 'material-ui/Toggle';
import deviceService from '../services/deviceService';
import Paper from 'material-ui/Paper';

import {TableRow, TableRowColumn} from 'material-ui/Table';

export default class Toggles extends Component {

    constructor(props) {
        super(props);
        this.state = {
            loading: true,
            devices: []
        }
        this.reloadDevices = this.reloadDevices.bind(this);
        this.trigger = this.trigger.bind(this);
    }

    componentDidMount() {
        this.reloadDevices();
    }

    trigger(deviceId, status) {
        this.setState({loading: true});
        deviceService.trigger(deviceId, status)
            .then(loaded =>
                      this.reloadDevices());
    }

    reloadDevices() {
        this.setState({
                          loading: true,
                          devices: []
                      });
        deviceService.getAllDevices()
            .then(loaded =>
                      this.setState(
                          {
                              devices: loaded,
                              loading: false
                          }
                      ));
    }

    render() {
        const {devices, loading} = this.state;
        return (
            (loading) ? <div>Loading...</div> : <div width="100px">
                <Paper zDepth={2}>
                    {
                        devices.map(
                            device => <ToggleBlock device={device} key={device.id} trigger={this.trigger}/>
                        )
                    }
                </Paper>
            </div>
        );
    }
}

export class ToggleBlock extends Component {
    constructor(props) {
        super(props);
        this.handleSwitch = this.handleSwitch.bind(this);
    }

    handleSwitch() {
        this.props.trigger(this.props.device.id,
                           this.props.device.state === "ON" ? "OFF" : "ON");
    }

    render() {
        const {state, name} = this.props.device;

        return (
            <TableRow>
                <TableRowColumn>{state}</TableRowColumn>
                <TableRowColumn>
                    <Toggle onToggle={this.handleSwitch}
                            toggled={((state === "OFF")) ? true : false}
                    />
                </TableRowColumn>
                <TableRowColumn>{name}</TableRowColumn>
            </TableRow>
        );
    }
}
