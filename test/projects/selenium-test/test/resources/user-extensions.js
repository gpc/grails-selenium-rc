Selenium.prototype.doLogin = function(username, password) {
	this.open("/login");

    this.type("j_username", username);
    this.type("j_password", password);

	this.clickAndWait("css=input[type=submit]");
};