
/***js***/

$(document).ready(function(){
	var meter_LayerCont = $(".King_Chance_LayerCont");
	var meter_Close = $(".King_Chance_Layer_Close");
	meter_Close.click(function(){ 
        clearInterval(meter_Play_Time);
	meter_LayerCont.slideUp();   
    });
	meter_Pop = function(){
		//meter_LayerCont.slideDown();
		meter_Play_Time = setInterval(function(){  },2000);
		}; 
	meter_add();
	});

function meter_Probability(meter_Array){
	var meter_Array_Num=0,
		meter_Array_Rnd,
		meter_Array_NewRnd=0;
	for(i=0;i<meter_Array.length;i++)
		{meter_Array_Num += meter_Array[i];  };  

	var meter_Array_Rnd=Math.round((meter_Array_Num - 1) * Math.random()) + 1;
	if(meter_Array_Rnd<=0)    return false;   

	for(i=0;i<meter_Array.length;i++){
		meter_Array_NewRnd += meter_Array[i];   
		if(meter_Array_Rnd<=meter_Array_NewRnd){
			if(window.addEventListener)
				{window.addEventListener("load",meter_Pop,false);}
			else{window.attachEvent("onload",meter_Pop);  };
			break;
			}
		}     
	};
 
  function meter_add(){
     var   meter_Array = new Array();
			meter_Array[0] = 10;
			meter_Array[1] = 10;
			meter_Array[2] = 10;
			meter_Array[3] = 10;
			meter_Array[4] = 10;
			meter_Array[5] = 10;
			meter_Array[6] = 40;
	  meter_Probability(meter_Array);	
  }
