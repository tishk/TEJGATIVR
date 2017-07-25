var JSNTemplate = {
	_templateParams: {},
	initOnDomReady: function()
	{
		JSNUtils.createGridLayout("DIV", "grid-layout", "grid-col", "grid-lastcol");
		JSNUtils.createExtList("list-number-", "span", "jsn-listbullet", true);
		JSNUtils.createExtList("list-icon", "span", "jsn-listbullet", false);
		JSNUtils.setupLayout();
	},
	initOnLoad: function()
	{
		JSNUtils.setSubmenuPosition(_templateParams.enableRTL);
		JSNUtils.setVerticalPosition("jsn-pos-stick-leftmiddle", 'middle');
		JSNUtils.setVerticalPosition("jsn-pos-stick-rightmiddle", 'middle');
	},
	initTemplate: function(templateParams)
	{
		_templateParams = templateParams;
		window.addEvent('domready', JSNTemplate.initOnDomReady);
		window.addEvent('load', JSNTemplate.initOnLoad);
	}
};
