import React from "react";
import { Route, Switch } from "react-router-dom";
import Home from "./containers/Home";
import NotFound from "./containers/NotFound";
import Login from "./containers/Login";
import AppliedRoute from "./components/AppliedRoute";
import ChangePassword from "./containers/ChangePassword";
import Message from "./containers/Messaging";
import LinkWithChild from "./containers/LinkWithChild";
import Approved from "./containers/Approved";
import Oauth from "./containers/Oauth";
export default ({ childProps }) => (
  <Switch>
    <AppliedRoute path="/" exact component={Home} props={childProps} />
    <AppliedRoute path="/login" exact component={Login} props={childProps} />
    <AppliedRoute
      path="/editpass"
      exact
      component={ChangePassword}
      props={childProps}
    />
    <AppliedRoute
      path="/messaging"
      exact
      component={Message}
      props={childProps}
    />
    <AppliedRoute
      path="/linkChild"
      exact
      component={LinkWithChild}
      props={childProps}
    />
    <AppliedRoute
      path="/approved"
      exact
      component={Approved}
      props={childProps}
    />
    <AppliedRoute path="/oauth" exact component={Oauth} props={childProps} />

    {/* Finally, catch all unmatched routes */}
    <Route component={NotFound} />
  </Switch>
);
