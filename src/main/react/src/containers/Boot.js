import React, {Component} from 'react';
import Divider from 'material-ui/Divider';
import Paper from 'material-ui/Paper';
import BootDevice from "../components/BootDevice";

export default class Lambdas extends Component {

    render() {
        return (
            <div>
                <Paper zDepth={2}>
                    <BootDevice/>
                    <Divider/>
                </Paper>
            </div>
        );
    }
}
