import React, { Component } from "react";
import firebase from "firebase";
import { ButtonToolbar, Button } from "react-bootstrap";
// import * as ROUTES from "../../constants/routes";
// import oth from "oauth";
var RequestToken;
var RequestTokenSecret;
var twitterAPI = require("node-twitter-api");
var twitter = new twitterAPI({
  consumerKey: "KrKj0MnihSR5cUCXix2aS8aJV",
  consumerSecret: "aaJY6emW1hwjmXPqrQMStjwGWGAcXpuNPvx849PUjBzijSfFVR"
  // callback: 'http://yoururl.tld/something'
});

class Oauth extends Component {
  constructor(props) {
    super(props);
    this.state = {
      TwitterCode: "",
      ChildName: "",
      error: ""
    };
  }

  onSubmit = event => {
    const { TwitterCode, ChildName, error } = this.state;

    twitter.getAccessToken(
      RequestToken,
      RequestTokenSecret,
      TwitterCode,
      function(error, accessToken, accessTokenSecret, results) {
        if (error) {
          console.log(error);
        } else {
          //store accessToken and accessTokenSecret somewhere (associated to the user)
          //Step 4: Verify Credentials belongs here
          console.log(accessToken);
          console.log(accessTokenSecret);
          console.log(results);
          //push to firebase
          const db = firebase.firestore();
          db.settings({
            timestampsInSnapshots: true
          });
          var SMRef = db.collection("SocialMedia");

          SMRef.set({
            "Parent Email": window.localStorage.getItem("ParentEmail"),
            "Risk Factor": -1,
            "Student Name": ChildName,
            UID: results.user_id,
            accountName: results.screen_name,
            secret: accessTokenSecret,
            token: accessToken
          });
        }
      }
    );
    this.setState({
      TwitterCode: "",
      ChildName: ""
    });
    alert("Twitter Authorized");
    event.preventDefault();
  };

  onChangeOauth = event => {
    // console.log(event.target.value);
    this.setState({ TwitterCode: event.target.value });
  };

  onChangeChild = event => {
    // console.log(event.target.value);
    this.setState({ ChildName: event.target.value });
  };

  onClickOauth = event => {
    //do oauth

    twitter.getRequestToken(function(
      error,
      requestToken,
      requestTokenSecret,
      results
    ) {
      if (error) {
        console.log("Error getting OAuth request token : " + error);
      } else {
        //store token and tokenSecret somewhere, you'll need them later; redirect user
        RequestToken = requestToken;
        RequestTokenSecret = requestTokenSecret;

        var url = twitter.getAuthUrl(requestToken);
        //window,open(url);
        console.log(requestToken);
        console.log(requestTokenSecret);
        console.log(url);
        window.open(url);
      }
    });
    // alert("TWITTTTT");
  };
  handleClick = button => {
    alert("REEE");
  };

  goBack = async event => {
    this.props.history.push("/");
  };

  render() {
    const { TwitterCode, ChildName, error } = this.state;

    return (
      <div>
        <button
          //  className='button1'
          onClick={() => this.onClickOauth(1)}
        >
          {" "}
          OAuth
        </button>
        <form onSubmit={this.onSubmit}>
          <input
            name="TwitterCode"
            value={TwitterCode}
            onChange={this.onChangeOauth}
            type="text"
            placeholder="Twitter Code"
          />
          <br />
          <input
            name="ChildName"
            value={ChildName}
            onChange={this.onChangeChild}
            type="text"
            placeholder="Child's Name"
          />
          <br />

          <button type="submit">Submit</button>

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

export default Oauth;
