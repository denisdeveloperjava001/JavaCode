package com.javacode.demo;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javacode.demo.controller.WalletController;
import com.javacode.demo.model.OperationType;
import com.javacode.demo.model.Wallet;
import com.javacode.demo.model.WalletOperationParameters;
import com.javacode.demo.repository.WalletRepository;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@AutoConfigureEmbeddedDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WalletApplicationTests {
	private final UUID walletId = UUID.randomUUID();

	@Autowired
	private WebApplicationContext context;

	private ObjectMapper objectMapper = new ObjectMapper();

	private MockMvc mvc;

	@BeforeAll
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.build();
	}

	@Test
	void mainTest() throws Exception {
		putNewWalletTest();
		putToExistingWalletTest();
		withdrawFromExistingWalletTest();
		withdrawFromExistingWalletAndNotEnoughFundsTest();
		withdrawFromNotExistingWalletTest();
		getNotExistingWallet();
		getExistingWallet();
	}

	void putNewWalletTest() throws Exception {
		WalletOperationParameters walletOperationParameters = new WalletOperationParameters(walletId, OperationType.DEPOSIT, 1000);
		mvc.perform(
				post("/api/v1/wallet")
						.content(objectMapper.writeValueAsString(walletOperationParameters))
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(1000))
				.andExpect(MockMvcResultMatchers.jsonPath("$.walletId").value(walletId.toString()));
	}

	void putToExistingWalletTest() throws Exception {
		WalletOperationParameters walletOperationParameters = new WalletOperationParameters(walletId, OperationType.DEPOSIT, 1000);
		mvc.perform(
						post("/api/v1/wallet")
								.content(objectMapper.writeValueAsString(walletOperationParameters))
								.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(2000))
				.andExpect(MockMvcResultMatchers.jsonPath("$.walletId").value(walletId.toString()));
	}

	void withdrawFromExistingWalletTest() throws Exception {
		WalletOperationParameters walletOperationParameters = new WalletOperationParameters(walletId, OperationType.WITHDRAW, 1000);
		mvc.perform(
						post("/api/v1/wallet")
								.content(objectMapper.writeValueAsString(walletOperationParameters))
								.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(1000))
				.andExpect(MockMvcResultMatchers.jsonPath("$.walletId").value(walletId.toString()));
	}

	void withdrawFromExistingWalletAndNotEnoughFundsTest() throws Exception {
		WalletOperationParameters walletOperationParameters = new WalletOperationParameters(walletId, OperationType.WITHDRAW, 2000);
		mvc.perform(
						post("/api/v1/wallet")
								.content(objectMapper.writeValueAsString(walletOperationParameters))
								.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	void withdrawFromNotExistingWalletTest() throws Exception {
		WalletOperationParameters walletOperationParameters = new WalletOperationParameters(UUID.randomUUID(), OperationType.WITHDRAW, 1000);
		mvc.perform(
						post("/api/v1/wallet")
								.content(objectMapper.writeValueAsString(walletOperationParameters))
								.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	void getNotExistingWallet() throws Exception {
		mvc.perform(get("/api/v1/wallets/"+UUID.randomUUID()))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	void getExistingWallet() throws Exception {
		mvc.perform(get("/api/v1/wallets/"+ walletId))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("1000.0"));
	}

}
