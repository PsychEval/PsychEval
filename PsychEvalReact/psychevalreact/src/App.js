import React, { Component } from "react";
// import { BrowserRouter, Route, Switch } from "react-router-dom";
// import Learned from "./components/Learned";
// import Error from "./components/error";
// import Login from "./components/Login";
// import Navigation from "./components/navigation";
import Routes from "./Routes";
import "./App.css";
import SplashNav from "./components/splashNav";

class App extends Component {
  render() {
    return (
      <div className="App container">
        <SplashNav />
        <Routes />
      </div>
    );
  }
}

export default App;
