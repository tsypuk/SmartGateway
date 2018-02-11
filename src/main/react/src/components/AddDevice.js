import React, {Component} from 'react';
import deviceService from '../services/deviceService';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';

export default class AddDevice extends Component {

    constructor(props) {
        super(props);
        this.state = props.device;
        this.handleAdd = this.handleAdd.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this);
    }

    render() {
        return (
            <div>
                <table>
                    <tbody>
                    <tr>
                        <td>name</td>
                        <td><TextField id="name" name="name" value={this.state.name} onChange={this.handleInputChange}/>
                        </td>
                    </tr>
                    <tr>
                        <td>ip</td>
                        <td><TextField id="ip" name="ip" value={this.state.ip} onChange={this.handleInputChange}/></td>
                    </tr>
                    <tr>
                        <td>port</td>
                        <td><TextField id="port" name="port" value={this.state.port} onChange={this.handleInputChange}/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <RaisedButton primary={true} onClick={this.handleAdd} label="Add"/>
                        </td>
                        <td><RaisedButton primary={true} onClick={this.props.handleClose} label="Cancel"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        );
    }

    handleAdd() {
        deviceService.addDevice(this.state);
        this.props.handleClose();
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
