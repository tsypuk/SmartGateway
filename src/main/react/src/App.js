import React, {Component} from 'react';
import alexa from './images/alexa.png';
import lambda from './images/lambda.png';
import './App.css';
import DeviceOperations from "./containers/DeviceOperations";
import UPnP from "./containers/UPnP";

import Analytics from "./containers/Analytics";
import Lambdas from "./containers/Lambdas";
import Statistics from "./containers/Statistics";

import darkBaseTheme from 'material-ui/styles/baseThemes/darkBaseTheme';
import lightBaseTheme from 'material-ui/styles/baseThemes/lightBaseTheme.js';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import AppBar from 'material-ui/AppBar';
import BootDevice from './components/BootDevice';

import {
    BrowserRouter as Router,
    Link,
    Route
} from 'react-router-dom';

let NIGHT_HOUR = 17;
let MORNING_HOUR = 8;

class App extends Component {

    check = () => {
        return ((new Date().getHours() > NIGHT_HOUR) || (new Date().getHours() < MORNING_HOUR));
    }



    render() {
        return (



            <MuiThemeProvider
                muiTheme={getMuiTheme(this.check()
                        ? darkBaseTheme : lightBaseTheme)}>
                <div>
                    <AppBar title="AWS Alexa smart gateway"/>
                    <header
                        className={this.check() ? "App-header-dark" : "App-header-light"}>
                        <img src={alexa} className="App-logo" alt="alexa dot" width="11%"/>
                        <img src={lambda} className="App-logo" alt="alexa dot" width="8%"/>
                    </header>
                    <Router>
                        <div>
                            <ul>
                                <li><Link to="/upnp">UPnP</Link></li>
                                <li><Link to="/stat">STATISTICS</Link></li>
                                <li><Link to="/lambda">LAMBDA</Link></li>
                                <li><Link to="/analytics">ANALYTICS</Link></li>
                                <li><Link to="/device">DEVICE</Link></li>
                                <li><Link to="/boot">BOOT</Link></li>
                            </ul>
                            <Route exact path="/" component={DeviceOperations}/>
                            <Route exact path="/upnp" component={UPnP}/>
                            <Route path="/stat" component={Statistics}/>
                            <Route path="/lambda" component={Lambdas}/>
                            <Route path="/analytic" component={Analytics}/>
                            <Route path="/device" component={DeviceOperations}/>
                            <Route path="/boot" component={BootDevice}/>
                        </div>
                    </Router>
                </div>
            </MuiThemeProvider>
        );
    }
}

export default App;
