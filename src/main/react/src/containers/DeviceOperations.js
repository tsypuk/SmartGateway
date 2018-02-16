import React, {Component} from 'react';
import DeviceList from '../components/DeviceList'
import Modal from "../components/Modal"
import Divider from 'material-ui/Divider';
import Paper from 'material-ui/Paper';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import ContentAdd from 'material-ui/svg-icons/content/add';

import deviceService from "../services/deviceService"

export default class DeviceOperations extends Component {

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
                },
            loading: true,
            devices: []
        }
        this.handleShowModalAdd = this.handleShowModalAdd.bind(this);
        this.handleShowModalEdit = this.handleShowModalEdit.bind(this);
        this.handleCloseModal = this.handleCloseModal.bind(this);
        this.reloadDevices = this.reloadDevices.bind(this);
    }

    handleShowModalAdd() {
        this.setState(
            {
                showModal: true,
                editMode: 'modal_add',
                device:
                    {
                        name: '',
                        ip: '192.168.1.6',
                        port: '',
                        uuid: ''
                    }

            });
    }

    handleShowModalEdit(currentDevice) {
        this.setState(
            {
                showModal: true,
                editMode: 'modal_edit',
                device: currentDevice
            });
    }

    handleCloseModal() {
        this.setState({showModal: false})
    }

    reloadDevices() {
        this.setState({
                          loading: true,
                          devices: []
                      });
        deviceService.getAllDevices()
            .then(loaded =>
                      this.setState(
                          {
                              devices: loaded,
                              loading: false
                          }
                      ));
    }

    render() {
        return (
            <div>
                <DeviceList
                    reloadDevices={this.reloadDevices}
                    devices={this.state.devices}
                    device={this.state.device}
                    showModal={this.handleShowModalEdit}
                    loading={this.state.loading}/>
                <Modal
                    id={this.state.editMode}
                    cancelLabel="Cancel this action"
                    submitLabel="Save action"
                    showModal={this.state.showModal}
                    title={(this.state.editMode === 'modal_edit') ? 'Edit device: ' : 'Add new emulated device'}
                    mode={this.state.editMode}
                    device={this.state.device}
                    handleCloseModal={this.handleCloseModal}
                    reloadDevices={this.reloadDevices}
                ></Modal>
                <Paper zDepth={2}>
                    <FloatingActionButton secondary={true}
                                          onClick={this.handleShowModalAdd}><ContentAdd/></FloatingActionButton>
                    <Divider/>
                </Paper>
            </div>
        );
    }
}
