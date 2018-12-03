// eslint-disable-next-line
import React, { Component } from "react";
// Stateless Funnctional Component
const NavBar = () => {
  return (
    // <nav className="navbar navbar-light bg-light">
    //   <span className="navbar-brand mb-0 h1">
    //     Navbar{" "}
    //     <span className="badge badge-pill badge-secondary">
    //       {totalCounters}
    //     </span>
    //   </span>
    // </nav>
    <nav class="navbar navbar-dark bg-dark">
      <a class="navbar-brand" href="#">
        Navbar w/ text
      </a>
      <button
        class="navbar-toggler"
        type="button"
        data-toggle="collapse"
        data-target="#navbarText"
        aria-controls="navbarText"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon" />
      </button>
      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item active">
            <a class="nav-link" href="#">
              Home <span class="sr-only">(current)</span>
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">
              Features
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">
              Pricing
            </a>
          </li>
        </ul>
        <span class="navbar-text">Navbar text with an inline element</span>
      </div>
    </nav>
  );
};

export default NavBar;
