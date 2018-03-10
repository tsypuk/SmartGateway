import React, {Component} from 'react';
import bootService from '../services/bootService';
import RaisedButton from 'material-ui/RaisedButton';

export default class BootDevice extends Component {

    constructor(props) {
        super(props);
        this.handleReload = this.handleReload.bind(this);
    }

    render() {
        return (
            <div>
                <RaisedButton className="reloadDevice" primary={true}
                              onClick={this.handleReload}
                              label="Reload devices"/>
            </div>
        );
    }

    handleReload() {
        bootService.trigger({action: 'reload'});
    }

}
