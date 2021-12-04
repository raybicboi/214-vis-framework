import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import {Provider} from "react-redux";
import {createStore} from "redux";
import DynForm from "./Board/Form/Form";

function todos(state = [], action) {
    switch (action.type) {
        case 'ADD_TODO':
            return state.concat([action.text])
        default:
            return state
    }
}

const store = createStore(todos, ['Use Redux'])

// App
class App extends React.Component {
    render() {
        return (
            <div className="game">
                <div className="game-board">
                    {/*<Board />*/}
                    <Provider store={store}>
                        <DynForm/>
                    </Provider>
                </div>

            </div>
        );
    }
}

// render
ReactDOM.render(
    <App />,
    document.getElementById('root')
);



