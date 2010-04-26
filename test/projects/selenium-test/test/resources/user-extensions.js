Selenium.prototype.doLogin = function(username, password) {
	this.doType("css=input[name=j_username]", username);
	this.doType("css=input[name=j_password]", password);

	this.doClick("css=input[type=submit]");
	return this.doWaitForPageToLoad(this.defaultTimeout);
};

Selenium.prototype.getLoginState = function(locator) {
	return this.getText("css=.loginInfo");
};
