package com.app.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.Dao.AddressDao;
import com.app.Dao.UserDao;
import com.app.Entities.Address;
import com.app.Entities.Users;
import com.app.dto.AddressDTO;
import com.app.dto.ApiResponse;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

	@Autowired
	private AddressDao addressDao;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired 
	private UserDao userDao;
	
	@Override
	public ApiResponse addUserAddress(AddressDTO address) {
		// TODO Auto-generated method stub
		try {
			System.out.println(address);
		    Optional<Users> currentUserOpt = userDao.findById(address.getUid());
		    if(!currentUserOpt.isPresent()) {
		    	return new ApiResponse("User Not Found");
		    }
		    Users currentUser = currentUserOpt.get();
			Address currentAddress = mapper.map(address,Address.class);
			currentAddress.setUser(currentUser);
			addressDao.save(currentAddress);
			return  new ApiResponse("Address Added SuccessFully");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("ERROR HERE-"+e);
			return new ApiResponse<Object>("Address too long!","error");
		}
	}

	@Override
	public List<Address> GetUserAddress(Long uid) {
		return addressDao.findAllUserAddressById(uid);
	}
}
