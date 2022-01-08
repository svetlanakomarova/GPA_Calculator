/**
 * Wendi Jollymore
 */
document.addEventListener("DOMContentLoaded", init);

function init() {

    // get all the elements with a class name of clink
    links = document.getElementsByClassName("clink");

    // add a click event handler to each link
    for (let item of links) {
    
        item.addEventListener("mouseover", function(e) {
            
            div = document.getElementById("display");  // display div
            //div.innerHTML.display = "block";           
			
            // invoke the handler that gets a course by code
            fetch('https://localhost:8443/course/' + item.id)
                        
                .then(data => data.json()) // convert to json
                
                // function to execute on the json data
                .then(function(data) {             
                	var output = "Course: " + data.code + "<br>" 
                			+ data.title + "<br>"
                        	+ "Credits: " + data.credits;
						if (data.complete)
                        	output += "<br>Term: " + data.term + "<br>"
                        		+ "Grade: " + data.finalGrade; 
                
					// set div coordinates to mouse coords
					// and offsets div/tooltip to prevent overlap with cursor					
					var cX = e.clientX + 10;
					var cY = e.clientY + 10;
					
					div.style.left = cX   + 'px';
  					div.style.top = cY  + 'px';
  										
                    // finally, display the course output in the div                    
                    div.innerHTML = output;
                    div.style.display = "block";  // wendi

                });    
        });
    
            item.addEventListener("mouseout", function() {
            // added by wendi
            div = document.getElementById("display");  // display div
            div.style.display = "none";    
            });
    }
}