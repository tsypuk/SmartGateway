import React, {Component} from 'react';
import DeviceList from '../components/DeviceList'
import DiscoverySwitchBlock from "../components/DiscoverySwitchBlock";

export default class Dashboard extends Component {

    render() {
        return (
            <div>
                <DiscoverySwitchBlock/>
                <DeviceList/>
            </div>
        );
    }
}
