package store.dto;

public record OrderRequest(
        String productName,
        int quantity
) {
}
