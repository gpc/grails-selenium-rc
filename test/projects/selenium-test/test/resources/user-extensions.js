Selenium.prototype.doLogin = function(username, password) {
	//	this.doOpen("/login");

	this.doType("css=input[name=j_username]", username);
	this.doType("css=input[name=j_password]", password);

	this.doClick("css=input[type=submit]");
	return this.doWaitForPageToLoad(this.defaultTimeout);
};