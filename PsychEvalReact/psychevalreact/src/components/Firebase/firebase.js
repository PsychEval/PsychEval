import app from "firebase/app";
import "firebase/auth";

const config = {
  apiKey: "AIzaSyCtouzPejLMADgZkDp_5iXN3jQ3MiNf0G4",
  authDomain: "psycheval-ff91b.firebaseapp.com",
  databaseURL: "https://psycheval-ff91b.firebaseio.com",
  projectId: "psycheval-ff91b",
  storageBucket: "psycheval-ff91b.appspot.com",
  messagingSenderId: "573918469225"
};

class Firebase {
  constructor() {
    app.initializeApp(config);
    this.auth = app.auth();
  }
}
// *** Auth API ***

doCreateUserWithEmailAndPassword = (email, password) =>
  this.auth.createUserWithEmailAndPassword(email, password);

doSignInWithEmailAndPassword = (email, password) =>
  this.auth.signInWithEmailAndPassword(email, password);

doSignOut = () => this.auth.signOut();

export default Firebase;
