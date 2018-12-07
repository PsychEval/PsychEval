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

export default Firebase;
