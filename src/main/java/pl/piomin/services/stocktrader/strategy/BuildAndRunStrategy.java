package pl.piomin.services.stocktrader.strategy;

import org.ta4j.core.BarSeries;
import org.ta4j.core.TradingRecord;

public interface BuildAndRunStrategy {
    TradingRecord buildAndRun(BarSeries series);
}
