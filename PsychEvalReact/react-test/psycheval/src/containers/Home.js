import React, { Component } from "react";
import "./Home.css";
import { ButtonToolbar, Button } from "react-bootstrap";
export default class Home extends Component {
  handleLinkWithChild = async event => {
    // await Auth.signOut();
    this.props.history.push("/linkChild");
  };

  handleOauth = async event => {
    // await Auth.signOut();
    this.props.history.push("/oauth");
  };

  handleAmIApproved = async event => {
    // await Auth.signOut();
    this.props.history.push("/approved");
  };

  handleMessaging = async event => {
    // await Auth.signOut();
    this.props.history.push("/messaging");
  };

  handleEditPass = async event => {
    // await Auth.signOut();
    this.props.history.push("/editpass");
  };

  render() {
    return (
      <div className="Home">
        <div className="lander">
          <ButtonToolbar>
            <Button
              bsStyle="primary"
              bsSize="large"
              active
              onClick={this.handleLinkWithChild}
            >
              Link With Child
            </Button>
            <Button
              bsStyle="primary"
              bsSize="large"
              active
              onClick={this.handleOauth}
            >
              Oauth
            </Button>
            <Button
              bsStyle="primary"
              bsSize="large"
              active
              onClick={this.handleAmIApproved}
            >
              Am I Approved
            </Button>
            <Button
              bsStyle="primary"
              bsSize="large"
              active
              onClick={this.handleMessaging}
            >
              Messaging
            </Button>
            <Button
              bsStyle="primary"
              bsSize="large"
              active
              onClick={this.handleEditPass}
            >
              Edit Password
            </Button>
          </ButtonToolbar>
        </div>
      </div>
    );
  }
}
