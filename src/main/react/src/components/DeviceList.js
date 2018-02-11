import React, {Component} from 'react';
import Device from "./Device";
import deviceService from "../services/deviceService"
import Divider from 'material-ui/Divider';
import Paper from 'material-ui/Paper';
import {
    Table,
    TableBody,
    TableHeader,
    TableHeaderColumn,
    TableRow,
} from 'material-ui/Table';

export default class DeviceList extends Component {

    constructor(props) {
        super(props);
        this.state =
            {
                devices: [],
                loading: false,
                isMounted: false
            };
        this.reloadDevices = this.reloadDevices.bind(this);
    }

    componentDidMount() {
        this.reloadDevices();
    }

    componentWillUnmount() {
        this.setState({isMounted: false})
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
        console.log(devices);
        return (
            (loading) ? <div>Loading...</div> : <div>

                <Paper zDepth={2}>
                    Registered devices
                    <Table>
                        <TableHeader>
                            <TableRow>
                                <TableHeaderColumn>UUID</TableHeaderColumn>
                                <TableHeaderColumn>NAME</TableHeaderColumn>
                                <TableHeaderColumn>IP</TableHeaderColumn>
                                <TableHeaderColumn>PORT</TableHeaderColumn>
                                <TableHeaderColumn>ACTION</TableHeaderColumn>
                                <TableHeaderColumn>ACTION</TableHeaderColumn>
                            </TableRow>
                        </TableHeader>
                        <TableBody>
                            {
                                devices.map(
                                    device => <Device device={device} key={device.id} onDelete={this.reloadDevices}/>)
                            }
                        </TableBody>
                    </Table>
                    <Divider/>
                </Paper>
            </div>
        )
    }
}
