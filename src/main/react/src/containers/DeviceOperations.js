import React, {Component} from 'react';
import DeviceList from '../components/DeviceList'
import Modal from "../components/Modal"
import RaisedButton from 'material-ui/RaisedButton';
import Divider from 'material-ui/Divider';
import Paper from 'material-ui/Paper';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import ContentAdd from 'material-ui/svg-icons/content/add';

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
            <div>
                <DeviceList/>
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
            </div>
        );
    }
}
