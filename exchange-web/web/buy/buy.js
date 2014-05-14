var buyPrice = undefined;

var roundToTwo = function(num) {    
    return +(Math.round(num + "e+2")  + "e-2");
};

var updateCalculatedPrice = function() {
	var buyAmount = $("#inputBuyAmount").val();
	 
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
};	


var updatePrices = function() {
	$.getJSON("../customerBuyPrice", function(prices) {
		buyPrice = prices.buyPrice;

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

var submitUnconfirmedPreOrder = function() {
	$('#dialogTitle').text("Please wait a second...");
	$('#dialogBody').html("<div class=\"alert alert-info\"><p>Please wait while we are sending you an confirmation e-mail...</p></div>");
	$('#dialog').modal('show');	
	var buyFormData = $("#buyForm").serialize();	
	$.post("../unconfirmedBuyService", buyFormData, function(response, status) {
			if (response === "emailSent") {
				$('#dialogTitle').text("Confirmation e-mail has been sent to you.");
				$('#dialogBody').html("<div class=\"alert alert-success\"><p><strong>Confirmation e-mail has been sent to you.</strong></p><p>Please follow the link in the e-mail to continue.</p></div>");
				$('#dialog').modal('show');		
			} else {
				$('#dialogTitle').text("Some error ocurred while sending you confirmation e-mail.");
				$('#dialogBody').html("<div class=\"alert alert-danger\"><p><strong>Some error ocurred while sending you confirmation e-mail.</strong></p><p>Please try again later or contact our support at support@newpecunia.com.</p></div>");
				$('#dialog').modal('show');		
			}
		});	
};

var submitBuy = function() {
	var buyFormData = $("#buyForm").serialize();	
	$.post("../buyService", buyFormData, function(response, status) {
			var responseObject = jQuery.parseJSON( response );
			var txId = responseObject.transactionId;
			$('#trans_id').val(txId);
			$("#goToCardPaymentForm").submit();
		});	
};

window.onload = function() {
	
	$("#inputBuyAmount").on("input", function(){
		fixDecimalComma($(this));
		updateCalculatedPrice();
	});
	
	updatePrices();
	window.setInterval("updatePrices()", 5000);
	
};
