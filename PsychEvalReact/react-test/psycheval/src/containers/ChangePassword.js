import React, { Component } from "react";
import { ButtonToolbar, Button } from "react-bootstrap";
class ChangePassword extends Component {
  state = {};

  goBack = async event => {
    this.props.history.push("/");
  };
  render() {
    return (
      <div>
        Change Password
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
