package rustamscode.productstorageapi.persistance.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import rustamscode.productstorageapi.enumeration.OrderStatus;

import java.util.List;
import java.util.UUID;

@Table(name = "\"order\"")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderEntity {

  @Id
  @GeneratedValue
  @UuidGenerator
  @Column(name = "id", nullable = false)
  UUID id;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  CustomerEntity customer;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status", nullable = false)
  OrderStatus orderStatus;

  @Column(name = "delivery_address", nullable = false)
  String deliveryAddress;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  List<OrderedProductEntity> orderedProducts;
}