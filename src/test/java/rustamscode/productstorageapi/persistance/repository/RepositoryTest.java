package rustamscode.productstorageapi.persistance.repository;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
abstract class RepositoryTest {

  abstract Object getRepository();
}
