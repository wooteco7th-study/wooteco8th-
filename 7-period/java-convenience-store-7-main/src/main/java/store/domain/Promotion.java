package store.domain;

import java.time.LocalDate;

public class Promotion {

    private final String name;
    private final int buy;
    private final int get;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final PromotionType type;

    public enum PromotionType {
        EXISTENT, NONE
    }

    public static Promotion none() {
        return new Promotion("null", 0, 0,
                LocalDate.MIN, LocalDate.MIN, PromotionType.NONE);
    }

    public static Promotion of(String name, String buy, String get, String startDate, String endDate) {
        return new Promotion(name, Integer.parseInt(buy), Integer.parseInt(get),
                LocalDate.parse(startDate), LocalDate.parse(endDate), PromotionType.EXISTENT);
    }

    public Promotion(String name, int buy, int get,
                     LocalDate startDate, LocalDate endDate, PromotionType type) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    public boolean hasSameName(String promotionName) {
        return this.name.equals(promotionName);
    }

    public boolean isActive(LocalDate date) {
        return exists()
                && (date.isAfter(startDate) || date.isEqual(startDate))
                && (date.isBefore(endDate) || date.isEqual(endDate));
    }

    public boolean exists() {
        return type == PromotionType.EXISTENT;
    }

    public boolean canGetFreeProduct(int purchasedQuantity, int promotionQuantity) {
        return purchasedQuantity % (buy + get) == buy
                && promotionQuantity >= purchasedQuantity;
    }

    public String getName() {
        return name;
    }

    public int getGet() {
        return get;
    }
}
