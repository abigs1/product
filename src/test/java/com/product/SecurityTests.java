package com.product;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityTests {

	// Debe ser la misma clave que usa JwtUtil (y la API Auth)
	private static final String SECRET = "mi_clave_super_secreta_para_jwt_segura_2026_abcdef";

	// Nombres de rol tal como vienen en el token (ajustar si la API Auth usa otros)
	private static final String ADMIN = "Admin";
	private static final String CUSTOMER = "Customer";

	@Autowired
	MockMvc mvc;

	// Fabrica un token firmado como lo haria la API Auth
	private String token(String role) {
		Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
		return Jwts.builder()
				.setSubject("usuario_prueba")
				.claim("id", 1)
				.claim("roles", List.of(role))
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 3600_000))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	private int status(String method, String url, String role) throws Exception {
		var req = method.equals("POST") ? post(url) : get(url);
		if (role != null) {
			req = req.header("Authorization", "Bearer " + token(role));
		}
		MvcResult result = mvc.perform(req).andReturn();
		return result.getResponse().getStatus();
	}

	private void assertAllowed(int status) {
		assertNotEquals(401, status, "No deberia ser 401");
		assertNotEquals(403, status, "No deberia ser 403");
	}

	// ---------- Sin token ----------
	@Test
	void sinTokenEsRechazado() throws Exception {
		org.junit.jupiter.api.Assertions.assertEquals(401, status("GET", "/category/active", null));
	}

	@Test
	void tokenFalsoEsRechazado() throws Exception {
		MvcResult r = mvc.perform(get("/category/active").header("Authorization", "Bearer abc.def.ghi")).andReturn();
		org.junit.jupiter.api.Assertions.assertEquals(401, r.getResponse().getStatus());
	}

	// ---------- Customer: permitido ----------
	@Test
	void customerPuedeVerCategoriasActivas() throws Exception {
		assertAllowed(status("GET", "/category/active", CUSTOMER));
	}

	@Test
	void customerPuedeVerDetalleDeProducto() throws Exception {
		assertAllowed(status("GET", "/product/1", CUSTOMER));
	}

	@Test
	void customerPuedeVerImagenesDeProducto() throws Exception {
		assertAllowed(status("GET", "/product/1/image", CUSTOMER));
	}

	// ---------- Customer: prohibido ----------
	@Test
	void customerNoPuedeVerTodasLasCategorias() throws Exception {
		org.junit.jupiter.api.Assertions.assertEquals(403, status("GET", "/category", CUSTOMER));
	}

	@Test
	void customerNoPuedeListarProductos() throws Exception {
		org.junit.jupiter.api.Assertions.assertEquals(403, status("GET", "/product", CUSTOMER));
	}

	@Test
	void customerNoPuedeCrearProductos() throws Exception {
		org.junit.jupiter.api.Assertions.assertEquals(403, status("POST", "/product", CUSTOMER));
	}

	// ---------- Admin: todo ----------
	@Test
	void adminPuedeVerTodasLasCategorias() throws Exception {
		assertAllowed(status("GET", "/category", ADMIN));
	}

	@Test
	void adminPuedeListarProductos() throws Exception {
		assertAllowed(status("GET", "/product", ADMIN));
	}

	@Test
	void adminPuedeCrearProductos() throws Exception {
		assertAllowed(status("POST", "/product", ADMIN));
	}
}
