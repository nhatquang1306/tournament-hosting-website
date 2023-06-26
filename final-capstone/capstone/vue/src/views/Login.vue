<template>
  <main>
    <div id="login">
      <form @submit.prevent="login">
        <img src="../assets/images/Logo.png" id="logo" />
        
        <div class="alert" role="alert" v-if="invalidCredentials">
          Invalid username or password.
        </div>
        <div role="alert" v-if="this.$route.query.registration">
          Thank you for registering, please sign in.
        </div>
        <div class="form-input-group">
          <label for="username">Username</label>
          <input
            type="text"
            id="username"
            v-model="user.username"
            required
            autofocus
          />
        </div>
        <div class="form-input-group">
          <label for="password">Password</label>
          <input
            type="password"
            id="password"
            v-model="user.password"
            required
          />
        </div>
        <button type="submit">Sign in</button>
        <p>
          Need an account?
          <router-link :to="{ name: 'register' }">Sign Up</router-link>
        </p>
        
      </form>
    </div>
  </main>
</template>

<script>
import authService from "../services/AuthService";

export default {
  name: "login",
  components: {},
  data() {
    return {
      user: {
        username: "",
        password: "",
      },
      invalidCredentials: false,
    };
  },
  methods: {
    login() {
      authService
        .login(this.user)
        .then((response) => {
          if (response.status == 200) {
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);
            this.$router.push("/");
          }
        })
        .catch((error) => {
          const response = error.response;

          if (response.status === 401) {
            this.invalidCredentials = true;
          }
        });
    },
  },
};
</script>

<style scoped>
main {
  background: url("../assets/images/LoginBackgroundImage.png") no-repeat center
    center fixed;
  height: 100vh;
  width: 100vw;
  background-size: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

#login {
  background-color: rgba(107, 107, 107, 0.603);
  border-radius: 5px;
  width: 75%;
  height: 80%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: radial-gradient(
      circle at 100% 100%,
      transparent 8px,
      #ff0000 8px,
      #ff0000 10px,
      transparent 10px
    ),
    linear-gradient(to right, #ff0000, #eeff00),
    radial-gradient(
      circle at 0% 100%,
      transparent 8px,
      #eeff00 8px,
      #eeff00 10px,
      transparent 10px
    ),
    linear-gradient(to bottom, #eeff00, #ff531a),
    radial-gradient(
      circle at 0% 0%,
      transparent 8px,
      #ff531a 8px,
      #ff531a 10px,
      transparent 10px
    ),
    linear-gradient(to left, #ff531a, #ffa200),
    radial-gradient(
      circle at 100% 0%,
      transparent 8px,
      #ffa200 8px,
      #ffa200 10px,
      transparent 10px
    ),
    linear-gradient(to top, #ffa200, #ff0000);
  background-size: 10px 10px, calc(100% - 20px) 2px, 10px 10px,
    2px calc(100% - 20px);
  background-position: top left, top center, top right, center right,
    bottom right, bottom center, bottom left, center left;
  background-repeat: no-repeat;
}
.form-input-group {
  display: block;
  margin-bottom: 1rem;
}
label {
  display: block;
  margin-right: 0.5rem;
  font-size: 125%;
}
input {
  width: 50vw;
  height: 3.5vh;
  max-width: 30rem;
  border-radius: 14px;
  background-image: radial-gradient(
      circle at 100% 100%,
      transparent 8px,
      #ff0000 8px,
      #ff0000 10px,
      transparent 10px
    ),
    linear-gradient(to right, #ff0000, #eeff00),
    radial-gradient(
      circle at 0% 100%,
      transparent 8px,
      #eeff00 8px,
      #eeff00 10px,
      transparent 10px
    ),
    linear-gradient(to bottom, #eeff00, #ff531a),
    radial-gradient(
      circle at 0% 0%,
      transparent 8px,
      #ff531a 8px,
      #ff531a 10px,
      transparent 10px
    ),
    linear-gradient(to left, #ff531a, #ffa200),
    radial-gradient(
      circle at 100% 0%,
      transparent 8px,
      #ffa200 8px,
      #ffa200 10px,
      transparent 10px
    ),
    linear-gradient(to top, #ffa200, #ff0000);
  background-size: 10px 10px, calc(100% - 20px) 2px, 10px 10px,
    2px calc(100% - 20px);
  background-position: top left, top center, top right, center right,
    bottom right, bottom center, bottom left, center left;
  background-repeat: no-repeat;
}
form {
  display: flex;
  flex-direction: column;
  align-items: center;
}

button {
  border-radius: 14px;
  margin-top: 7px;
  margin-bottom: 18px;
  
  border: 1px solid #ff531a;

  /* border-radius: 5px;
  padding: 5px 8px;
  border: 2px solid #1000a1;
  color: #1000a1;
  font-size: 18px; */
}

.alert {
  color: #dd6b6b;
  margin-bottom: 18px;
  font-weight: bold;
  font-size: 18px;
}

p {
  
  margin-bottom: 10px;
}

#logo {
  width: 25rem;
}

@media only screen and (max-width: 600px) {
  #logo {
    width: 20rem;
  }
  label {
    font-size: 110%;
  }
}

@media only screen and (max-width: 500px) {
  #logo {
    width: 15rem;
    font-size: 100%;
  }
}

@media only screen and (max-width: 400px) {
  #logo {
    width: 10rem;
    font-size: 100%;
  }
}

</style>