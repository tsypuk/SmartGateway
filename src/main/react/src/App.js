import React, {Component} from 'react';
import alexa from './images/alexa.png';
import lambda from './images/lambda.png';
import './App.css';
import DeviceOperations from "./containers/DeviceOperations";
import UPnP from "./containers/UPnP";

import Modal from "./components/Modal"
import RaisedButton from 'material-ui/RaisedButton';

import FloatingActionButton from 'material-ui/FloatingActionButton';
import ContentAdd from 'material-ui/svg-icons/content/add';
import Analytics from "./containers/Analytics";
import Lambdas from "./containers/Lambdas";
import Statistics from "./containers/Statistics";

import darkBaseTheme from 'material-ui/styles/baseThemes/darkBaseTheme';
import lightBaseTheme from 'material-ui/styles/baseThemes/lightBaseTheme.js';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import AppBar from 'material-ui/AppBar';
import Divider from 'material-ui/Divider';
import Paper from 'material-ui/Paper';

let NIGHT_HOUR = 17;

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            showModal: false,
            name: 'The text',
            device:
                {
                    name: '',
                    ip: '192.168.1.6',
                    port: '',
                    uuid: ''
                }
        }
        this.handleShowModal = this.handleShowModal.bind(this);
        this.handleCloseModal = this.handleCloseModal.bind(this);
    }

    handleShowModal() {
        this.setState({showModal: true});
    }

    handleCloseModal() {
        this.setState({showModal: false})
    }

    render() {
        return (
            <div className="App">
                <MuiThemeProvider
                    muiTheme={getMuiTheme((new Date().getHours()) > NIGHT_HOUR ? darkBaseTheme : lightBaseTheme)}>
                    <div>
                        <AppBar title="AWS Alexa smart gateway"/>
                        <header
                            className={(new Date().getHours()) > NIGHT_HOUR ? "App-header-dark" : "App-header-light"}>
                            <img src={alexa} className="App-logo" alt="alexa dot" width="11%"/>
                            <img src={lambda} className="App-logo" alt="alexa dot" width="8%"/>
                        </header>
                        <UPnP/>
                        <DeviceOperations/>
                        <Modal
                            id={this.state.editMode ? 'modal_edit' : 'modal_add'}
                            cancelLabel="Cancel this action"
                            submitLabel="Save action"
                            showModal={this.state.showModal}
                            device={this.state.device}
                            handleCloseModal={this.handleCloseModal}
                        ></Modal>

                        <Paper zDepth={2}>
                            <RaisedButton primary={true} onClick={this.handleShowModal} label="Add"/>
                            <FloatingActionButton secondary={true}><ContentAdd/></FloatingActionButton>
                            <Divider/>
                        </Paper>
                        <Analytics/>
                        <Lambdas/>
                        <Statistics/>
                    </div>
                </MuiThemeProvider>
            </div>
        );
    }
}

export default App;
