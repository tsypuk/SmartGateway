import React, {Component} from 'react';
import deviceService from '../services/deviceService';
import RaisedButton from 'material-ui/RaisedButton';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import ContentAdd from 'material-ui/svg-icons/content/create';
import {
    TableRow,
    TableRowColumn,
} from 'material-ui/Table';

export default class Device extends Component {

    constructor(props) {
        super(props);
        this.state = props.device;
        this.handleSave = this.handleSave.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleDelete = this.handleDelete.bind(this);
    }

    render() {
        return (
                <TableRow key="1">
                        {/*<Toggle defaultToggled={true} />*/}
                    <TableRowColumn>{this.state.id}</TableRowColumn>
                    <TableRowColumn>
                        {/*<TextField id="name" name="name" value={this.state.name} onChange={this.handleInputChange}/>*/}
                        {this.state.name}</TableRowColumn>
                    <TableRowColumn>
                        {/*<TextField id="ip" name="ip" value={this.state.ip} onChange={this.handleInputChange}/>*/}
                        {this.state.ip}</TableRowColumn>
                    <TableRowColumn>
                        {/*<TextField id="port" name="port" value={this.state.port} onChange={this.handleInputChange}/>*/}
                        {this.state.port}</TableRowColumn>
                    <TableRowColumn><RaisedButton primary={true} onClick={this.handleSave} label="Save"/></TableRowColumn>
                    {/*<FloatingActionButton mini={true}>*/}
                        {/*<ContentAdd />*/}
                    {/*</FloatingActionButton>*/}
                    <TableRowColumn><RaisedButton secondary={true} onClick={this.handleDelete} label="Delete"/></TableRowColumn>
                </TableRow>
        );
    }

    handleSave() {
        deviceService.updateDevice(this.state);
    }

    handleDelete() {
        deviceService.deleteDevice(this.state).then(
            data => this.props.onDelete());
    }

    handleInputChange(event) {
        const target = event.target;
        const value = target.type === 'checkbox' ? target.checked : target.value;
        const name = target.name;

        this.setState({
                          [name]: value
                      });
    }
}
