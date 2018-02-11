import React from 'react';
import Dialog from 'material-ui/Dialog';
import AddDevice from '../components/AddDevice'

export default class Modal extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            device: props.device,
        }
    }

    render() {
        const {device} = this.state;
        console.log(device);
        return (
            <div>
                <Dialog
                    title="Add new emulated device"
                    open={this.props.showModal}>
                    <AddDevice device={device} key={device.id} handleClose={this.props.handleCloseModal}/>
                </Dialog>
            </div>
        );
    }
}
