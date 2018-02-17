import React, {Component} from 'react';
import Divider from 'material-ui/Divider';
import Paper from 'material-ui/Paper';

import {ComposedChart, Tooltip, XAxis, YAxis, CartesianGrid, Legend, Area, Bar, Line} from 'recharts';

const data = [
    {name: 'MO', uv: 590, pv: 800, amt: 1400},
    {name: 'TU', uv: 868, pv: 967, amt: 1506},
    {name: 'WD', uv: 1397, pv: 1098, amt: 989},
    {name: 'TH', uv: 1480, pv: 1200, amt: 1228},
    {name: 'FRI', uv: 1520, pv: 1108, amt: 1100},
    {name: 'SAT', uv: 1400, pv: 680, amt: 1700},
    {name: 'SUN', uv: 1400, pv: 680, amt: 1700}];


export default class Analytics extends Component {

    render() {
        return (
            <div>
                <Paper zDepth={2}>
                    Analytics of toggle usage now this is dummy data
                    <ComposedChart width={600}
                                   height={400}
                                   data={data}
                                   margin={{top: 20, right: 20, bottom: 20, left: 20}}>
                        <XAxis dataKey="name"/>
                        <YAxis/>
                        <Tooltip/>
                        <Legend/>
                        <CartesianGrid stroke='#f5f5f5'/>
                        <Area type='monotone'
                              dataKey='amt'
                              fill='#8884d8'
                              stroke='#8884d8'/>

                        <Bar dataKey='pv' barSize={20} fill='#413ea0'/>
                        <Bar dataKey='uv' barSize={20} fill='#9925a0'/>

                        <Line type='monotone' dataKey='uv' stroke='#ff7300'/>
                    </ComposedChart>
                    <Divider/>
                </Paper>
            </div>
        );
    }
}
