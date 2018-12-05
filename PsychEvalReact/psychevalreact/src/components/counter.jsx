import React, { Component } from "react";

class Counter extends Component {
  //   state = {
  //     value: this.props.counter.value,
  //     anyVariablesNeeded: 0,
  //     tags: ["tag1", "tag2", "tag3"]
  //     //imageUrl: "https://picsum.photos/200"
  //   };

  //   constructor() {
  //     super();
  //     this.handleIncrement = this.handleIncrement.bind(this);
  //   }

  styles = {
    fontSize: 50,
    fontWeight: "bold"
  };

  render() {
    return (
      <div>
        {/* <img src={this.state.imageUrl} alt="" /> */}
        <span className={this.getBadgeClasses()}>{this.formatCount()}</span>
        <button
          onClick={() => this.props.onIncrement(this.props.counter)}
          className="btn btn-secondary btn-sm"
        >
          Increment
        </button>
        {/* <ul>
          {this.state.tags.map(tag => (
            <li key={tag}>{tag}</li>
          ))}
        </ul> */}
        <button
          onClick={() => this.props.onDelete(this.props.counter.id)}
          className="btn btn-danger btn-sm m-2"
        >
          Delete
        </button>
      </div>
    );
  }

  getBadgeClasses() {
    let classes = "badge m-2 badge-";
    classes += this.props.counter.value === 0 ? "warning" : "primary";
    return classes;
  }

  formatCount() {
    const { value } = this.props.counter;
    return value === 0 ? "Zero" : value;
  }
}

export default Counter;
