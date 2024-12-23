package springweb.courseproject.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateCartItemRequestDto {
    @NotNull
    @Positive
    private Long bookId;
    @Positive
    private int quantity;
}
