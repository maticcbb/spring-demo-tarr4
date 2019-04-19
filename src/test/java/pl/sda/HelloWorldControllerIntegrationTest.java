package pl.sda;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
// automatycznie konfiguruje MockMvc
@AutoConfigureMockMvc
// wykonuje kazdy test z nowo podniesiona aplikacja (Wydluza to czas testow , ale zapewnia izolacje testow )
// @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class HelloWorldControllerIntegrationTest {

    //klasa do testow wersji prezentacji
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private GreetingsRepository greetingsRepository;

    @BeforeEach
    void cleanDB() {
        greetingsRepository.deleteAll();
    }


    @DisplayName("when call GET on /greetings, then 200 status is" +
            " returned and empty json array")
    @Test
    void test() throws Exception {
        // given

        //sprawdzamy get na /greetings
        // when
        mockMvc.perform(get("/greetings"))

                // oczekujemy odpowiedzi statusu 200 i zwraca content (body ktore zwraca status)
                // then
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @DisplayName("given one message in the system," +
            "when call Get on greetings," +
            "then 200 status and json array of size 1 is returned")
    @Test
    void test1() throws Exception {
        //given
        postGrretingWithMessage("hello");

        //when
        mockMvc.perform(get("/greetings"))
                .andDo(MockMvcResultHandlers.print())

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].msg", is("hello")));

    }

    @DisplayName("given two greetings in the database, " +
            "when call GET on single greeting endpoint" +
            "then status OK and matching greeting is returned")
    @Test
    void test2() throws Exception {
        //given
        postGrretingWithMessage("hello");
        postGrretingWithMessage("hi");
        int hiGreeting = postGrretingWithMessage("hi");

        //when
        mockMvc.perform(get("/greetings/{id}", hiGreeting))

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg", is("hi")))
                .andExpect(jsonPath("$.id", is(hiGreeting)));
    }

    private int postGrretingWithMessage(String msg) throws Exception {
        String createdGreetingAsJson = String.format("{ \"msg\":\"%s\"}", msg);
        String responseBodyAsJson = mockMvc.perform(post("/greetings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createdGreetingAsJson)
        ).andExpect(status()
                .isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return (int) objectMapper.readValue(
                responseBodyAsJson, Greeting.class).getId();

    }

    @DisplayName(
            "given two greetings in the database" +
                    "when call DELETE on a single greeting endpoint," +
                    "then status is OK and matching greeting is removed"
    )
    @Test
    public void test3() throws Exception {

        // given
        int helloGreetingId = postGrretingWithMessage("hello");
        postGrretingWithMessage("hi");

        // when
        mockMvc.perform(delete("/greetings/{id}", helloGreetingId))

                // then
                .andExpect(status().isOk());
        mockMvc.perform(get("/greetings"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].msg", is("hi")));
    }
}