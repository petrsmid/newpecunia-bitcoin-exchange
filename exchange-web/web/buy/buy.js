var buyPrice = undefined;
var sellPrice = undefined;

var roundToTwo = function(num) {    
    return +(Math.round(num + "e+2")  + "e-2");
};

var updateCalculatedPrice = function() {
	var buyAmount = $("#buyAmount").val();
	 
	var calculatedPrice = buyPrice * buyAmount;
	if (!isNaN(calculatedPrice) && calculatedPrice >= 0) {
		var roundedPrice = roundToTwo(calculatedPrice);
		$("#calculatedPrice").text(roundedPrice);
		$("#totalPrice").text("$"+roundedPrice);
	} else {
		$("#calculatedPrice").text("");
		$("#totalPrice").text("");
	}
};

var showUpdatedPrices = function() {
	$("#buyPrice").text("$"+buyPrice);
	$("#sellPrice").text("$"+sellPrice);
};	


var updatePrices = function() {
	$.getJSON("../customerBuySellPrice", function(prices) {
		buyPrice = prices.buyPrice;
		sellPrice = prices.sellPrice;

		showUpdatedPrices();
		updateCalculatedPrice();
	});
};

var fixDecimalComma = function(input) {
	var origValue = input.val();
	var fixedValue = origValue.replace(/,/g,".");
	if (fixedValue !== origValue) {
		input.val(fixedValue);
	} 
};

window.onload = function() {
	
	$("#buyAmount").on("input", function(){
		fixDecimalComma($(this));
		updateCalculatedPrice();
	});
	
	updatePrices();
	window.setInterval("updatePrices()", 5000);
	
};
