import React from "react";
import Plot from '../../../node_modules/react-plotly.js/react-plotly';

class ChartFactory {

    produceChart(input, i) {
        return (
            <Plot key={i} {...input}/>
        );
    }
}

export default ChartFactory;