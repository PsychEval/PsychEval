import React, { Component } from "react";
class Messaging extends Component {
  state = {};
  const data =[{"name":"test1"},{"name":"test2"}];
    const listItems = data.map((d) => <li key={d.name}>{d.name}</li>);
  render() {
    return <div>{listItems}</div>;
  }
}

export default Messaging;
