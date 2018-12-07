import React, { Component } from "react";
import firebase from "firebase";
import { ButtonToolbar, Button } from "react-bootstrap";
class isApproved extends Component {
  constructor(props) {
    super(props);
    this.state = {
      message: ""
    };
  }
  goBack = async event => {
    this.props.history.push("/");
  };

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
    return (
      <div>
        {this.state.message}{" "}
        <ButtonToolbar>
          <Button bsStyle="primary" bsSize="large" active onClick={this.goBack}>
            Back
          </Button>
        </ButtonToolbar>
      </div>
    );
  }
}

export default isApproved;
