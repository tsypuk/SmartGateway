import React, {Component} from 'react';
import Divider from 'material-ui/Divider';
import Paper from 'material-ui/Paper';

export default class Statistics extends Component {

    render() {
        return (
            <div>
                <Paper zDepth={2}>
                Statistics<br/>Table with details of invocation
                    <Divider/>
                </Paper>
            </div>
        );
    }
}
