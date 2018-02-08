import React, {Component} from 'react';
import deviceService from '../services/deviceService';
import TextField from 'material-ui/TextField';
import Button from 'material-ui/Button';

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
                <tr>
                    <td>{this.state.id}</td>
                    <td>
                        <TextField id="name" name="name" value={this.state.name} onChange={this.handleInputChange}/>
                    </td>
                    <td>
                        <TextField id="ip" name="ip" value={this.state.ip} onChange={this.handleInputChange}/>
                    </td>
                    <td>
                        <TextField id="port" name="port" value={this.state.port} onChange={this.handleInputChange}/>
                    </td>
                    <td>
                        <Button variant="raised" color="primary" onClick={this.handleSave}>Save</Button>
                    </td>
                    <td>
                        <Button variant="raised" color="primary" onClick={this.handleDelete}>Delete</Button>
                    </td>
                </tr>
        );
    }

    handleSave() {
        console.log(this.state);
        deviceService.updateDevice(this.state);
    }

    handleDelete() {
        console.log('Delete invoked');
        console.log(this.state);
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
