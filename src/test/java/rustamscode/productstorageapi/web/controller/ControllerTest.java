package rustamscode.productstorageapi.web.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PROTECTED)
abstract class ControllerTest {

  MockMvc mockMvc;

  ObjectMapper objectMapper = new ObjectMapper()
          .registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES))
          .registerModule(new JavaTimeModule());

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(getController())
            .setCustomArgumentResolvers(getArgumentResolver())
            .setControllerAdvice(getExceptionHandler())
            .build();
  }

  protected abstract Object getController();

  protected abstract HandlerMethodArgumentResolver getArgumentResolver();

  protected abstract Object getExceptionHandler();
}
