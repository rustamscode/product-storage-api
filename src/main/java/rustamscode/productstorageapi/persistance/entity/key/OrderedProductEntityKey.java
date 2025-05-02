package rustamscode.productstorageapi.persistance.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class OrderedProductEntityKey implements Serializable {

  @Column(name = "product_id")
  private UUID productId;

  @Column(name = "order_id")
  private UUID orderId;
}
