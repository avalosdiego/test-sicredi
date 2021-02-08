package com.avalos.sicredi.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PautaControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void deveCadastrarPauta() throws Exception {
		URI uri = new URI("/pautas/v2");

		String json = 
				"{"
				+ 	"\"descricao\": \"Pauta para teste \", "
				+ 	"\"titulo\": \"Pauta 1\""
				+ "}";

		mockMvc
			.perform(
					MockMvcRequestBuilders
					.post(uri)
					.content(json)
					.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}
	
	@Test
	void deveDetalharPauta() throws Exception {
		URI uri = new URI("/pautas/v2/1");

		mockMvc
			.perform(
					MockMvcRequestBuilders
					.get(uri)
					.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.titulo").value("Pauta 1"));
	}
	
	@Test
	void devAbrirSessao() throws Exception {
		URI uri = new URI("/pautas/v2/1/abrirSessao");

		mockMvc
			.perform(
					MockMvcRequestBuilders
					.put(uri)
					.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

}
