import React, { Component } from "react";
import firebase from "firebase";
import { ButtonToolbar, Button } from "react-bootstrap";
class Messaging extends Component {
  constructor(props) {
    super(props);
    this.state = {
      cEmail: "",
      message: "",
      newMessage: ""
    };
  }

  goBack = async event => {
    this.props.history.push("/");
  };

  readMessages = () => {
    var pEmail = window.localStorage.getItem("ParentEmail");
    const { cEmail } = this.state;
    var m = "CONVERSATION SO FAR:\n";

    firebase
      .firestore()
      .collection("Messages")
      .get()
      .then(querySnapshot => {
        let newText = "";
        querySnapshot.docs.forEach(doc => {
          for (const key in doc.data()["Message List"]) {
            if (
              doc.data()["Counselor Email"] === this.state.cEmail &&
              doc.data()["Parent Email"] === pEmail
            ) {
              if (doc.data()["Message List"].hasOwnProperty(key)) {
                const messages = doc.data()["Message List"][key];
                if (messages[1] === 1) {
                  m += pEmail + ": " + messages[0] + "\n";
                } else {
                  m += cEmail + ": " + messages[0] + "\n";
                }
                newText = m.split("\n").map((item, i) => <p key={i}>{item}</p>);
              }
            }
          }
        });
        this.setState({
          newMessage: newText
        });
      });
  };

  onSubmit = event => {
    event.preventDefault();
    var pEmail = window.localStorage.getItem("ParentEmail");
    const { cEmail, message } = this.state;

    const db = firebase.firestore();
    // db.settings({
    //   timestampsInSnapshots: true
    // });
    var MessagesRef = db.collection("Messages");
    MessagesRef.get().then(querySnapshot => {
      querySnapshot.forEach(function(doc) {
        if (
          doc.data()["Counselor Email"] === cEmail &&
          doc.data()["Parent Email"] === pEmail &&
          message !== ""
        ) {
          var messages = doc.data()["Message List"];
          var mCount = Object.keys(messages).length;
          var newObj = Object.assign({}, messages);
          var plz = [message, 1];
          var mRef = MessagesRef.doc(doc.id);
          newObj[mCount] = plz;
          mRef.update({
            "Message List": firebase.firestore.FieldValue.delete()
          });
          mRef.update({
            "Message List": newObj
          });
        }
      });
      this.readMessages();
    });
  };

  onChangeCounselor = event => {
    this.setState({ cEmail: event.target.value });
  };

  onChangeMessage = event => {
    this.setState({ message: event.target.value });
  };

  render() {
    const { cEmail, message, error } = this.state;

    return (
      <div>
        <div>
          Enter the Counselor Email without entering a Message and hit Send to
          read your conversation with the Counselor so far!
        </div>
        <br />
        <div>{this.state.newMessage}</div>
        <form onSubmit={this.onSubmit}>
          <input
            name="Counselor Email"
            value={cEmail}
            onChange={this.onChangeCounselor}
            type="text"
            placeholder="Counselor's Email Address"
          />
          <br />
          <input
            name="Message"
            value={message}
            onChange={this.onChangeMessage}
            type="text"
            placeholder="Your message"
          />
          <br />
          <button type="submit">Send Message</button>

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

export default Messaging;
