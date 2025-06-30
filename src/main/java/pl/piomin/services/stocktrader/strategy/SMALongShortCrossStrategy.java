package pl.piomin.services.stocktrader.strategy;

import org.springframework.stereotype.Service;
import org.ta4j.core.*;
import org.ta4j.core.backtest.BarSeriesManager;
import org.ta4j.core.indicators.averages.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;

@Service
public class SMALongShortCrossStrategy implements BuildAndRunStrategy {

    @Override
    public TradingRecord buildAndRun(BarSeries series) {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        SMAIndicator shortSma = new SMAIndicator(closePrice, 15);
        SMAIndicator longSma = new SMAIndicator(closePrice, 200);
        Rule entryRule = new CrossedUpIndicatorRule(shortSma, longSma);
        Rule exitRule = new CrossedDownIndicatorRule(longSma, shortSma);
        Strategy strategy = new BaseStrategy(entryRule, exitRule);
        BarSeriesManager seriesManager = new BarSeriesManager(series);
        TradingRecord tradingRecord = seriesManager.run(strategy);
        return tradingRecord;
    }
}
