package gerenciar.usuario.desafio.adapter.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import gerenciar.usuario.desafio.domain.entity.UserType;
import gerenciar.usuario.desafio.port.output.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ObjectMapper objectMapper;

    private final String baseUrl = "/api/users";
    private Long createdUserId;

    @BeforeEach
    void setup() throws Exception {

        userRepository.deleteAll();

        var requestBody = Map.of(
                "fullName", "Teste Inicial",
                "email", "teste1@email.com",
                "phone", "+55 11 99999-9999",
                "birthDate", "1990-01-01",
                "userType", "ADMIN"
        );

        MvcResult result = mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andReturn();

        Map<String, Object> responseBody = objectMapper.readValue(
                result.getResponse().getContentAsString(), Map.class);
        Map<String, Object> resultData = (Map<String, Object>) responseBody.get("result");
        createdUserId = Long.valueOf(resultData.get("id").toString());
    }


    @Test
    @DisplayName("GET /api/users retorna lista de usuários")
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get(baseUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("Teste Inicial"));
    }

    @Test
    @DisplayName("GET /api/users/{id} retorna usuário por ID")
    void testGetUserById() throws Exception {
        mockMvc.perform(get(baseUrl + "/" + createdUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdUserId))
                .andExpect(jsonPath("$.email").value("teste1@email.com"));
    }

    @Test
    @DisplayName("GET /api/users/filter retorna usuários por tipo")
    void testGetUsersByType() throws Exception {
        mockMvc.perform(get(baseUrl + "/filter")
                        .param("type", UserType.ADMIN.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userType").value("ADMIN"));
    }

    @Test
    @DisplayName("PUT /api/users/{id} atualiza usuário")
    void testUpdateUser() throws Exception {
        var updateBody = Map.of(
                "fullName", "Usuário Atualizado",
                "email", "teste2@email.com",
                "phone", "+55 11 88888-8888",
                "birthDate", "1991-02-02",
                "userType", "EDITOR"
        );

        mockMvc.perform(put(baseUrl + "/" + createdUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Usuário atualizado com sucesso!"))
                .andExpect(jsonPath("$.result.email").value("teste2@email.com"));
    }

    @Test
    @DisplayName("DELETE /api/users/{id} exclui usuário")
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete(baseUrl + "/" + createdUserId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(baseUrl + "/" + createdUserId))
                .andExpect(status().isNotFound());
    }
}
