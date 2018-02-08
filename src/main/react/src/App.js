import React, {Component} from 'react';
import logo from './logo.svg';
import './App.css';
import Dashboard from "./containers/Dashboard";
import Modal from "./components/Modal"
import Button from 'material-ui/Button';

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {
            showModal: false,
            name: 'The text',
            device:
                {
                    name: '',
                    ip: '',
                    port: '',
                    uuid: ''
                }
        }
        this.handleShowModal = this.handleShowModal.bind(this);
        this.handleCloseModal = this.handleCloseModal.bind(this);
    }

    handleShowModal() {
        this.setState(
            {showModal : true}
        );
        console.log(this.state)
    }

    handleCloseModal() {
        this.setState(
            {showModal: false}
        )
    }

    render() {
        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <h1 className="App-title">AWS Alexa smart gateway</h1>
                </header>
                <Dashboard/>
                <Modal
                    id={this.state.editMode ? 'modal_edit' : 'modal_add'}
                    title={this.state.editMode ? 'Edit project' : 'Create project'}
                    cancelLabel="Cancel this action"
                    submitLabel="Save action"
                    showModal={this.state.showModal}
                    onSubmit={this.state.editMode ? this.onEdit : this.onAdd}
                    onCancel={() => this.onModalCancel()}
                    device={this.state.device}
                    handleCloseModal = {this.handleCloseModal}
                >
                </Modal>
                <Button variant="raised" color="primary" onClick={this.handleShowModal}>Add</Button>
            </div>
        );
    }
}

export default App;
