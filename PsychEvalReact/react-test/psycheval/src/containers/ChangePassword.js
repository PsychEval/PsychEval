import React, { Component } from "react";
import { ButtonToolbar, Button } from "react-bootstrap";
import firebase from "firebase";
class ChangePassword extends Component {
  constructor(props) {
    super(props);
    this.state = {
      currentPassword: "",
      passwordOne: "",
      passwordTwo: "",
      error: ""
    };
  }

  onSubmit = event => {
    // eslint-disable-next-line
    const { currentPassword, passwordOne, passwordTwo, error } = this.state;

    const db = firebase.firestore();
    db.settings({
      timestampsInSnapshots: true
    });
    const email = window.localStorage.getItem("ParentEmail");
    if (email === "") {
      alert("please login again");
    } else {
      // TODO update database
      var db1 = db.collection("Authentication");
      db1.get().then(function(querySnapshot) {
        querySnapshot.forEach(function(doc) {
          if (
            doc.data().email === email &&
            doc.data().password === currentPassword
          ) {
            alert("password changed");
            db1.doc(doc.id).set({
              email: doc.data()["email"],
              name: doc.data()["name"],
              password: passwordOne,
              type: doc.data()["type"]
            });
          }
        });
      });
    }

    this.setState({
      currentPassword: "",
      passwordOne: "",
      passwordTwo: ""
    });

    event.preventDefault();
  };

  onChangePassword = event => {
    // console.log(event.target.value);
    this.setState({ currentPassword: event.target.value });
  };

  onChangePasswordOne = event => {
    // console.log(event.target.value);
    this.setState({ passwordOne: event.target.value });
  };

  onChangePasswordTwo = event => {
    // console.log(event.target.value);
    this.setState({ passwordTwo: event.target.value });
  };

  goBack = async event => {
    this.props.history.push("/");
  };
  render() {
    const { currentPassword, passwordOne, passwordTwo, error } = this.state;
    const isInvalid =
      currentPassword === "" ||
      passwordOne === "" ||
      passwordTwo === "" ||
      passwordOne !== passwordTwo;
    return (
      <div>
        <form onSubmit={this.onSubmit}>
          <input
            name="currentPassword"
            value={currentPassword}
            onChange={this.onChangePassword}
            type="password"
            placeholder="Current Password"
          />
          <input
            name="passwordOne"
            value={passwordOne}
            onChange={this.onChangePasswordOne}
            type="password"
            placeholder="New Password"
          />
          <input
            name="passwordTwo"
            value={passwordTwo}
            onChange={this.onChangePasswordTwo}
            type="password"
            placeholder="Retype new Password"
          />
          <button disabled={isInvalid} type="submit">
            Change Password
          </button>

          {error && <p>{error.message}</p>}
        </form>

        <ButtonToolbar>
          <Button bsStyle="primary" bsSize="large" active onClick={this.goBack}>
            Back
          </Button>
        </ButtonToolbar>
      </div>
    );
  }
}

export default ChangePassword;
