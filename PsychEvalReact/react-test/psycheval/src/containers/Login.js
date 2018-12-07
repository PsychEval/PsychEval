import React, { Component } from "react";
import { FormGroup, FormControl, ControlLabel } from "react-bootstrap";
import "./Login.css";
import LoaderButton from "../components/LoaderButton";
import firebase from "firebase";

export default class Login extends Component {
  constructor(props) {
    super(props);

    this.state = {
      isLoading: false,
      email: "",
      password: ""
    };
  }

  validateForm() {
    return this.state.email.length > 0 && this.state.password.length > 0;
  }

  handleChange = event => {
    this.setState({
      [event.target.id]: event.target.value
    });
  };

  handleSubmit = async event => {
    event.preventDefault();
    const { email, password } = this.state;
    this.setState({ isLoading: true });

    // await Auth.signIn(this.state.email, this.state.password);

    const db = firebase.firestore();
    db.settings({
      timestampsInSnapshots: true
    });
    firebase
      .firestore()
      .collection("Authentication")
      .get()
      .then(querySnapshot => {
        var isInvalid = true;
        querySnapshot.docs.forEach(doc => {
          if (doc.data().type === "Parent" && doc.data().email === email) {
            var hashed = doc.data().password;
            var c = "";
            var p = "";
            for (let i = 0; i < hashed.length; i++) {
              const element = hashed[i];
              if (element !== " ") {
                c += element;
              } else {
                var num = parseInt(c) - 21;
                p += String.fromCharCode(num);
                c = "";
              }
            }
            if (p === password) {
              window.localStorage.setItem("ParentEmail", email);
              this.props.userHasAuthenticated(true);
              this.setState({ isLoading: false });
              isInvalid = false;
              this.props.history.push("/home");
            }
          }
        });
        if (isInvalid) {
          alert("invalid login");
          this.setState({ isLoading: false });
        }
      });
  };

  // onSubmit = event => {
  //   const { email, password } = this.state;

  //   const db = firebase.firestore();
  //   db.settings({
  //     timestampsInSnapshots: true
  //   });

  //   firebase
  //     .firestore()
  //     .collection("Authentication")
  //     .get()
  //     .then(querySnapshot => {
  //       querySnapshot.docs.forEach(doc => {
  //         if (doc.data().email === email && doc.data().password === password) {
  //           console.log("here");
  //           window.localStorage.setItem("ParentEmail", email);
  //           this.props.userHasAuthenticated(true);
  //           this.setState({ isLoading: false });
  //           this.props.history.push("/");
  //         }
  //       });
  //     });
  //   event.preventDefault();
  // };

  render() {
    return (
      <div className="Login">
        <form onSubmit={this.handleSubmit}>
          <FormGroup controlId="email" bsSize="large">
            <ControlLabel>Email</ControlLabel>
            <FormControl
              autoFocus
              type="email"
              value={this.state.email}
              onChange={this.handleChange}
            />
          </FormGroup>
          <FormGroup controlId="password" bsSize="large">
            <ControlLabel>Password</ControlLabel>
            <FormControl
              value={this.state.password}
              onChange={this.handleChange}
              type="password"
            />
          </FormGroup>
          <LoaderButton
            block
            bsSize="large"
            disabled={!this.validateForm()}
            type="submit"
            isLoading={this.state.isLoading}
            text="Login"
            loadingText="Logging in…"
          />
        </form>
      </div>
    );
  }
}
