package com.godEShop.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.godEShop.Dto.UserInfoDto;
import com.godEShop.Entity.Account;
import com.godEShop.Entity.Role;
import com.godEShop.Entity.User;
import com.godEShop.Service.AccountService;
import com.godEShop.Service.RoleService;
import com.godEShop.Service.UserService;

@CrossOrigin("*")
@RestController
public class AccountRestController {
    @Autowired
    AccountService accountService;

    @Autowired
    UserService userService;
    
    @Autowired
    RoleService roleService;

    @GetMapping("/rest/accounts")
    public List<Account> getAll() {
	return accountService.findAll();
    }

    @GetMapping("/rest/info-accounts")
    public List<UserInfoDto> getInfo() {
	return userService.lstUserInfoDto();
    }

    @DeleteMapping("/rest/delete-account/{accountUsername}")
    public void deleteUsername(@PathVariable("accountUsername") String id) {
	accountService.delete(id);
    }
    
    @PutMapping("/rest/update-account/{accountUsername}")
    public UserInfoDto update(@PathVariable("accountUsername") String id, @RequestBody UserInfoDto ui) {
	System.out.println(" >>> : " + ui.getRoleId());
	System.out.println(" >>> : " + ui.getAccountUsername());
	System.out.println(" >>> : " + ui.getUserId());
	System.out.println(" >>> : " + ui.getAccountIsDeleted());
	
	Role r = new Role();
	r = roleService.findById(ui.getRoleId());
	
	
	Account a = new Account();
	a.setUsername(ui.getAccountUsername());
	a.setPassword(ui.getAccountPassword());
	a.setIsDelete(ui.getAccountIsDeleted());
	a.setRole(r);
	accountService.update(a);
	
	User u = new User();
	u = userService.findById(ui.getUserId());
	
	u.setId(ui.getUserId());
	u.setAddress(ui.getUserAddress());
	u.setDob(ui.getUserDob());
	u.setEmail(ui.getUserEmail());
	u.setFullname(ui.getUserFullname());
	u.setGender(ui.getUserGender());
	u.setPhone(ui.getUserPhone());
	u.setPhoto(ui.getUserPhoto());
	u.setAccount(a);
	userService.update(u);
	
	
	return ui;
    }

}
