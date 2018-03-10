import React, {Component} from 'react';
import DiscoverySwitchBlock from "../components/DiscoverySwitchBlock";
import Paper from 'material-ui/Paper';

export default class UPnP extends Component {

    render() {
        return (
            <div>
                <Paper zDepth={2}>
                    <DiscoverySwitchBlock/>
                </Paper>
            </div>
        );
    }
}
