import React, {Component} from 'react';
import deviceService from '../services/deviceService';
import RaisedButton from 'material-ui/RaisedButton';
import {
    TableRow,
    TableRowColumn,
} from 'material-ui/Table';

export default class Device extends Component {

    constructor(props) {
        super(props);
        this.state = props.device;
        this.handleEdit = this.handleEdit.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleDelete = this.handleDelete.bind(this);
    }

    render() {
        return (
                <TableRow key={this.state.id} selected={false}>
                    <TableRowColumn>{this.state.id}</TableRowColumn>
                    <TableRowColumn>{this.state.name}</TableRowColumn>
                    <TableRowColumn>{this.state.ip}</TableRowColumn>
                    <TableRowColumn>{this.state.port}</TableRowColumn>
                    <TableRowColumn><RaisedButton primary={true}
                                                  onClick={this.handleEdit}
                                                  label="Edit"/></TableRowColumn>
                    <TableRowColumn><RaisedButton secondary={true}
                                                  onClick={this.handleDelete}
                                                  label="Delete"/></TableRowColumn>
                </TableRow>
        );
    }

    handleEdit() {
        this.props.showModal(this.state);
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
