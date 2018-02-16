import React, {Component} from 'react';
import DiscoverySwitch from './DiscoverySwitch';
import LinearProgress from 'material-ui/LinearProgress';
import TextField from 'material-ui/TextField';
import './DiscoverySwitchBlock.css';

const discoveryTimeStyle = {
    width: '35px'
};

export default class DiscoverySwitchBlock extends Component {

    constructor(props) {
        super(props);
        this.state =
            {
                period: 20,
                completed: 0,
                running: false
            };
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
            <div>UPnP Discovery<br/>
                <TextField id="discoveryTime" style={discoveryTimeStyle} className="discoveryTime" name="period" value={this.state.period}
                           onChange={this.handleInputChange}/>
                sec<br/>
                <DiscoverySwitch onTriggered={this.onTriggered} block={this} action="Start" primary={true}
                                 time={this.state.period}
                                 running={this.state.running}/>
                <DiscoverySwitch offTriggered={this.offTriggered} block={this} action="Stop" primary={true}
                                 time={this.state.period}
                                 running={this.state.running}/>
                <LinearProgress mode="determinate" value={this.state.completed}/>
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
