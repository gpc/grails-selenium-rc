Selenium.prototype.doLogin = function(username, password) {
	selenium.doOpen("/login");

	var usernameField = this.page().findElement("id=j_username");
	var passwordField = this.page().findElement("id=j_password");
	var loginButton = this.page().findElement("css=input[type=submit]");

    selenium.doType(usernameField, username);
    selenium.doType(passwordField, password);

	selenium.doClickAndWait(loginButton);
};