var dates = ['Iphone','Ipad','Iwatch','三星笔记本','超市购物券','Iphone','Ipad','Iwatch','三星笔记本','超市购物券'];
var timer = null;
var playflag = 0;
var successFlag = 0;
window.onload = function(){
	var play = document.getElementById('play');
	var stop = document.getElementById('stop');
	play.onclick = function(){
			start();
		}
	stop.onclick =function () {
			stopchou();
	} 
	document.onkeyup = function (event) {
		if(event.keyCode ==13)
		{
			if(playflag == 0)
			{
				start();
			}else{
				stopchou();
			}
		}
		if(event.keyCode == 68){
			successFlag = 1;
		}
	}
}
function start() {
	var play = document.getElementById('play');
	if(playflag == 0){
		play.style.background = "#CDC5BF";
		playflag = 1;
		play.style.cursor="default";
		timer = setInterval(function(){
			var title = document.getElementById('title');
			var p = Math.floor(Math.random()*dates.length);	
			title.innerHTML = dates[p];
		},60)
	}
}
function stopchou() {
		if(playflag ==1)
	{
		if(successFlag == 1)
		{
			title.innerHTML = ("马尔代夫双人四日游！");
		}
		playflag = 0;
		play .style. background ="black";
		play.style.cursor="pointer"
		clearInterval(timer);
	}
}