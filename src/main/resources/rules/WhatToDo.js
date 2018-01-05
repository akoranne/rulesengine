//
// Implementing
// http://study.com/academy/lesson/what-is-a-decision-tree-examples-advantages-role-in-management.html

var family_visiting;
var weather;
var money;
var known_weathers = [ "sunny", "rainy", "windy" ];
var rich_money = [ "rich", "wealthy" ];

// function(s)
var contains = function(needle) {
	// Per spec, the way to identify NaN is that it is not equal to itself
	var findNaN = needle !== needle;
	var indexOf;

	if(!findNaN && typeof Array.prototype.indexOf === 'function') {
		indexOf = Array.prototype.indexOf;
	} else {
		indexOf = function(needle) {
			var i = -1, index = -1;

			for(i = 0; i < this.length; i++) {
				var item = this[i];

				if((findNaN && item !== item) || item === needle) {
					index = i;
					break;
				}
			}

			return index;
		};
	}

	return indexOf.call(this, needle) > -1;
};


// -------- output variable(s) ---------
var todo;

// --- starts here

var arrValues = ["Sam","Great", "Sample", "High"];
print (arrValues.indexOf("Sam"));
print(" jjs args: ---> ", family_visiting, weather, money);

if (family_visiting == true)  {
	todo = "Cinema";
} else {
	if (!known_weathers.indexOf(weather) == -1) {
		todo = null;
	} else if (weather == "sunny" ) {
		todo = "Play Tennis";
	} else if (weather == "rainy") {
		todo = "Stay In";
	} else {
		// weather is cold / windy, we need an indoor activity
		todo = rich_money.indexOf(money) > -1 ? "Shopping" : (money=="poor" ? "Cinema" : null) ;
	}
}
