<template>
  <header>
    <nav class="container">
      <ul>
        <li>
          <router-link to="/">
            <img
              class="navbar-logo"
              src="../assets/images/Logo.png"
              alt="Logo"
            />
          </router-link>
        </li>

        <li><a href=""><router-link :to="{name: 'home'}">Home</router-link></a></li>
        <li><a href=""><router-link :to="{name: 'browse'}">Browse</router-link></a></li>
        <li v-if="$store.state.teamId != 0 && $store.state.teamId != null">
          <router-link to="/team" >Team</router-link>
        </li>
        <li v-if="$store.state.user.authorities[0].name=='ROLE_ORGANIZER'">
          <router-link to="/create-tournament">Create Tournament</router-link>
        </li>
        
      </ul>
      <div id="grow"></div>
      <ul>
        <li class="welcome-message">Welcome, {{ username }}</li>
        <li id="logout"><a @click="logout">Logout</a></li>
      </ul>
    </nav>
   
    <div class="mobile-container">
      <div class="welcome-message-2 box">Welcome, {{ username }}</div>
      <router-link to="/">
        <img class="navbar-logo-2 box" src="../assets/images/Logo.png" alt="Logo" />
      </router-link>
      <div class="box" id="hamburger-icon" @click="toggleMobileMenu($event)">
        <div class="bar1"></div>
        <div class="bar2"></div>
        <div class="bar3"></div>
        <ul class="mobile-menu" id="mobileMenu">
          <li><a href=""><router-link :to="{name: 'home'}">Home</router-link></a></li>
          <li><a href=""><router-link :to="{name: 'browse'}">Browse</router-link></a></li>
          <li><a href=""><router-link :to="{name: 'invites'}">Invites</router-link></a></li>
          <li v-if="$store.state.teamId != 0 && $store.state.teamId != null">
          <router-link to="/team" >Team</router-link>
        </li>
        <li v-if="$store.state.user.authorities[0].name=='ROLE_ORGANIZER'">
          <router-link to="/create-tournament" >Create Tournament</router-link>
        </li>
          <li id="logout"><a @click="logout">Logout</a></li>
        </ul>
      </div>
    </div>
    
  </header>
</template>

<script>
import UserService from "../services/UserService.js"
export default {
  data() {
    return {
      username: this.$store.state.user.username
    };
  },
  methods: {
    logout() {
      this.$router.push("/logout");
    },
    toggleMobileMenu(event) {
      event.currentTarget.classList.toggle("open");
    },
  },
  created() {
    UserService.getUser(this.$store.state.user.id).then(response => {
      this.$store.commit("IMPORT_TEAM_ID", response.data.team_id)
    })
  }
};
</script>

<style scoped>
* {
  padding: 0;
  margin: 0;
  box-sizing: border-box;
}

body {
  background-color: #353836;
  color: white;
  font-family: sans-serif;
}

header a {
  text-decoration: none;
}

/* JC separates logo and other buttons */
header {
  padding: 0 20px;
  background-color: #61001036;
  height: 50px;
  display: flex;
  justify-content: space-between;
}

.welcome-message-2 {
  display: none;
}

.navbar-logo-2 {
  display: none;
}

#brand {
  font-weight: bold;
  font-size: 18px;
  display: flex;
  align-items: center;
}

#brand a {
  color: #eed709;
}

ul {
  list-style: none;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-around;
}

ul a {
  color: #eed709;
}

img {
  height: 50px;
}

.welcome-message {
  
  
  color: #eed709;
}

ul li {
  padding: 5px;
  margin-left: 10px;
}

ul li:hover {
  transform: scale(1.1);
  transition: 0.3s;
}

/* this padding separates the logout from the rest of the LIs */
.container {
  display: flex;
  justify-content: space-between;
  flex-grow: 1;
}

#logout {
  cursor: pointer;
  border-radius: 5px;
  padding: 5px 8px;
  border: 1px solid #ff3860;
  margin-left: 10px; /* Add margin to separate logout from other items */
}

