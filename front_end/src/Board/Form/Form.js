import {useState} from "react";
import React, { Fragment } from 'react'
import "./Form.css"
import axios from 'axios';
import {Helmet} from "react-helmet";
import ChartFactory from "../Chart/ChartFactory";

const DynForm = () => {
    const [dataSource, setDataSource] = useState([
        {dataSourceName: '', dataSourceUrl: ''}
    ]);

    const [visSource, setVisSource] = useState([
        {visSourceName: ''}
    ]);

    const [proLogic, setProLogic] = useState(
        {startDate: '2018-07-01', endDate: '2021-12-31'}
        );

    const [graph, setGraph] = useState([
    ]);

    const handleInputChangeDName = (index, event) => {
        const values = [...dataSource];
        values[index].dataSourceName = event.target.value;
        setDataSource(values);
    };

    const handleInputChangeDUrl = (index, event) => {
        const values = [...dataSource];
        values[index].dataSourceUrl = event.target.value;
        setDataSource(values);
    };

    const handleInputChangeVName = (index, event) => {
        const values = [...visSource];
        values[index].visSourceName = event.target.value;
        setVisSource(values);
    };

    const handleInputChangeStart = (event) => {
        const new_proLogic = {...proLogic};
        new_proLogic.startDate = event.target.value;
        setProLogic(new_proLogic)
        // console.log(new_proLogic)
    };

    const handleInputChangeEnd = (event) => {
        const new_proLogic = {...proLogic};
        new_proLogic.endDate = event.target.value;
        setProLogic(new_proLogic)
    };

    const handleAddDFields = () => {
        const values = [...dataSource];
        values.push({ dataSourceName: '', dataSourceUrl: '' });
        setDataSource(values);
    };

    const handleRemoveDFields = index => {
        const values = [...dataSource];
        values.splice(index, 1);
        setDataSource(values);
    };

    const handleAddVFields = () => {
        const values = [...visSource];
        values.push({ visSourceName: ''});
        setVisSource(values);
    };

    const handleRemoveVFields = index => {
        const values = [...visSource];
        values.splice(index, 1);
        setVisSource(values);
    };

    const updateState = (data) => {
        if (data.hasOwnProperty("errorMessage")) {
            alert(data.errorMessage);
            return;
        }

        if (!data.hasOwnProperty("graph")) {
            alert("No graph");
            return;
        }

        setGraph(data.graph);
    }

    const updateStateDebug = (data) => {
       const a =  [{
           data:[{
                    type: 'bar',
                    x: ["2021/08/17","2021/11/21","2021/11/22"],
                    y: [2,1.8690958164642377,2],
                    connectgaps: true
           }],
           layout: {
               xaxis: {
                   title: 'Date of Comment',
                   type: 'category',
               },
               yaxis: {
                   title: 'Rate of Sentiment',
                   range: [1.5, 2],
               },
               width: 320,
               height: 240,
               title: 'Bar Chart',
               bargap: 0.3,
           }
       }];

        setGraph(a);
        console.log("debug")
    }


    const renderGraph = (data) => {
        let factory = new ChartFactory();

        let graphs = []
        for (let i = 0; i <= graph.length - 1; i ++) {
            console.log(graph[i]);
            graphs = graphs.concat(factory.produceChart(graph[i], i));
        }

        return (
            <div>
                <h className={"config-header"}>Result Graphs</h>
                <hr/>
                <br/>
                {graphs}
            </div>
        )
    }

    const sendServer = (json) => {
        // updateStateDebug(null);
        axios.post("http://localhost:4000/inputPost", json, {
            headers: {
                "Access-Control-Allow-Origin": "*",
                "Content-Type": "application/json"
            }
        })
            .then((res) => {
                updateState(res.data);
            })
            .catch((error) => {
                console.log(error);
            });
        return null;
    }

    const handleSubmit = e => {
        e.preventDefault();

        let new_json = {}
        new_json.workSpace = [{}]
        new_json.workSpace[0].dataSource = dataSource;
        new_json.workSpace[0].visSource = visSource;
        new_json.workSpace[0].processSource = {
            "filter": proLogic
        };

        new_json = JSON.stringify(new_json);
        console.log("new_json:", new_json);

        sendServer(new_json)
    };

    const renderData = e => {
        return <div className="form-row">
            <h className={"config-header"}> Data Source Config</h>

            {dataSource.map((inputField, index) => (
                <Fragment key={`${inputField}~${index}`}>
                    <div className="form-group col-sm-6">
                        <label htmlFor="dataSourceName">Data Source Name
                            <input
                                type="text"
                                className="form-control"
                                value={inputField.dataSourceName}
                                placeholder={"e.g. youtube/twitter/news/csv/instagram"}
                                onChange={event => handleInputChangeDName(index, event)}
                            />
                        </label>
                    </div>

                    <div className="form-group col-sm-4">
                        <label htmlFor="dataSourceUrl">Data Source Url
                            <input
                                type="text"
                                className="form-control"
                                value={inputField.dataSourceUrl}
                                placeholder={"e.g. url/@userid/keyword/test.txt/#hashtag,@user"}
                                onChange={event => handleInputChangeDUrl(index, event)}
                            />
                        </label>
                    </div>

                    <div className="form-group col-sm-2">
                        <button
                            className="btn btn-link"
                            type="button"
                            onClick={() => handleRemoveDFields(index)}
                        >
                            -
                        </button>
                        <button
                            className="btn btn-link"
                            type="button"
                            onClick={() => handleAddDFields()}
                        >
                            +
                        </button>
                    </div>

                </Fragment>
            ))}
        </div>
    };

    const renderVis = e => {
        return (
            <div className="form-row">
                <hr/>
                <h className={"config-header"}>Visualization Config</h>

                {visSource.map((inputField, index) => (
                    <Fragment key={`${inputField}~${index}`}>
                        <div className="form-group col-sm-6">
                            <label htmlFor="visSourceName">Chart Name
                                <input
                                    type="text"
                                    className="form-control"
                                    value={inputField.visSourceName}
                                    placeholder={"e.g. bar/pie/heapmap/timeseries"}
                                    onChange={event => handleInputChangeVName(index, event)}
                                />
                            </label>
                        </div>

                        <div className="form-group col-sm-2">
                            <button
                                className="btn btn-link"
                                type="button"
                                onClick={() => handleRemoveVFields(index)}
                            >
                                -
                            </button>
                            <button
                                className="btn btn-link"
                                type="button"
                                onClick={() => handleAddVFields()}
                            >
                                +
                            </button>
                        </div>

                    </Fragment>
                ))
                }
        </div>
        )
    };

    const renderPro = e => {
        return (
            <div className="form-row">
                <hr/>
                <h className={"config-header"}>Filter Config</h>


                <div className="form-group col-sm-6">
                    <label htmlFor="startDate">Start Date
                        <input type="date" id="start" name="trip-start"
                               value={proLogic.startDate}
                               min="2016-01-01" max="2021-12-31"
                               onChange={event => handleInputChangeStart(event)}/>
                    </label>

                    <label htmlFor="endDate">End Date
                        <input type="date" id="end" name="trip-end"
                               value={proLogic.endDate}
                               min="2016-01-01" max="2021-12-31"
                               onChange={event => handleInputChangeEnd(event)}/>
                    </label>
                </div>
            </div>
        )
    };

    return (
        <>
            <h1>Dynamic Panel for Data Analysis</h1>

            <Helmet>
                <script src="../Chart/jstest.js"/>
            </Helmet>

            <div id="myDiv">
            </div>

            <div className={"dyn-form"}>
                <form onSubmit={handleSubmit}>
                    {renderData()}
                    <br/>
                    <br/>

                    {renderVis()}
                    <br/>
                    <br/>

                    {renderPro()}
                    <br/>

                    <div className="submit-button">
                        <button
                            className="btn btn-primary mr-2"
                            type="submit"
                            onSubmit={handleSubmit}
                        >
                            Submit
                        </button>
                    </div>

                    <br/>
                    <br/>
                </form>
            </div>

            <div className={"dyn-form"}>
                {renderGraph()}
            </div>
        </>
    )



}
export default DynForm;