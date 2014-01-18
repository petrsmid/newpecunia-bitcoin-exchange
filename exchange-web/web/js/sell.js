var buyPrice = undefined;
var sellPrice = undefined;

var fee = 15;

var btcAddress = undefined;
var total = undefined;

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
		total = roundedPrice-fee;
		$("#totalPrice").text("$"+(total));
		
	} else {
		$("#calculatedPrice").text("");
		$("#calculatedPrice2").text("");
		$("#totalPrice").text("");
		total = undefined;
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

var openBtcReceiveDialog = function() {
	var btcAddress = "someBtcAddress";
	$('#btcAddress').text(btcAddress);
	var btcSellAmount = $("#sellAmount").val();
	$('#btcAmountToSend').text(btcSellAmount);
	var qrCodeUrl = "http://qrickit.com/api/qr?d=bitcoin:" + btcAddress + "?amount=" + btcSellAmount + "&qrsize=480";
	$('#qrCode').attr("src", qrCodeUrl);
	$("#btcNotReceived").attr("style", "display: none;");
	$("#btcReceived").attr("style", "display: none;");
	$("#progressBarWithLabel").attr("style", "");
	startCountDown();
	$('#waitForBtcModal').modal('show');	
};

//////////////// Progress bar ////////////////////////////
var maxTime = 5*60*100; //5 minutes
var start = undefined;
var oneStepTimeout = 3*1000; //every 3 sec
var btcReceived = false;

var startCountDown = function () {
	start = new Date();
	doOneStep();
};

var checkBtcReceived = function() {
	//TODO - ask server whether BTC received
	btcReceived = false;
};

function updateProgressBar(percentage) {
	var perc = 100-percentage;
	var style = "width: "+perc+"%";
	$('#progressBar').attr("style", style);
	$('#progressBar').attr("aria-valuenow", perc);
}

function doOneStep() {
	var now = new Date();
	var timeDiff = now.getTime() - start.getTime();
	var perc = Math.round((timeDiff/maxTime)*100);
	if (!btcReceived && perc < 100) {
		checkBtcReceived();
		updateProgressBar(perc);
		setTimeout(doOneStep, oneStepTimeout);
	} else {
		updateProgressBar(100);
		if (btcReceived) {
			$("#btcReceived").attr("style", "");
		} else {
			$("#btcNotReceived").attr("style", "");
		}
		$("#progressBarWithLabel").attr("style", "display: none;");
	}
}

/////////////////////////////////////////////////////////

window.onload = function() {
	
	$("#sellAmount").on("input", function(){
		fixDecimalComma($(this));
		updateCalculatedPrice();
	});
	
	updatePrices();
	window.setInterval("updatePrices()", 5000);

};
