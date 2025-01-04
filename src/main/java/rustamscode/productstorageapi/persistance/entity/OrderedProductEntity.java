package rustamscode.productstorageapi.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderedProductEntity {

  @EmbeddedId
  OrderedProductEntityKey id;

  @ManyToOne()
  @JoinColumn(name = "order_id", nullable = false, insertable = false, updatable = false)
  OrderEntity order;

  @ManyToOne()
  @JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
  ProductEntity product;

  @Column(name = "price", table = "ordered_product", nullable = false)
  BigDecimal price;

  @Column(name = "amount", table = "ordered_product", nullable = false)
  BigDecimal amount;
}
