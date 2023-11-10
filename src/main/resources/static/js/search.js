const search=()=>
{
	//console.log("JSRK")
	
	let content=$("#input_search").val();
	if(content!='')
	{
	//send the request to server through restapi and also full url since it has to fetch from endpoint 
		
		let url=`http://localhost:8081/cpro/search/${content}`;
		
		fetch(url)
		.then(
			(response)=> {return response.json()}
		)
		.then (
			(data)=>{ //console.log(data)
			
			let text=`<div  class='list-group'>`;
			let contactURL=`/cpro/user/show_contact_details/`;
			data.forEach((contact)=>{
				
				//list-group-item-action won't show hyperlink color
				text+=`<a href=${contactURL}+${contact.contactId} class='list-group-item list-group-item-action' >${contact.name}</a>`;
				
				
			});
			text+=`</div>`;
			
			$(".search-result").html(text);
			}
		)		
		
		
		//if content is not null the show result box
		$(".search-result").show();
				
	}
	else
	{
		//otherwise hide the result box
		$(".search-result").hide();
	}
	
}