package com.app.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.Dao.ProductDao;
import com.app.Dao.ProductVariantDao;
import com.app.Dao.UserDao;
import com.app.Dao.WishListDao;
import com.app.Entities.Address;
import com.app.Entities.Product;
import com.app.Entities.ProductVariant;
import com.app.Entities.Users;
import com.app.Entities.WishList;
import com.app.customException.ResourceNotFoundException;
import com.app.dto.UserRegisterDTO;
import com.app.dto.UserResponseDto;
import com.app.dto.WishListDTO;
import com.app.dto.ApiResponse;
import com.app.dto.ProductDescDTO;
import com.app.dto.UserLoginDTO;

@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDao dao;
	
	@Autowired
	ModelMapper mapper;
	
	@Autowired
	WishListDao wishlistdao;
	
	@Autowired
	ProductDao prodDao;
	
	@Autowired 
    private ProductVariantDao variant;

	@Override
	public String register(UserRegisterDTO user) {
 		Users newUser = mapper.map(user,Users.class); 
		dao.save(newUser);
		return "registered successfully!";
	}

	@Override
	public UserResponseDto login(UserLoginDTO u) {
		Users user = dao.findByEmail(u.getEmail()).orElseThrow(()->new RuntimeException("user not found"));
		if(user.getPassword().equals(u.getPassword())) {
			UserResponseDto resultUser = mapper.map(user,UserResponseDto.class);
			return resultUser;
		}
		else {
			throw new RuntimeException("invalid credentials!");
		}
	}

	@Override
	public ApiResponse updateUserDetails(UserResponseDto user) {
		// TODO Auto-generated method stub
		Long uid = user.getUid();
		Users currentUser = dao.findById(uid).orElseThrow(()-> new ResourceNotFoundException("User Not FOund"));
		currentUser.setDOB(user.getDOB());
		currentUser.setEmail(user.getEmail());
		currentUser.setFirstName(user.getFirstName());
		currentUser.setLastName(user.getLastName());
		currentUser.setPhone(user.getPhone());
		System.out.println("hello");
        dao.save(currentUser);
		return new ApiResponse("Updated User Details");
	}

	@Override
	public String addToWishList(Long imgid,Long uid) {
		
		WishList wsh = new WishList();
		wsh.setImgid(imgid);
		wsh.setUid(uid);
		wishlistdao.save(wsh);
		return "product added ot wishlist";
	}

	@Override
	public List<WishListDTO> getWishItems(Long uid) {
		List<WishListDTO> items = new ArrayList<WishListDTO>();
		List<WishList> wshitems = wishlistdao.findByuid(uid);
		System.out.println(wshitems);
		for(WishList wsh:wshitems) {
			WishListDTO item  = new WishListDTO();
        	item.setImgid(wsh.getImgid());
            ProductVariant prodvar = variant.findById(wsh.getImgid()).orElseThrow(() -> new ResourceNotFoundException("Product Doesn't Exist"));
            Product product = prodvar.getProduct();
        	item.setPrice(product.getPrice());
        	item.setTitle(product.getProductName());
        	item.setRating(prodvar.getRating());
        	items.add(item);
        }
		return items;
	}

	@Override
	public ApiResponse deleteFromWishList(Long imgid,Long uid) {
		 
		int cnt = wishlistdao.deleteByImgIdAndUid(imgid, uid);
		if(cnt>0)
		return new ApiResponse("product deleted from wishlist");
		else
			return new ApiResponse("failed");
	}
}
