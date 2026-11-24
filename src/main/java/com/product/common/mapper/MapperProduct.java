package com.product.common.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.product.api.dto.in.DtoProductIn;
import com.product.api.dto.out.DtoProductListOut;
import com.product.api.dto.out.DtoProductOut;
import com.product.api.entity.Product;

@Service
public class MapperProduct {

	// Convierte la lista de entidades en la lista que se regresa en GET /product
	public List<DtoProductListOut> fromProductList(List<Product> products) {
		List<DtoProductListOut> list = new ArrayList<>();
		for (Product product : products) {
			list.add(new DtoProductListOut(
					product.getProductId(),
					product.getGtin(),
					product.getProduct(),
					product.getPrice(),
					product.getStatus()));
		}
		return list;
	}

	// Convierte una entidad en el detalle que se regresa en GET /product/{id}
	public DtoProductOut fromProduct(Product product) {
		DtoProductOut dto = new DtoProductOut();
		dto.setProductId(product.getProductId());
		dto.setGtin(product.getGtin());
		dto.setProduct(product.getProduct());
		dto.setDescription(product.getDescription());
		dto.setPrice(product.getPrice());
		dto.setStock(product.getStock());
		dto.setCategoryId(product.getCategoryId());
		dto.setStatus(product.getStatus());
		return dto;
	}

	// Convierte el body de una alta en una entidad nueva
	public Product fromDto(DtoProductIn dto) {
		Product product = new Product();
		product.setGtin(dto.getGtin());
		product.setProduct(dto.getProduct());
		product.setDescription(dto.getDescription());
		product.setPrice(dto.getPrice());
		product.setStock(dto.getStock());
		product.setCategoryId(dto.getCategoryId());
		product.setStatus(1);
		return product;
	}

	// Igual que el anterior, pero con el id del producto (para actualizar)
	public Product fromDto(Integer id, DtoProductIn dto) {
		Product product = fromDto(dto);
		product.setProductId(id);
		return product;
	}
}
