package rustamscode.productstorageapi.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
abstract class ServiceTest {

  abstract Object getService();
}
