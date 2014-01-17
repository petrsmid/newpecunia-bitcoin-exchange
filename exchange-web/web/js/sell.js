var buyPrice = undefined;
var sellPrice = undefined;

var fee = 15;

var roundToTwo = function(num) {    
    return +(Math.round(num + "e+2")  + "e-2");
};

var updateCalculatedPrice = function() {
	var sellAmount = $("#sellAmount").val();
	 
	var calculatedPrice = buyPrice * sellAmount;
	if (!isNaN(calculatedPrice) && calculatedPrice >= 0) {
		var roundedPrice = roundToTwo(calculatedPrice);
		$("#calculatedPrice").text(roundedPrice);
		$("#calculatedPrice2").text("$"+roundedPrice);
		$("#totalPrice").text("$"+(roundedPrice-fee));
	} else {
		$("#calculatedPrice").text("");
		$("#calculatedPrice2").text("");
		$("#totalPrice").text("");
	}
};

var showUpdatedPrices = function() {
	$("#buyPrice").text("$"+buyPrice);
	$("#sellPrice").text("$"+sellPrice);
};	


var updatePrices = function() {
	//mock
	buyPrice = roundToTwo(700 + Math.random() * 100);
	sellPrice = roundToTwo(buyPrice + 50);
	
	showUpdatedPrices();
	updateCalculatedPrice();
};

var fixDecimalComma = function(input) {
	var origValue = input.val();
	var fixedValue = origValue.replace(/,/g,".");
	if (fixedValue !== origValue) {
		input.val(fixedValue);
	} 
};

window.onload = function() {
	
	$("#sellAmount").on("input", function(){
		fixDecimalComma($(this));
		updateCalculatedPrice();
	});
	
	updatePrices();
	window.setInterval("updatePrices()", 5000);

};
