package com.godEShop.Service.Impl;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.godEShop.Dao.AccountDAO;
import com.godEShop.Entity.Account;
import com.godEShop.Service.AccountService;
@Service
public class AccountServiceImpl implements AccountService{
@Autowired
AccountDAO adao;

@Override
public Account findById(String username) {
	// TODO Auto-generated method stub
	return adao.findById(username).get();
}

@Override
public List<Account> findAll() {
	// TODO Auto-generated method stub
	return adao.findAll();
}
@Override
public Account create( Account account) {
	// TODO Auto-generated method stub
	return adao.save(account);
}
//
//@Override
//public Account update(Account account) {
//	// TODO Auto-generated method stub
//	return adao.save(account);
//}
//
//@Override
//public void delete(String username) {
//	// TODO Auto-generated method stub
//	adao.deleteById(username);
//}


}
