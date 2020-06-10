import axios from "axios";
import store from "./cookieService.js";

export default {
  login(username, password, callback) {
    return axios({
      // url: "/trader/login",
      url: "/tradr/login",
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      data: "id=" + username + "&password=" + password,
    })
      .then((response) => {
        if (response.data.status) {
          // this.$cookie.set("user_jwt", response.data.token[0].replace("JWT=", ""));
          // this.$cookie.set("user", response.data.token[1].replace("user=", ""));
          const token = response.data.token[0]
            .replace("; Path=/; Secure; HttpOnly", "")
            .replace("JWT=", "");
          // const jwt = store.getCookie("JWT");
          localStorage.setItem("user_jwt", token);
          window.location.href = "/tradr";
        } else {
          localStorage.removeItem("user_jwt");
          // this.$cookie.remove("user_jwt");
          // this.$cookie.remove("user");
          return callback();
        }
      })
      .catch((e) => {});
  },
};