#logout a {
  color: #ff3860;
}

#hamburger-icon {
  margin: auto 0;
  display: none;
  cursor: pointer;
}

#hamburger-icon div {
  widows: 35px;
  height: 3px;
  background-color: white;
  margin: 6px 0;
  transition: 0.4s;
}

.open .bar1 {
  -webkit-transform: rotate(-45deg) translate(-6px, 6px);
  transform: rotate(-45deg) translate(-6px, 6px);
}

.open .bar2 {
  opacity: 0;
}

.open .bar3 {
  -webkit-transform: rotate(45deg) translate(-6px, -8px);
  transform: rotate(45deg) translate(-6px, -8px);
}

.open .mobile-menu {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  /* border: 1px solid #eed709; */
  border-top: 1px solid #eed709;
  border-bottom: 1px solid #eed709;
}

.mobile-menu {
  display: none;
  position: absolute;
  top: 50px;
  left: 0;
  height: calc(100vh - 50px);
  width: 100%;
  
}

.mobile-menu li {
  /* margin-bottom: 0px; */
  /* border: 1px solid #eed709; */
  
  
  
}

@media only screen and (max-width: 480px) {
  header nav ul {
    display: none;
  }
  #hamburger-icon {
    display: block;
  }
}

/* pasted code below here */

* {
  padding: 0;
  margin: 0;
  box-sizing: border-box;
}

header a {
  text-decoration: none;
}

header {
  padding: 0 20px;
  background-color: #01073f33;
  height: 50px;
  display: flex;
  justify-content: space-between;
}

#brand {
  
  font-size: 18px;
  display: flex;
  align-items: center;
}

ul {
  list-style: none;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-around;
}

ul a {
  color: #eed709;
}

ul li {
  padding: 5px;
  margin-left: 10px;
}

ul li:hover {
  transform: scale(1.1);
  transition: 0.1s;
}

#hamburger-icon {
  margin: auto 0;
  display: none;
  cursor: pointer;
}

#hamburger-icon div {
  width: 35px;
  height: 3px;
  background-color: #eed709;
  margin: 6px 0;
  transition: 0.4s;
}

.open .bar1 {
  -webkit-transform: rotate(-45deg) translate(-6px, 6px);
  transform: rotate(-45deg) translate(-6px, 6px);
}

.open .bar2 {
  opacity: 0;
}

.open .bar3 {
  -webkit-transform: rotate(45deg) translate(-6px, -8px);
  transform: rotate(45deg) translate(-6px, -8px);
}

.open .mobile-menu {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
}

.mobile-menu {
  display: none;
  position: absolute;
  top: 50px;
  left: 0;
  height: 238px;
  width: 100%;
  z-index: 1;
  background-color: #0f1020;
}

.mobile-menu li {
  /* margin-bottom: 10px; */
}

@media only screen and (max-width: 480px) {
  header nav {
    display: none;
  }

  #hamburger-icon {
    display: block;
    /* border: 1px solid black; */
  }

  .welcome-message-2 {
    display: block;
    
    font-size: 16px;
    color: #eed709;
  }

  .navbar-logo-2 {
    display: block;
    /* margin-left: auto;
    margin-right: auto; */
  }
/* ------------------------------------------ */
  
  .mobile-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  

  nav.container {
    display: none;
  }

  header {
    display: block;
  }

  ul#mobileMenu.mobile-menu {
    flex-direction: column;
    justify-content: space-evenly;
  }

  div.welcome-message-2.box {
    max-width: 50px;
    font-size: 14px;
    text-align: center;
  }


  /* .box {
    flex: 1;
    display: flex;
    justify-content: center;
  } */

  /* div#hamburger-icon.box {
    margin-right: auto;
  } */

  /* .box:first-child > span {
    margin-right: auto;
  } */

  /* .box:last-child  > span { 
    margin-left: auto;  
  } */


}
</style>
