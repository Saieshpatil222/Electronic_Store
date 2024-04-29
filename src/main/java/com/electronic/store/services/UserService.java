package com.electronic.store.services;

import java.util.List;
import java.util.Optional;

import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.User;

public interface UserService {

	public UserDto createUser(UserDto userDto);

	public UserDto updateUser(UserDto userDto, String userId);

	public void deleteUser(String userId);

	public PageableResponse<UserDto> getAllUsers(int pageNumber,int pageSize, String sortBy, String sortDir);

	public UserDto getUserById(String userId);

	public UserDto getUserByEmail(String email);

	public List<UserDto> searchUser(String keyword);
	
	Optional<User> findUserByEmailOptional(String email);
}
