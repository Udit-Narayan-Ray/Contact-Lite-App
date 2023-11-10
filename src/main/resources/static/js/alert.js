function deleteContact(contactId)
{
swal({
  title: "Are you sure?",
  text: "Contact will be deleted permanently",
  icon: "warning",
  buttons: true,
  dangerMode: true,
})
.then((willDelete) => {
  if (willDelete) {
	  
		window.location="/cpro/user/delete_contact/"+contactId;
		
  } else {
    swal({
		title:"!! Contact is Safe !!"
		});
  }
});

}