package com.fastride.infrastructure.api.account.v1;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fastride.FastRideApplication;
import com.fastride.PostgresTestContainerInitializer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = FastRideApplication.class)
@AutoConfigureMockMvc
@Testcontainers
@ContextConfiguration(initializers = PostgresTestContainerInitializer.class)
@Transactional
class AccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldCallPostEndpointForAccountAndSignUpPassengerSuccessfullyWithStatusCreated() throws Exception {
		String inputJson = "{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"cpf\":\"136.803.160-97\",\"passenger\": true}";

		ResultActions result = mockMvc
				.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(inputJson));

		result.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.accountId").isNotEmpty()).andExpect(jsonPath("$.name").value("John Doe"))
				.andExpect(jsonPath("$.email").value("john.doe@example.com"))
				.andExpect(jsonPath("$.cpf").value("136.803.160-97"))
				.andExpect(jsonPath("$.carPlate").value(nullValue())).andExpect(jsonPath("$.passenger").value("true"))
				.andExpect(jsonPath("$.driver").value("false"));
	}

	@Test
	void shouldCallPostEndpointForAccountAndSignUpDriverSuccessfullyWithStatusCreated() throws Exception {
		String inputJson = "{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"cpf\":\"136.803.160-97\",\"driver\": true,\"carPlate\":\"ABC1234\"}";

		ResultActions result = mockMvc
				.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(inputJson));

		result.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.accountId").isNotEmpty()).andExpect(jsonPath("$.name").value("John Doe"))
				.andExpect(jsonPath("$.email").value("john.doe@example.com"))
				.andExpect(jsonPath("$.cpf").value("136.803.160-97")).andExpect(jsonPath("$.carPlate").value("ABC1234"))
				.andExpect(jsonPath("$.passenger").value("false")).andExpect(jsonPath("$.driver").value("true"));
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
				"\"objects\":[{\"name\":\"cpf\",\"userMessage\":\"Invalid CPF.\"},{\"name\":\"cpf\",\"userMessage\":\"CPF is required.\"",
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
