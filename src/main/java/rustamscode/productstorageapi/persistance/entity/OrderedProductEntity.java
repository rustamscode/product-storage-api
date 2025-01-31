package rustamscode.productstorageapi.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import rustamscode.productstorageapi.persistance.entity.key.OrderedProductEntityKey;

import java.math.BigDecimal;

@Table(name = "ordered_product")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderedProductEntity {

  @EmbeddedId
  OrderedProductEntityKey id;

  @ManyToOne()
  @MapsId("orderId")
  OrderEntity order;

  @ManyToOne()
  @MapsId("productId")
  ProductEntity product;

  @Column(name = "price", table = "ordered_product", nullable = false)
  BigDecimal price;

  @Column(name = "amount", table = "ordered_product", nullable = false)
  BigDecimal amount;
}
