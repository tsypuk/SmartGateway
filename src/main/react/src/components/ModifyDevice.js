import React, {Component} from 'react';
import deviceService from '../services/deviceService';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';

const inputStyle = {
    width: '100px'
};
export default class ModifyDevice extends Component {

    constructor(props) {
        super(props);
        this.state = props.device;
        this.handleAdd = this.handleAdd.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleUpdate = this.handleUpdate.bind(this);
    }

    render() {
        return (
            <div>
                <center>
                    <table>
                        <tbody>
                        <tr>
                            <td>NAME</td>
                            <td><TextField id="name"
                                           name="name"
                                           value={this.state.name}
                                           style={inputStyle}
                                           onChange={this.handleInputChange}/>
                            </td>
                        </tr>
                        <tr>
                            <td>IP</td>
                            <td><TextField id="ip"
                                           name="ip"
                                           value={this.state.ip}
                                           style={inputStyle}
                                           onChange={this.handleInputChange}/>
                            </td>
                        </tr>
                        <tr>
                            <td>PORT</td>
                            <td><TextField id="port"
                                           name="port"
                                           value={this.state.port}
                                           style={inputStyle}
                                           onChange={this.handleInputChange}/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <RaisedButton primary={true}
                                              onClick={(this.props.mode === 'modal_add') ? this.handleAdd : this.handleUpdate}
                                              label={(this.props.mode === 'modal_add') ? 'Add' : 'Update'}
                                />
                            </td>
                            <td><RaisedButton primary={true}
                                              onClick={this.props.handleClose}
                                              label="Cancel"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </center>
            </div>
        );
    }

    handleAdd() {
        deviceService.addDevice(this.state)
            .then(promise => {
                this.props.reloadDevices();
                this.props.handleClose();
            });
    }

    handleUpdate() {
        deviceService.updateDevice(this.state)
            .then(promise => {
                      this.props.reloadDevices();
                      this.props.handleClose();
                  }
            );
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
