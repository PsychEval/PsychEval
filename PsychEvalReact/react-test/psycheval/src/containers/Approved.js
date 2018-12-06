import React, { Component } from "react";
import firebase from "firebase";

class isApproved extends Component {
  constructor(props) {
    super(props);
    this.state = {
      message: ""
    };
  }

  // checkApproved = () => {
  componentDidMount() {
    var pEmail = window.localStorage.getItem("ParentEmail");
    var m = "";
    firebase
      .firestore()
      .collection("Counselor")
      .get()
      .then(querySnapshot => {
        let newText = "";
        querySnapshot.docs.forEach(doc => {
          for (const key in doc.data().Parents) {
            if (doc.data().Parents.hasOwnProperty(key)) {
              const parent = doc.data().Parents[key];
              if (parent[0] === pEmail) {
                if (parent[1] === true) {
                  m += parent[2] + " has been approved!\n";
                } else {
                  m += parent[2] + " has not been approved!\n";
                }
                newText = m.split("\n").map((item, i) => <p key={i}>{item}</p>);
              }
            }
          }
        });
        this.setState({
          message: newText
        });
      });
  }

  render() {
    // const { message } = this.state;
    // this.checkApproved();
    return <div>{this.state.message}</div>;
  }
}

export default isApproved;
