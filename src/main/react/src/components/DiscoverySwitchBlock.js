import React, {Component} from 'react';
import DiscoverySwitch from './DiscoverySwitch';
import {LinearProgress} from 'material-ui/Progress';
import TextField from 'material-ui/TextField';

export default class DiscoverySwitchBlock extends Component {

    constructor(props) {
        super(props);
        this.resetTimer();
        this.state = {
            period: 20
        }
        this.handleInputChange = this.handleInputChange.bind(this);
        this.onTriggered = this.onTriggered.bind(this);
        this.offTriggered = this.offTriggered.bind(this);
    }

    componentWillUnmount() {
        clearInterval(this.timer);
    }

    progress = () => {
        const {completed} = this.state;
        if (completed > 100) {
            clearInterval(this.timer);
            this.resetTimer();
        } else {
            const diff = 100 / this.state.period;
            this.setState(
                {
                    completed: completed + diff
                });
        }
    }

    resetTimer() {
        this.setState(
            {
                completed: 0,
                running: false
            })
    }

    onTriggered() {
        this.resetTimer();
        this.setState(
            {
                completed: 0,
                running: true
            }
        );
        this.timer = setInterval(this.progress, 1000);
    }

    offTriggered() {
        clearInterval(this.timer);
        this.resetTimer();
    }

    render() {
        return (
            <div>UPnP Discovery
                <TextField id="discoveryTime" name="period" value={this.state.period}
                           onChange={this.handleInputChange}/>
                sec
                <DiscoverySwitch onTriggered={this.onTriggered} block={this} action="Start"
                                 running={this.state.running}/>
                <DiscoverySwitch offTriggered={this.offTriggered} block={this} action="Stop"
                                 running={this.state.running}/>
                <LinearProgress variant="determinate" value={this.state.completed}/>
            </div>
        );
    }

    handleInputChange(event) {
        this.setState(
            {
                period: event.target.value
            });
    }
}
