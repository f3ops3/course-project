package springweb.courseproject.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import springweb.courseproject.model.Status;

@Data
public class PatchOrderRequestDto {
    @NotNull
    private Status status;
}
