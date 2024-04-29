package com.electronic.store.dtos;

import java.util.HashSet;
import java.util.Set;

import com.electronic.store.validate.ImageNameValid;

//import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDto {

	private String userId;

	@Size(min = 3, max = 40, message = "Invalid Name!!")
	//@ApiModelProperty(value = "user_name", name="username",required = true,notes = "user name of user")
	private String name;

	//@Email(message = "Invalid User Email !!")
	@Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$",message = "Invalid User Email !!")
	@NotBlank(message = "Email is required!!")
	private String email;

	@NotBlank(message = "Password is Required !!")
	private String password;

	@Size(min = 4, max = 6, message = "Inavlid gender !!")
	private String gender;

	@NotBlank(message = "Write something about yourself !!")
	private String about;

	@ImageNameValid
	private String imageName;

	private Set<RoleDto> roles = new HashSet<>();
}
