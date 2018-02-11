import React, {Component} from 'react';
import Divider from 'material-ui/Divider';
import Paper from 'material-ui/Paper';

export default class Analytics extends Component {

    render() {
        return (
            <div>
                <Paper zDepth={2}>
                Analytics<br/>draw graphics here
                    <Divider/>
                </Paper>
            </div>
        );
    }
}
