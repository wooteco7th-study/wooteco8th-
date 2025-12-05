package store.domain;

import java.util.List;
import store.FileReader;
import store.util.Parser;

public class PromotionInitializer {

    public Promotions initialize() {
        Promotions promotions = new Promotions();
        for (String promotion : FileReader.readPromotions()) {
            addPromotionFromLine(promotion, promotions);
        }
        return promotions;
    }

    private static void addPromotionFromLine(String promotion, Promotions promotions) {
        List<String> promotionInfo = Parser.parseByDelimiter(promotion);

        String name = promotionInfo.get(0);
        String buy = promotionInfo.get(1);
        String get = promotionInfo.get(2);
        String startDate = promotionInfo.get(3);
        String endDate = promotionInfo.get(4);

        promotions.add(Promotion.of(name, buy, get, startDate, endDate));
    }
}
