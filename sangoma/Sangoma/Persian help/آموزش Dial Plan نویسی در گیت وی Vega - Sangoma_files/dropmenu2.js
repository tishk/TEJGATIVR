function showHTML2(id) {
	if (!jQuery("#main-items #item" + id).hasClass('selected')) {
		jQuery("#main-items .item").removeClass("selected");
		jQuery("#main-items #item" + id).addClass("selected");
		jQuery(".menu-desc-item").hide();
		jQuery("#menu-desc-item" + id).fadeIn();
	}
}