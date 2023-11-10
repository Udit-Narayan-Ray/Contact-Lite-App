package com.contact.payload;

public class ContactDTO {

	private Integer contactId;

	private String phoneNo;

	private String name;

	private String email;

	private String address;

	private String image;

	private String work;

	private String description;

	private UserDTO user;

	public ContactDTO() {
		// TODO Auto-generated constructor stub
	}

	public ContactDTO(Integer contactId, String phoneNo, String name, String email, String address, String image,
			String work, String description, UserDTO user) {
		super();
		this.contactId = contactId;
		this.phoneNo = phoneNo;
		this.name = name;
		this.email = email;
		this.address = address;
		this.image = image;
		this.work = work;
		this.description = description;
		this.user = user;
	}

	public Integer getContactId() {
		return contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "ContactDTO [contactId=" + contactId + ", phoneNo=" + phoneNo + ", name=" + name + ", email=" + email
				+ ", address=" + address + ", image=" + image + ", work=" + work + ", description=" + description
				 /* + ", user=" + user */  + "]";
	}

}
