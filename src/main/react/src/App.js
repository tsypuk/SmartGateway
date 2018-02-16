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
import lightBaseTheme from 'material-ui/styles/baseThemes/lightBaseTheme';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import AppBar from 'material-ui/AppBar';
import Boot from './containers/Boot';

import {
    BrowserRouter as Router,
    Link,
    Route
} from 'react-router-dom';

let NIGHT_HOUR = 17;
let MORNING_HOUR = 8;

const discoveryTimeStyle = {
    height: '100%'

};

class App extends Component {

    check = () => ((new Date().getHours() > NIGHT_HOUR) || (new Date().getHours() < MORNING_HOUR));

    render() {
        return (
            <MuiThemeProvider
                muiTheme={getMuiTheme(this.check ? darkBaseTheme : lightBaseTheme)}>
                <div>
                    <AppBar title="AWS Alexa smart gateway"/>
                    <center>
                        <header
                            className={this.check ? "App-header-dark" : "App-header-light"}>
                            <img src={alexa} className="App-logo" alt="alexa dot" width="11%"/>
                            <img src={lambda} className="App-logo" alt="alexa dot" width="8%"/>
                        </header>
                        <Router>
                            <div>
                                <nav className="nav-collapse">
                                    <ul className="tabs primary-nav">
                                        <li className="tabs__item"><Link to="/toggle">Toggles</Link></li>
                                        <li className="tabs__item"><Link to="/upnp">UPnP Discovery</Link></li>
                                        <li className="tabs__item"><Link to="/stat">Statistics</Link></li>
                                        <li className="tabs__item"><Link to="/lambda">Lambda</Link></li>
                                        <li className="tabs__item"><Link to="/analytic">Analytics</Link></li>
                                        <li className="tabs__item"><Link to="/device">Configuration</Link></li>
                                        <li className="tabs__item"><Link to="/boot">Boot</Link></li>
                                    </ul>
                                </nav>
                                <Route exact path="/" component={DeviceOperations}/>
                                <Route exact path="/upnp" component={UPnP}/>
                                <Route path="/stat" component={Statistics}/>
                                <Route path="/lambda" component={Lambdas}/>
                                <Route path="/analytic" component={Analytics}/>
                                <Route path="/device" component={DeviceOperations}/>
                                <Route path="/boot" component={Boot}/>
                            </div>
                        </Router>
                        <footer className="App-header-dark" style={discoveryTimeStyle}></footer>
                    </center>
                </div>
            </MuiThemeProvider>
        );
    }
}

export default App;
