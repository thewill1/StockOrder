package com.example.Entities;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;

public enum TradeAction {
    BUY,
    SELL;

    public static Map<TradeAction, Comparator<Order>> ACTION_COMPARATOR_MAP;

    static {
        ACTION_COMPARATOR_MAP = new EnumMap<>(TradeAction.class);
        ACTION_COMPARATOR_MAP.put(TradeAction.BUY, (o1, o2) -> {
            float price1 = o1.getPrice();
            float price2 = o2.getPrice();

            if (price1 == price2) {
                return o1.getTime() - o2.getTime();
            }

            return price2 > price1 ? 1 : -1;
        });
        ACTION_COMPARATOR_MAP.put(TradeAction.SELL, (o1, o2) -> {
            float price1 = o1.getPrice();
            float price2 = o2.getPrice();

            if (price1 == price2) {
                return o1.getTime() - o2.getTime();
            }

            return price2 > price1 ? -1 : 1;
        });
    }
}
