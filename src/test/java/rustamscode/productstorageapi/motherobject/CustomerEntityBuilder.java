package rustamscode.productstorageapi.motherobject;

import lombok.Getter;
import rustamscode.productstorageapi.persistance.entity.CustomerEntity;

import java.util.UUID;

@Getter
public class CustomerEntityBuilder {
  public static final Long DEFAULT_ID = null;
  public static final String DEFAULT_LOGIN = "Login Test";
  public static final String DEFAULT_EMAIL = "Email Test";
  public static final Boolean DEFAULT_IS_ACTIVE = true;

  private Long id = DEFAULT_ID;
  private String login = DEFAULT_LOGIN;
  private String email = DEFAULT_EMAIL;
  private Boolean isActive = DEFAULT_IS_ACTIVE;

  private CustomerEntityBuilder() {
  }

  protected static CustomerEntityBuilder getInstance() {
    return new CustomerEntityBuilder();
  }

  public CustomerEntityBuilder withId(final Long id) {
    this.id = id;
    return this;
  }

  public CustomerEntityBuilder withLogin(final String login) {
    this.login = login;
    return this;
  }

  public CustomerEntityBuilder withEmail(final String email) {
    this.email = email;
    return this;
  }

  public CustomerEntityBuilder withIsActive(final Boolean isActive) {
    this.isActive = isActive;
    return this;
  }

  public CustomerEntity build() {
    return new CustomerEntity(id, login, email, isActive);
  }
}
