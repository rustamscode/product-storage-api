package rustamscode.productstorageapi.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "ordered_products")
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderedProductEntity {

  @Id
  @GeneratedValue
  @UuidGenerator
  UUID id;

  @ManyToOne
  @JoinColumn(name = "order_id", nullable = false)
  OrderEntity order;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  ProductEntity product;

  @Column(name = "ordered_product_price", table = "ordered_products", nullable = false)
  BigDecimal orderedProductPrice;

  @Column(name = "ordered_product_amount", table = "ordered_products", nullable = false)
  BigDecimal orderedProductAmount;
}
