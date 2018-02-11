import React, {Component} from 'react';
import discoveryService from '../services/discoveryService';
import RaisedButton from 'material-ui/RaisedButton';

export default class DiscoverySwitch extends Component {

    constructor(props) {
        super(props);
        this.handleClick = this.handleClick.bind(this);
    }

    render() {
        return (
            <RaisedButton secondary={this.props.secondary} primary={this.props.primary} onClick={this.handleClick}
                          disabled={
                              (this.props.action === 'Start') ? this.props.running : !this.props.running}>
                {this.props.action}
            </RaisedButton>
        )
    }

    handleClick() {
        if (this.props.action === 'Start') {
            discoveryService.trigger({action: 'on', time: this.props.time});
            this.props.onTriggered();
        } else {
            discoveryService.trigger({action: 'off'});
            this.props.offTriggered();
        }
    }
}
