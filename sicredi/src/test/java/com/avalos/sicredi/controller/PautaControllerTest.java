package com.avalos.sicredi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.avalos.sicredi.form.PautaForm;
import com.avalos.sicredi.model.StatusPauta;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PautaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	void case1__cadastraPauta() throws Exception {
		PautaForm pautaForm = new PautaForm();
		pautaForm.setTitulo("Teste Controller");
		pautaForm.setDescricao("Teste Controller Descrição");

		mockMvc.perform(
				post("/pautas/v2")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(pautaForm)))
				.andExpect(status().isCreated());

	}

	@Test
	void case2__detalhaPauta() throws Exception {
		URI uri = new URI("/pautas/v2/1");

		mockMvc.perform(
				get(uri)
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
				.andExpect(jsonPath("$.titulo").value("Teste Controller"));
	}

	@Test
	void case3__abreSessao() throws Exception {
		URI uri = new URI("/pautas/v2/1/abrirSessao");

		mockMvc.perform(
				put(uri)
					.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.titulo").value("Teste Controller"))
				.andExpect(jsonPath("$.status").value(StatusPauta.OPENED.toString()));
	}

}
