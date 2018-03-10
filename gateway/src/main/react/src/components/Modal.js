import React from 'react';
import Dialog from 'material-ui/Dialog';
import ModifyDevice from './ModifyDevice'

const modalStyle = {
    width: '460px'
};

export default class Modal extends React.Component {

    render() {
        const {device} = this.props;
        const deviceId = this.props.device.id || '';
        return (
            <div>
                <Dialog
                    contentStyle={modalStyle}
                    title={`${this.props.title} ${deviceId}`}
                    open={this.props.showModal}>
                    <ModifyDevice device={device}
                                  key={deviceId}
                                  reloadDevices={this.props.reloadDevices}
                                  mode={this.props.mode}
                                  handleClose={this.props.handleCloseModal}/>
                </Dialog>
            </div>
        );
    }
}
