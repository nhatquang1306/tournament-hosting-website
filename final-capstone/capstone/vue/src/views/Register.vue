<template>
  <main>
    <div id="register" class="text-center">
      <form @submit.prevent="register">
        <h1>Create Account</h1>
        <p>
          Already have an account?
          <router-link :to="{ name: 'login' }">Log in.</router-link>
        </p>
        <div class="error" role="alert" v-if="registrationErrors">
          {{ registrationErrorMsg }}
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
        <div class="form-input-group">
          <label for="confirmPassword">Confirm Password</label>
          <input
            type="password"
            id="confirmPassword"
            v-model="user.confirmPassword"
            required
          />
        </div>
        
        <div class="form-input-group">
          <div class="checkbox-container">
            <!-- <div>are</div> -->
          <label for="isHost">Are you a tournament host?</label>
          <input type="checkbox" id="isHost" v-model="user.isHost" />
          </div>
        </div>
        <div v-if="user.isHost">
          <div class="form-input-group">
            
            <label for="organizationName">Organization Name</label>
            <input
              type="text"
              id="organizationName"
              v-model="user.organizationName"
              required
            />
          
          </div>
        </div>
        
        <button type="submit">Create Account</button>
        
      </form>
    </div>
  </main>
</template>

<script>
import authService from "../services/AuthService";

export default {
  name: "register",
  data() {
    return {
      user: {
        username: "",
        password: "",
        confirmPassword: "",
        isHost: false,
        organizationName: "",
        role: "user",
      },
      registrationErrors: false,
      registrationErrorMsg: "There were problems registering this user.",
    };
  },
  methods: {
    register() {
      if (this.user.password != this.user.confirmPassword) {
        this.registrationErrors = true;
        this.registrationErrorMsg = "Passwords do not match.";
      } else {
        if (this.user.isHost) {
          this.user.role = "organizer";
        } else {
          this.user.role = "user";
        }
        authService
          .register(this.user)
          .then((response) => {
            if (response.status == 201) {
              this.$router.push({
                path: "/login",
                query: { registration: "success" },
              });
            }
          })
          .catch((error) => {
            const response = error.response;
            this.registrationErrors = true;
            if (response.status === 400) {
              this.registrationErrorMsg = "Bad Request: Validation Errors";
            }
          });
      }
    },
    clearErrors() {
      this.registrationErrors = false;
      this.registrationErrorMsg = "There were problems registering this user.";
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
#register {
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
.checkbox-container {
  display: flex;
  flex-direction: row;
  align-items: center;
  
}
input#isHost {
  width: 1rem;
}
button {
  margin-bottom: 22px;
  border-radius: 14px;
  border: 1px solid #ff531a;
}
h1 {
  margin-bottom: 20px;
}
.error {
  color: #dd6b6b;
  margin-bottom: 18px;
  font-weight: bold;
  font-size: 18px;
}
label {
  display: block;
  margin-right: 0.5rem;
  font-size: 110%;
}
p {
  margin-bottom: 12px;
}

@media only screen and (max-width: 480px) {
  label {
    font-size: 100%;
  }
}
</style>
