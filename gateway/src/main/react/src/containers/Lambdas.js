import React, {Component} from 'react';
import Divider from 'material-ui/Divider';
import Paper from 'material-ui/Paper';

export default class Lambdas extends Component {

    render() {
        return (
            <div>
                <Paper zDepth={2}>
                    Lambdas<br/>connect devices to trigger lambdas
                    <Divider/>
                </Paper>
            </div>
        );
    }
}
