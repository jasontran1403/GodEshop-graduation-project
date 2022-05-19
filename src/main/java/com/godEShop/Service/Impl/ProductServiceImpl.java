package com.godEShop.Service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.godEShop.Dao.ProductDAO;
import com.godEShop.Entity.Product;
import com.godEShop.Service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDAO productDAO;

    @Override
    public List<String> getProductAndOneImage() {
	// TODO Auto-generated method stub
	return productDAO.getProductAndOneImage();
    }

    @Override
    public List<Product> findAll() {
	// TODO Auto-generated method stub
	return productDAO.findAll();
    }

    @Override
    public Page<Product> findAllByNameLike(String string, Pageable pageable) {
	// TODO Auto-generated method stub
	return productDAO.findAllByNameLike(string, pageable);
    }

    @Override
    public Page<Product> findAllProductByCategoryId(int id, Pageable pageable) {
	// TODO Auto-generated method stub
	return productDAO.findAllProductByCategoryId(id, pageable);
    }

    @Override
    public Page<Product> findAllProductByBrandId(int id, Pageable pageable) {
	// TODO Auto-generated method stub
	return productDAO.findAllProductByBrandId(id, pageable);
    }

    @Override
    public Page<Product> findAllPriceAsc(String keywords, Pageable pageable) {
	// TODO Auto-generated method stub
	return productDAO.findAllPriceAsc(keywords, pageable);
    }

    @Override
    public Page<Product> findAllPriceDec(String keywords, Pageable pageable) {
	// TODO Auto-generated method stub
	return productDAO.findAllPriceDec(keywords, pageable);
    }

    @Override
    public Page<Product> findAllNewProduct(String keywords, Pageable pageable) {
	// TODO Auto-generated method stub
	return productDAO.findAllNewProduct(keywords, pageable);
    }

    @Override
    public List<Product> getProductByPopularity() {
	// TODO Auto-generated method stub
	List<Product> lstProduct = new ArrayList<>();
	List<Long> lstProductId = productDAO.getProductByPopularity();
	lstProductId.forEach(a -> {
	    lstProduct.add(productDAO.getById(a));
	});
	return lstProduct;
    }

    @Override
    public List<Product> getProductByRating() {
	// TODO Auto-generated method stub
	List<Product> lstProduct = new ArrayList<>();
	List<Long> lstProductId = productDAO.getProductByRating();
	lstProductId.forEach(a -> {
	    lstProduct.add(productDAO.getById(a));
	});
	return lstProduct;
    }

}