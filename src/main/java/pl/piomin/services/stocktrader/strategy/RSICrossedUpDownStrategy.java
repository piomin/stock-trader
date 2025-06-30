package pl.piomin.services.stocktrader.strategy;

import org.springframework.stereotype.Service;
import org.ta4j.core.*;
import org.ta4j.core.backtest.BarSeriesManager;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;

@Service
public class RSICrossedUpDownStrategy implements BuildAndRunStrategy {

    public TradingRecord buildAndRun(BarSeries series) {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        RSIIndicator rsi = new RSIIndicator(closePrice, 14);

        Rule entryRule = new CrossedDownIndicatorRule(rsi, 30);
        Rule exitRule = new CrossedUpIndicatorRule(rsi, 75);
        Strategy strategy = new BaseStrategy(entryRule, exitRule);
        BarSeriesManager seriesManager = new BarSeriesManager(series);
        TradingRecord tradingRecord = seriesManager.run(strategy);
        return tradingRecord;
    }
}
