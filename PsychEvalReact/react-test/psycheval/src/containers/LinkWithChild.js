import React, { Component } from "react";
import { ButtonToolbar, Button } from "react-bootstrap";
import firebase from "firebase";

class LinkWithChild extends Component {
  constructor(props) {
    super(props);
    this.state = {
      ParentEmail: "",
      CounselorEmail: "",
      ChildName: "",
      error: ""
    };
  }

  onSubmit = event => {
    const { ParentEmail, CounselorEmail, ChildName, error } = this.state;

    const db = firebase.firestore();
    db.settings({
      timestampsInSnapshots: true
    });
    var CouncelorRef = db.collection("Counselor");
    CouncelorRef.get().then(function(querySnapshot) {
      querySnapshot.forEach(function(doc) {
        // doc.data() is never undefined for query doc snapshots
        // console.log(doc.id, " => ", doc.data());
        // console.log(CounselorEmail)
        if (doc.data()["Email"] === CounselorEmail) {
          console.log(
            "Link with child True: ",
            ChildName,
            error,
            ParentEmail,
            CounselorEmail
          );

          var parents = doc.data().Parents;
          var pcount = Object.keys(parents).length;
          var newObj = Object.assign({}, parents);
          var plz = [ParentEmail, false, ChildName, false];

          var cRef = CouncelorRef.doc(doc.id);
          newObj[pcount] = plz;
          cRef.update({
            Parents: firebase.firestore.FieldValue.delete()
          });
          cRef.update({
            Parents: newObj
          });
        }
      });
    });

    this.setState({
      ParentEmail: "",
      CounselorEmail: "",
      ChildName: ""
    });
    alert("Your counselor will approve your request");
    event.preventDefault();
  };
  goBack = async event => {
    this.props.history.push("/home");
  };

  onChangeParent = event => {
    // console.log(event.target.value);
    this.setState({ ParentEmail: event.target.value });
  };

  onChangeCounselor = event => {
    // console.log(event.target.value);
    this.setState({ CounselorEmail: event.target.value });
  };

  onChangeChild = event => {
    // console.log(event.target.value);
    this.setState({ ChildName: event.target.value });
  };

  render() {
    const { ParentEmail, CounselorEmail, ChildName, error } = this.state;

    return (
      <div>
        <form onSubmit={this.onSubmit}>
          <input
            name="ParentEmail"
            value={ParentEmail}
            onChange={this.onChangeParent}
            type="text"
            placeholder="Parent's Email Address"
          />
          <br />
          <input
            name="CounselorEmail"
            value={CounselorEmail}
            onChange={this.onChangeCounselor}
            type="text"
            placeholder="Counselor's Email Address"
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
          <button type="submit">Submit for Approval</button>

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

export default LinkWithChild;
