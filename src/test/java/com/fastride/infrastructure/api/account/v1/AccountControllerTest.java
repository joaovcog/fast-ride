package com.fastride.infrastructure.api.account.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.regex.Pattern;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fastride.IntegrationTest;

@IntegrationTest
@AutoConfigureMockMvc
class AccountControllerTest {

	private static final String VALID_ID_PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldCallPostEndpointForAccountAndSignUpPassengerSuccessfullyWithStatusCreated() throws Exception {
		String inputJson = "{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"cpf\":\"136.803.160-97\",\"passenger\": true}";

		ResultActions result = mockMvc
				.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(inputJson));

		String responseContent = result.andReturn().getResponse().getContentAsString();
		result.andExpect(status().isCreated()).andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
				.andExpect(content().string(Matchers.not(Matchers.emptyString())));
		assertTrue(Pattern.matches(VALID_ID_PATTERN, responseContent));
	}

	@Test
	void shouldCallPostEndpointForAccountAndSignUpDriverSuccessfullyWithStatusCreated() throws Exception {
		String inputJson = "{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"cpf\":\"136.803.160-97\",\"driver\": true,\"carPlate\":\"ABC1234\"}";

		ResultActions result = mockMvc
				.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(inputJson));
		String responseContent = result.andReturn().getResponse().getContentAsString();
		result.andExpect(status().isCreated()).andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
				.andExpect(content().string(Matchers.not(Matchers.emptyString())));
		assertTrue(Pattern.matches(VALID_ID_PATTERN, responseContent));
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "\"\"" })
	void shouldCallPostEndpointForAccountAndNotSignUpWhenEmailIsNotProvidedReturningBadRequestWithMessageEmailIsRequired(
			String email) throws Exception {
		String inputJson = String.format(
				"{\"name\":\"John Doe\",\"email\":%s,\"cpf\":\"136.803.160-97\",\"driver\": true,\"carPlate\":\"ABC1234\"}",
				email);

		MockHttpServletRequestBuilder request = post("/accounts").contentType(MediaType.APPLICATION_JSON)
				.content(inputJson);
		MockHttpServletResponse response = mockMvc.perform(request).andExpect(status().isBadRequest()).andReturn()
				.getResponse();
		assertNotNull(response);
		String contentAsString = response.getContentAsString();
		String fieldsWithErrors = contentAsString.substring(contentAsString.indexOf("\"objects"),
				contentAsString.indexOf("}]}"));
		assertEquals("\"objects\":[{\"name\":\"email\",\"userMessage\":\"E-mail is required.\"", fieldsWithErrors);
	}

	@ParameterizedTest
	@ValueSource(strings = { "john.doe", "john", "23@", "john.email.com" })
	void shouldCallPostEndpointForAccountAndNotSignUpWhenEmailIsInvalidReturningBadRequestWithMessageInvalidEmail(
			String email) throws Exception {
		String inputJson = String.format(
				"{\"name\":\"John Doe\",\"email\":\"%s\",\"cpf\":\"136.803.160-97\",\"driver\": true,\"carPlate\":\"ABC1234\"}",
				email);

		MockHttpServletRequestBuilder request = post("/accounts").contentType(MediaType.APPLICATION_JSON)
				.content(inputJson);
		MockHttpServletResponse response = mockMvc.perform(request).andExpect(status().isBadRequest()).andReturn()
				.getResponse();
		assertNotNull(response);
		String contentAsString = response.getContentAsString();
		String fieldsWithErrors = contentAsString.substring(contentAsString.indexOf("\"objects"),
				contentAsString.indexOf("}]}"));
		assertEquals("\"objects\":[{\"name\":\"email\",\"userMessage\":\"Invalid E-mail.\"", fieldsWithErrors);
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "\"\"" })
	void shouldCallPostEndpointForAccountAndNotSignUpWhenNameIsNotProvidedReturningBadRequestWithMessageNameIsRequired(
			String name) throws Exception {
		String inputJson = String.format(
				"{\"name\":%s,\"email\":\"john.doe@example.com\",\"cpf\":\"136.803.160-97\",\"driver\": true,\"carPlate\":\"ABC1234\"}",
				name);

		MockHttpServletRequestBuilder request = post("/accounts").contentType(MediaType.APPLICATION_JSON)
				.content(inputJson);
		MockHttpServletResponse response = mockMvc.perform(request).andExpect(status().isBadRequest()).andReturn()
				.getResponse();
		assertNotNull(response);
		String contentAsString = response.getContentAsString();
		String fieldsWithErrors = contentAsString.substring(contentAsString.indexOf("\"objects"),
				contentAsString.indexOf("}]}"));
		assertEquals("\"objects\":[{\"name\":\"name\",\"userMessage\":\"Name is required.\"", fieldsWithErrors);
	}

	@ParameterizedTest
	@ValueSource(strings = { "\"John Smith$\"", "\"John 5\"", "\"4553\"" })
	void shouldCallPostEndpointForAccountAndNotSignUpWhenNameIsInvalidReturningUnprocessableEntityWithMessageInvalidName(
			String name) throws Exception {
		String inputJson = String.format(
				"{\"name\":%s,\"email\":\"john.doe@example.com\",\"cpf\":\"136.803.160-97\",\"driver\": true,\"carPlate\":\"ABC1234\"}",
				name);

		MockHttpServletRequestBuilder request = post("/accounts").contentType(MediaType.APPLICATION_JSON)
				.content(inputJson);
		MockHttpServletResponse response = mockMvc.perform(request).andExpect(status().isUnprocessableEntity())
				.andReturn().getResponse();
		assertNotNull(response);
		String contentAsString = response.getContentAsString();
		String errorMessage = contentAsString.substring(contentAsString.indexOf("\"userMessage"),
				contentAsString.indexOf("}"));
		assertEquals("\"userMessage\":\"Invalid name! The name should have only letters.\"", errorMessage);
	}

	@Test
	void shouldCallPostEndpointForAccountAndNotSignUpWhenCpfIsNullReturningBadRequestWithMessageCpfIsRequired()
			throws Exception {
		String inputJson = "{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"cpf\": null,\"driver\": true,\"carPlate\":\"ABC1234\"}";

		MockHttpServletRequestBuilder request = post("/accounts").contentType(MediaType.APPLICATION_JSON)
				.content(inputJson);
		MockHttpServletResponse response = mockMvc.perform(request).andExpect(status().isBadRequest()).andReturn()
				.getResponse();
		assertNotNull(response);
		String contentAsString = response.getContentAsString();
		String fieldsWithErrors = contentAsString.substring(contentAsString.indexOf("\"objects"),
				contentAsString.indexOf("}]}"));
		assertEquals("\"objects\":[{\"name\":\"cpf\",\"userMessage\":\"CPF is required.\"", fieldsWithErrors);
	}

	@Test
	void shouldCallPostEndpointForAccountAndNotSignUpWhenCpfIsEmptyReturningBadRequestWithMessageCpfIsRequiredAndInvalid()
			throws Exception {
		String inputJson = "{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"cpf\": \"\",\"driver\": true,\"carPlate\":\"ABC1234\"}";

		MockHttpServletRequestBuilder request = post("/accounts").contentType(MediaType.APPLICATION_JSON)
				.content(inputJson);
		MockHttpServletResponse response = mockMvc.perform(request).andExpect(status().isBadRequest()).andReturn()
				.getResponse();
		assertNotNull(response);
		String contentAsString = response.getContentAsString();
		String fieldsWithErrors = contentAsString.substring(contentAsString.indexOf("\"objects"),
				contentAsString.indexOf("}]}"));
		assertEquals(
				"\"objects\":[{\"name\":\"cpf\",\"userMessage\":\"CPF is required.\"},{\"name\":\"cpf\",\"userMessage\":\"Invalid CPF.\"",
				fieldsWithErrors);
	}

	@ParameterizedTest
	@ValueSource(strings = { "12345678910", "11111111111", "22222222222", "234bc" })
	void shouldCallPostEndpointForAccountAndNotSignUpWhenCpfIsInvalidReturningBadRequestWithMessageInvalidCpf(
			String cpf) throws Exception {
		String inputJson = String.format(
				"{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"cpf\":\"%s\",\"driver\": true,\"carPlate\":\"ABC1234\"}",
				cpf);

		MockHttpServletRequestBuilder request = post("/accounts").contentType(MediaType.APPLICATION_JSON)
				.content(inputJson);
		MockHttpServletResponse response = mockMvc.perform(request).andExpect(status().isBadRequest()).andReturn()
				.getResponse();
		assertNotNull(response);
		String contentAsString = response.getContentAsString();
		String fieldsWithErrors = contentAsString.substring(contentAsString.indexOf("\"objects"),
				contentAsString.indexOf("}]}"));
		assertEquals("\"objects\":[{\"name\":\"cpf\",\"userMessage\":\"Invalid CPF.\"", fieldsWithErrors);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "AA", "ABC-234", "AB-1234", "7896-ABC", "5462", "ABC" })
	void shouldCallPostEndpointForAccountAndNotSignUpDriverWhenCarPlateIsInvalidReturningBadRequestWithMessageInvalidCarPlate(
			String carPlate) throws Exception {
		String inputJson = String.format(
				"{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"cpf\":\"136.803.160-97\",\"driver\": true,\"carPlate\":\"%s\"}",
				carPlate);

		MockHttpServletRequestBuilder request = post("/accounts").contentType(MediaType.APPLICATION_JSON)
				.content(inputJson);
		MockHttpServletResponse response = mockMvc.perform(request).andExpect(status().isUnprocessableEntity())
				.andReturn().getResponse();
		assertNotNull(response);
		String contentAsString = response.getContentAsString();
		String errorMessage = contentAsString.substring(contentAsString.indexOf("\"userMessage"),
				contentAsString.indexOf("}"));
		assertEquals(
				"\"userMessage\":\"Invalid car plate! Please, type a valid car plate with 3 letters and 4 numbers for signing up.\"",
				errorMessage);
	}

}
