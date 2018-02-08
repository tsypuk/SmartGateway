import React, {Component} from 'react';
import discoveryService from '../services/discoveryService';
import Button from 'material-ui/Button';

export default class DiscoverySwitch extends Component {

    constructor(props) {
        super(props);
        this.handleClick = this.handleClick.bind(this);
    }

    render() {
        return (
            <Button variant="raised" color="primary" onClick={this.handleClick} disabled={
                (this.props.action === 'Start') ? this.props.running : !this.props.running}>
                {this.props.action}
            </Button>
        )
    }

    handleClick() {
        if (this.props.action === 'Start') {
            discoveryService.trigger('on');
            this.props.onTriggered();
        } else {
            discoveryService.trigger('off');
            this.props.offTriggered();
        }
    }
}
